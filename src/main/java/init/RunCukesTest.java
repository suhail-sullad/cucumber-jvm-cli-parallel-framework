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
import org.joda.time.DateTime;

public class RunCukesTest {
	private static int invocationcount = 0;
	private static int maxinvocationcount = 0;

	public static void main(String[] args) throws Exception {

		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(new File(
				"./tests.properties").getAbsoluteFile());
		prop.load(input);
		maxinvocationcount = Integer.parseInt(prop
				.getProperty("invocationcount"));

		switch (prop.getProperty("parallel")) {
		case "tags":
			run_tags_in_parallel();
			break;
		case "features":
			run_features_in_parallel();
			break;
		default:
			run_sequentially();
			break;
		}

	}

	private static void run_sequentially() throws IOException,
			InterruptedException {
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(new File(
				"./tests.properties").getAbsoluteFile());
		prop.load(input);

		List<String> arguments = new ArrayList<String>();
		String[] features = getfilelist(prop.getProperty("featurefilepath"),
				"feature");
		for (String feature : features)
			arguments.add(feature);

		arguments.add("--format");
		arguments.add("pretty");
		arguments.add("--format");
		arguments.add("json:./target/cucumber-reports/"
				+ DateTime.now().toDateTimeISO().toString("hhmmssddMMyyyy")
				+ ".json");
		String[] tags = prop.getProperty("tagstorun").split(",");

		for (String runnabletags : tags) {
			if (!runnabletags.contains("none")) {
				arguments.add("--tags");
				arguments.add("@" + runnabletags);
			}
		}

		String[] gluepackages = prop.getProperty("gluedpackages").split(",");
		for (String packages : gluepackages) {
			if (!packages.contains("none")) {
				arguments.add("--glue");
				arguments.add(packages);
			}
		}

		final String[] argv = arguments.toArray(new String[0]);
		executetests(argv);

	}

	public static void run_tags_in_parallel() throws IOException,
			InterruptedException {
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(new File(
				"./tests.properties").getAbsoluteFile());
		prop.load(input);

		String[] tags = prop.getProperty("tagstorun").split(",");
		for (String runnabletags : tags) {
			List<String> arguments = new ArrayList<String>();
			String[] features = getfilelist(
					prop.getProperty("featurefilepath"), "feature");
			for (String feature : features)
				arguments.add(feature);

			arguments.add("--format");
			arguments.add("pretty");
			arguments.add("--format");
			arguments.add("json:./target/cucumber-reports/"
					+ DateTime.now().toDateTimeISO().toString("hhmmssddMMyyyy")
					+ ".json");
			if (!runnabletags.contains("none")) {
				arguments.add("--tags");
				arguments.add("@" + runnabletags);
			}

			String[] gluepackages = prop.getProperty("gluedpackages")
					.split(",");
			for (String packages : gluepackages) {
				if (!packages.contains("none")) {
					arguments.add("--glue");
					arguments.add(packages);
				}
			}

			final String[] argv = arguments.toArray(new String[0]);

			executetests(argv);
		}
	}

	public static void run_features_in_parallel() throws IOException,
			InterruptedException {
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(new File(
				"./tests.properties").getAbsoluteFile());
		prop.load(input);
		String[] features = getfilelist(prop.getProperty("featurefilepath"),
				"feature");
		for (String feature : features) {
			List<String> arguments = new ArrayList<String>();
			arguments.add(feature);
			arguments.add("--format");
			arguments.add("pretty");
			arguments.add("--format");
			arguments.add("json:./target/cucumber-reports/"
					+ DateTime.now().toDateTimeISO().toString("hhmmssddMMyyyy")
					+ ".json");

			String[] gluepackages = prop.getProperty("gluedpackages")
					.split(",");
			for (String packages : gluepackages) {
				if (!packages.contains("none")) {
					arguments.add("--glue");
					arguments.add(packages);
				}
			}

			final String[] argv = arguments.toArray(new String[0]);
			executetests(argv);

		}

	}

	public static String[] getfilelist(String pathname, String type)
			throws IOException {
		Collection<File> files = FileUtils.listFilesAndDirs(
				new File(pathname).getAbsoluteFile(), TrueFileFilter.INSTANCE,
				DirectoryFileFilter.DIRECTORY);
		List<String> filenames = new ArrayList<String>();
		for (File file : files) {
			if (file.getName().contains(type))
				filenames.add(file.getPath().replace("\\", "/"));
		}
		return filenames.toArray(new String[0]);
	}

	public static void executetests(final String[] argv)
			throws InterruptedException {

		while (invocationcount > maxinvocationcount - 1)
			Thread.sleep(1000);

		ExecutorService es = Executors.newSingleThreadExecutor();
		Thread.sleep(2000);
		System.out.println(invocationcount);
		es.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				invocationcount++;
				try {
					cucumber.api.cli.Main.run(argv,
							RunCukesTest.class.getClassLoader());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					invocationcount--;
				}
			}

		});

		es.shutdown();

	}
}