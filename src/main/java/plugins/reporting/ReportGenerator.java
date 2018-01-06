package plugins.reporting;

import init.RunCukesTest;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.google.gson.stream.JsonReader;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

public class ReportGenerator {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Properties reportproperties = new Properties();
		FileInputStream reportinputstream = new FileInputStream(
				new File("./resources/report.properties").getAbsoluteFile());
		reportproperties.load(reportinputstream);
		List<String> reportfiles = RunCukesTest.getfilelist("./target/cucumber-reports", "json");
		Configuration cfg = new Configuration(new File(reportproperties.getProperty("outputdirectorypath")),
				reportproperties.getProperty("buildname"));
		cfg.setBuildNumber(reportproperties.getProperty("buildnumber"));
		ReportBuilder rp = new ReportBuilder(reportfiles, cfg);
		rp.generateReports();

	}
}