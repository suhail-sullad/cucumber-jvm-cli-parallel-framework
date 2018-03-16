package init;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.cfg4j.provider.GenericType;
import org.joda.time.DateTime;

import com.intuit.karate.cucumber.CucumberRunner;
import com.intuit.karate.cucumber.KarateFeature;
import com.intuit.karate.cucumber.KarateJunitAndJsonReporter;
import com.intuit.karate.cucumber.KarateRuntime;
import com.intuit.karate.filter.TagFilter;
import com.intuit.karate.filter.TagFilterException;
import com.jayway.jsonpath.internal.JsonFormatter;

import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberTagStatement;

public class RunCukesTest {
	/*
	 * private static int uiinvocationcount = 0; private static int
	 * apiinvocationcount = 0; private static int maxinvocationcount = 0;
	 */
	private static ExecutorService featureRunner = null;
	private static List<CompletableFuture<Supplier<Byte>>> featureStatus = new ArrayList<>();
	private static Logger log = Logger.getLogger(RunCukesTest.class);

	public static void main(String[] args) throws Exception {
		log.info("Initializing properties...");
		PropertyLoader.init();
		List<ExecutionModes> parallelModes = PropertyLoader.provider.getProperty("parallel",
				new GenericType<List<ExecutionModes>>() {
				});
		log.info("Initializing ThreadPool...");
		featureRunner = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

		log.info("Selecting ExecutionModes...");
		for (ExecutionModes em : parallelModes) {
			switch (em) {
			case API_TAG_PARALLEL:
				run_api_parallel(true);
				break;
			case API_FEATURE_SEQUENTIAL:
				run_api_features_sequential();
				break;
			case API_FEATURE_PARALLEL:
				run_api_parallel(false);
				break;
			case UI_TAG_PARALLEL:
				run_ui_tags_in_parallel();
				break;
			case UI_FEATURE_PARALLEL:
				run_ui_features_in_parallel();
				break;
			case UI_FEATURE_SEQUENTIAL:
				run_ui_sequentially();
				break;

			}
		}

		CompletableFuture.allOf(featureStatus.toArray(new CompletableFuture[featureStatus.size()]));
		featureRunner.shutdown();
	}

	private static void run_api_parallel(Boolean isTags) throws IOException, InterruptedException, TagFilterException {
		List<String> features = getfilelist(PropertyLoader.provider.getProperty("apifeaturefilepath", String.class),
				"feature");
		for (String feature : features) {
			executeApiTests(feature, isTags);
		}
	}

	private static void run_api_features_sequential() throws IOException {

		List<String> features = getfilelist(PropertyLoader.provider.getProperty("apifeaturefilepath", String.class),
				"feature");
		JsonFormatter jf = new JsonFormatter();

		for (String string : features) {
			File file = new File(string);
			KarateFeature kf = new KarateFeature(file);
			KarateJunitAndJsonReporter reporter = new KarateJunitAndJsonReporter(file.getPath(),
					"./target/cucumber-reports/api/" + file.getName() + ".json");
			KarateRuntime runtime = kf.getRuntime(reporter);

			kf.getFeature().run(reporter, reporter, runtime);
			reporter.done();
		}
	}

	private static void run_ui_sequentially() throws IOException, InterruptedException {
		List<String> arguments = new ArrayList<String>();
		arguments.addAll(getfilelist(PropertyLoader.provider.getProperty("featurefilepath", String.class), "feature"));
		arguments.add("--format");
		arguments.add("pretty");
		arguments.add("--format");
		arguments.add("json:./target/cucumber-reports/" + DateTime.now().toDateTimeISO().toString("hhmmssddMMyyyy")
				+ ".json");
		List<String> tags = PropertyLoader.provider.getProperty("tagstorun", new GenericType<List<String>>() {
		});
		for (String runnabletags : tags) {
			if (!runnabletags.contains("none")) {
				arguments.add("--tags");

				// arguments.add("@" + runnabletags);
				arguments.add(runnabletags);
			}
		}
		List<String> gluepackages = PropertyLoader.provider.getProperty("gluedpackages",
				new GenericType<List<String>>() {
				});
		for (String packages : gluepackages) {
			if (!packages.contains("none")) {
				arguments.add("--glue");
				arguments.add(packages);
			}
		}

		final String[] argv = arguments.toArray(new String[0]);
		executeUITests(argv);

	}

	public static void run_ui_tags_in_parallel() throws IOException, InterruptedException {

		List<String> tags = PropertyLoader.provider.getProperty("tagstorun", new GenericType<List<String>>() {
		});
		for (String runnabletags : tags) {
			List<String> arguments = new ArrayList<String>();
			arguments.addAll(
					getfilelist(PropertyLoader.provider.getProperty("featurefilepath", String.class), "feature"));
			arguments.add("--format");
			arguments.add("pretty");
			arguments.add("--format");
			arguments.add("json:./target/cucumber-reports/" + DateTime.now().toDateTimeISO().toString("hhmmssddMMyyyy")
					+ ".json");
			if (!runnabletags.contains("none")) {
				arguments.add("--tags");
				// arguments.add("@" + runnabletags);
				arguments.add(runnabletags);
			}

			List<String> gluepackages = PropertyLoader.provider.getProperty("gluedpackages",
					new GenericType<List<String>>() {
					});
			for (String packages : gluepackages) {
				if (!packages.contains("none")) {
					arguments.add("--glue");
					arguments.add(packages);
				}
			}

			final String[] argv = arguments.toArray(new String[0]);

			executeUITests(argv);
		}
	}

