package init;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class DriverManager extends EventFiringWebDriver {
	public static int invocationcount = 0;

	public DriverManager() {
		super(getdriver());
	}

	public static WebDriver getdriver() {
		// TODO Auto-generated method stub

		invocationcount++;
		try {
			DesiredCapabilities capabilities = null;
			// To use cucumber-eclipse plugin set the user directory path to
			// your project folder

			// prop.getProperty("browsername")
			switch (Browsers.valueOf(PropertyLoader.provider.getProperty("browsername", String.class))) {
			case CHROME:
				ChromeDriverService cds = new ChromeDriverService.Builder().usingDriverExecutable(
						new File(PropertyLoader.provider.getProperty("chromebrowserdriverpath", String.class))
								.getAbsoluteFile())
						.usingAnyFreePort().build();

				return new ChromeDriver(cds);

			case FIREFOX:
				return new FirefoxDriver();

			case INTERNET_EXPLORER:
				InternetExplorerDriverService ids = new InternetExplorerDriverService.Builder().usingDriverExecutable(
						new File(PropertyLoader.provider.getProperty("iebrowserdriverpath", String.class))
								.getAbsoluteFile())
						.usingAnyFreePort().build();
				return new InternetExplorerDriver(ids);

			/*
			 * case "htmlunit": return new HtmlUnitDriver();
			 * 
			 * case "phantomjs": PhantomJSDriverService pds = new
			 * PhantomJSDriverService.Builder() .usingPhantomJSExecutable( new
			 * File(prop.getProperty("phantomdriverpath"))
			 * .getAbsoluteFile()).usingAnyFreePort() .build(); return new
			 * PhantomJSDriver(pds, DesiredCapabilities.firefox());
			 * 
			 * case "remotechrome": capabilities = DesiredCapabilities.chrome();
			 * capabilities.setJavascriptEnabled(true); break;
			 * 
			 * case "remotefirefox": capabilities =
			 * DesiredCapabilities.firefox();
			 * capabilities.setJavascriptEnabled(true); break;
			 * 
			 * case "remoteie": capabilities =
			 * DesiredCapabilities.internetExplorer();
			 * capabilities.setJavascriptEnabled(true); break;
			 * 
			 * case "remotehtmlunit": capabilities =
			 * DesiredCapabilities.htmlUnit(); break;
			 * 
			 * case "remotehtmlunitjs": capabilities =
			 * DesiredCapabilities.htmlUnitWithJs(); break;
			 * 
			 * case "remotephantomjs": capabilities =
			 * DesiredCapabilities.phantomjs();
			 * capabilities.setJavascriptEnabled(true); break;
			 */ default:
				return new FirefoxDriver();

			}
			// return new RemoteWebDriver(new
			// URL(prop.getProperty("remotedriverurl")), capabilities);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
