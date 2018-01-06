package init;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.cfg4j.provider.GenericType;
import org.joda.time.DateTime;

import com.intuit.karate.cucumber.CucumberRunner;
import com.intuit.karate.cucumber.KarateFeature;
import com.intuit.karate.cucumber.KarateJunitAndJsonReporter;
import com.intuit.karate.cucumber.KarateRuntime;
import com.jayway.jsonpath.internal.JsonFormatter;

public class RunCukesTest {
	private static int uiinvocationcount = 0;
	private static int apiinvocationcount = 0;
	private static int maxinvocationcount = 0;

	public static void main(String[] args) throws Exception {

		PropertyLoader.init();
		maxinvocationcount = PropertyLoader.provider.getProperty("invocationcount", Integer.class);
		List<String> parallelMode = PropertyLoader.provider.getProperty("parallel", new GenericType<List<String>>() {
		});
		for (String mode : parallelMode) {
			switch (mode) {
			case "uitagsparallel":
				run_ui_tags_in_parallel();
				break;
			case "uifeaturesparallel":
				run_ui_features_in_parallel();
				break;
			case "apifeaturessequential":
				run_api_features_sequential();
				break;
			case "apifeaturesparallel":
				run_api_features_parallel();
				break;
			case "uifeaturessequential":
				run_ui_sequentially();
				break;
			}
		}

	}

	private static void run_api_features_parallel() throws InterruptedException, IOException {
		List<String> features = getfilelist(PropertyLoader.provider.getProperty("apifeaturefilepath", String.class),
				"feature");
		for (String string : features) {
			executeApiITests(string);
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
				arguments.add("@" + runnabletags);
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
				arguments.add("@" + runnabletags);
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
		
		List<String> features =getfilelist(PropertyLoader.provider.getProperty("featurefilepath", String.class), "feature");
		for (String feature : features) {
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

			final String[] argv = arguments.toArray(new String[0]);
			executeUITests(argv);

		}

	}

	public static List<String> getfilelist(String pathname, String type) throws IOException {
		Collection<File> files = FileUtils.listFilesAndDirs(new File(pathname).getAbsoluteFile(),
				TrueFileFilter.INSTANCE, DirectoryFileFilter.DIRECTORY);
		List<String> filenames = new ArrayList<String>();
		for (File file : files) {
			if (file.getName().contains(type))
				filenames.add(file.getPath().replace("\\", "/"));
		}
		return filenames;
	}

	public static void executeUITests(final String[] argv) throws InterruptedException {

		while (uiinvocationcount > maxinvocationcount - 1)
			Thread.sleep(1000);

		ExecutorService es = Executors.newSingleThreadExecutor();
		Thread.sleep(2000);
		System.out.println(uiinvocationcount);
		es.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				uiinvocationcount++;
				try {
					cucumber.api.cli.Main.run(argv, RunCukesTest.class.getClassLoader());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					uiinvocationcount--;
				}
			}

		});

		es.shutdown();

	}

	public static void executeApiITests(final String featureFile) throws InterruptedException {

		while (apiinvocationcount > maxinvocationcount - 1)
			Thread.sleep(1000);

		ExecutorService es = Executors.newSingleThreadExecutor();
		Thread.sleep(2000);
		System.out.println("Running API Thread:"+apiinvocationcount);
		es.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				apiinvocationcount++;
				try {
					File file = new File(featureFile);
					KarateFeature kf = new KarateFeature(file);
					KarateJunitAndJsonReporter reporter = new KarateJunitAndJsonReporter(file.getPath(),
							"./target/cucumber-reports/api/" + file.getName() + ".json");
					KarateRuntime runtime = kf.getRuntime(reporter);
					kf.getFeature().run(reporter, reporter, runtime);
					reporter.done();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					apiinvocationcount--;
				}
			}

		});

		es.shutdown();

	}

}