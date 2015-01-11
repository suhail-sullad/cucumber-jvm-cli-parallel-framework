package plugins.reporting;

import init.RunCukesTest;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.masterthought.cucumber.ReportBuilder;

public class ReportGenerator {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Properties reportproperties = new Properties();
		FileInputStream reportinputstream = new FileInputStream(new File(
				"./report.properties").getAbsoluteFile());
		reportproperties.load(reportinputstream);

		String[] reportfiles = RunCukesTest.getfilelist(
				"./target/cucumber-reports", "json");
		List<String> jsonReports = new ArrayList<String>();
		for (String filename : reportfiles) {
			jsonReports.add(filename);
		}
		ReportBuilder rp = new ReportBuilder(jsonReports, new File(
				reportproperties.getProperty("outputdirectorypath")), "",
				reportproperties.getProperty("buildnumber"),
				reportproperties.getProperty("buildname"), true, true, true,
				false, false, "", false);
		rp.generateReports();

	}
}