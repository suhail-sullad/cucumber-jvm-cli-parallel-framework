package uitests;

import init.DriverManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import plugins.logging.WebDriverListner;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import plugins.screenrecording.RecordScreen;

public class ScreenRecorderHook {
	private RecordScreen record;
	protected final EventFiringWebDriver fd;
	private String videoreporting;
	private WebDriverListner listner;
	private String loggingstatus;

	public ScreenRecorderHook(DriverManager webdriver) throws IOException {
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(new File(
				"./tests.properties").getAbsoluteFile());
		prop.load(input);

		this.fd = webdriver;
		this.videoreporting = prop.getProperty("videoreporting");
		this.loggingstatus = prop.getProperty("logging");

	}
/*
	@Before
	public void preparevideo(Scenario scenario) throws Exception {

		if (videoreporting.equalsIgnoreCase("ENABLED")) {
			record = new RecordScreen("testrecording");
			record.beginrecord();
			Thread.sleep(1000);
		}

		if (loggingstatus.equalsIgnoreCase("ENABLED")) {
			listner = new WebDriverListner(scenario.getName());
			fd.register(listner);
		}
		ScenarioContext.flushscenariodata();
		ScenarioContext.setScenarioname(scenario.getId());

	}

	@After
	public void close(Scenario scenario) throws IOException,
			InterruptedException {
		scenario.embed(this.fd.getScreenshotAs(OutputType.BYTES), "image/png");
		if (videoreporting.equalsIgnoreCase("ENABLED"))
			scenario.write("<br/><a href=\"" + record.stoprecording()
					+ "\">Click here to navigate to video</a>");
		this.fd.quit();
		if (loggingstatus.equalsIgnoreCase("ENABLED")) {
			listner.write("Browser closed.");
			listner.writescenariosttus(scenario.getName(), scenario.getStatus());
			listner.quitlogger();
			scenario.write("<br/><a href=\"../logs/" + scenario.getName()
					+ ".html\">Click for log information.</a>");
		}

	}*/

}