	public static void run_ui_features_in_parallel() throws IOException, InterruptedException {

		List<String> features = getfilelist(PropertyLoader.provider.getProperty("featurefilepath", String.class),
				"feature");
		for (String feature : features) {
			System.out.println("Current Feature: " + feature);
			List<String> arguments = new ArrayList<String>();
			arguments.add(feature);
			arguments.add("--format");
			arguments.add("pretty");
			arguments.add("--format");
			arguments.add("json:./target/cucumber-reports/" + DateTime.now().toDateTimeISO().toString("hhmmssddMMyyyy")
					+ ".json");

			List<String> gluepackages = PropertyLoader.provider.getProperty("gluedpackages",
					new GenericType<List<String>>() {
					});
			for (String packages : gluepackages) {
				if (!packages.contains("none")) {
					arguments.add("--glue");
					arguments.add(packages);
				}
			}
			System.out.println("Arguments sent:" + arguments);
			final String[] argv = arguments.toArray(new String[0]);
			executeUITests(argv);
		}
	}

	public static void executeUITests(final String[] argv) throws InterruptedException {
		// TODO need to find an alternate runner which has better error handline
		BiFunction<String[], Boolean, Supplier<Byte>> executeUITests = (args, tagOnly) -> {
			try {
				cucumber.api.cli.Main.run(args, RunCukesTest.class.getClassLoader());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("Exception occured: " + e.getMessage());
				return () -> 0x1;
			}
			return () -> 0x0;
		};

		executeTests(argv, false, executeUITests);
	}

	public static void executeApiTests(final String featureFile, final Boolean isTags)
			throws InterruptedException, IOException {

		log.info("Executing API tests...");
		BiFunction<String[], Boolean, Supplier<Byte>> executeAPITests = (args, tagOnly) -> {
			File file = new File(args[0]);

			KarateFeature kf = new KarateFeature(file);
			if (tagOnly)
				filterOnTags(kf.getFeature());
			if (!kf.getFeature().getFeatureElements().isEmpty()) {
				KarateJunitAndJsonReporter reporter = null;
				try {
					reporter = new KarateJunitAndJsonReporter(file.getPath(),
							"./target/cucumber-reports/api/" + file.getName() + ".json");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("Exception occured: " + e.getMessage());
					// e.printStackTrace();
				}
				KarateRuntime runtime = kf.getRuntime(reporter);
				kf.getFeature().run(reporter, reporter, runtime);
				reporter.done();
				return () -> runtime.exitStatus();
			}
			return () -> 0x0;
		};
		executeTests(new String[] { featureFile }, isTags, executeAPITests);
	}

	public static void executeTests(final String[] args, final Boolean isTags,
			BiFunction<String[], Boolean, Supplier<Byte>> executionFunction) {
		CompletableFuture<Supplier<Byte>> future = CompletableFuture
				.supplyAsync(() -> executionFunction.apply(args, isTags), featureRunner);
		featureStatus.add(future);

	}

	private static void filterOnTags(CucumberFeature feature) {
		final List<CucumberTagStatement> featureElements = feature.getFeatureElements();
		List<String> tags = PropertyLoader.provider.getProperty("tagstorun", new GenericType<List<String>>() {
		});

		System.err.println("Filtering tags: " + tags.toString());
		for (Iterator<CucumberTagStatement> iterator = featureElements.iterator(); iterator.hasNext();) {
			CucumberTagStatement cucumberTagStatement = iterator.next();
			final boolean isFiltered = cucumberTagStatement.getGherkinModel().getTags().stream()
					.anyMatch(t -> tags.contains(t.getName()))
					|| feature.getGherkinFeature().getTags().stream().anyMatch(t -> tags.contains(t.getName()));
			if (!isFiltered) {
				System.err.println("skipping feature element " + cucumberTagStatement.getVisualName() + " of feature "
						+ feature.getPath() + " At line: " + cucumberTagStatement.getGherkinModel().getLine());
				iterator.remove();
			}
		}
	}

	public static List<String> getfilelist(String pathname, String type) throws IOException {
		return FileUtils
				.listFilesAndDirs(new File(pathname).getAbsoluteFile(), TrueFileFilter.INSTANCE,
						DirectoryFileFilter.DIRECTORY)
				.stream().filter(file -> file.getName().endsWith(type)).map(f -> f.getPath().replace("\\", "/"))
				.collect(Collectors.toList());
	}

}
