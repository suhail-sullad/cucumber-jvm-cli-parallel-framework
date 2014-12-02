package logging;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class WebDriverListner extends AbstractWebDriverEventListener {
	private static String filename;
	private static Logger testlogger;
	private static FileAppender fileAppender;
	private static DateTime dt;
	
	public WebDriverListner(String scenarioname) throws IOException {
		this.filename = "./target/logs/"+scenarioname+".html";
		BasicConfigurator.configure();
		testlogger = Logger.getLogger(WebDriverListner.class.getName());
		testlogger.setLevel(Level.INFO);
		fileAppender = new RollingFileAppender(new CustomHTMLLayout(),filename);
		testlogger.addAppender(fileAppender);
		testlogger.info("\n<strong>Running Scenario : </strong>\n"+scenarioname);
		
		
	}

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
	//	TODO Auto-generated method stub
	testlogger.info("\n<b>Navigating to URL :</b><a href=\"" + url+"\">"+url+"</a>");
	dt=DateTime.now();
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub
		testlogger.info("\n<b>Successfully navigated to URL :</b><a href=\"" + url+"\">"+url+"</a>");
		testlogger.info("\n<b>Page Title :</b>" + driver.getTitle());
		testlogger.info("\n<b>Load Time :</b>" + new Period(dt,DateTime.now()).getMillis()+"ms");
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub
		testlogger.info("\n<b>Navigating back from current URL :</b>"
				+ driver.getCurrentUrl());
		testlogger.info("\n<b>Page Title :</b>" + driver.getTitle());
	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub
		testlogger.info("\n<b>Successfully navigated back to URL :</b>"
				+ driver.getCurrentUrl());
		testlogger.info("\n<b>Page Title :</b>" + driver.getTitle());
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub
		testlogger.info("\n<b>Navigating forward from current URL :</b>"
				+ driver.getCurrentUrl());
		testlogger.info("\n<b>Page Title :</b>" + driver.getTitle());
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub
		testlogger.info("\n<b>Successfully navigated forward to URL :</b>"
				+ driver.getCurrentUrl());
		testlogger.info("\n<b>Page Title :</b>" + driver.getTitle());
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		testlogger.info("\n<div><b>Finding Element:</b> <font color=\"#0000FF\">"+by.toString()+"</font></div> <b>On page :</b> " + driver.getTitle());
	}

	@Override 
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
		testlogger.info("\n<div><b>Found Element:</b> <font color=\"#006600\">"+by.toString()+"</font></div> <b>On page : </b>" + driver.getTitle());
	}

/*	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	
		//testlogger.info("\n<div><b>Performing click on Element:</b> <font color=\"#0000FF\">"+getcomplexelement(element)+"</font></div> </b>On page : <b>" + driver.getTitle());
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		//testlogger.info("\n<b>Successfull click  Element:</b> <font color=\"#006600\">"+getcomplexelement(element)+"</font></div> </b>On page : </b>" + driver.getTitle());
	}*/

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		super.beforeChangeValueOf(element, driver);
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		super.afterChangeValueOf(element, driver);
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub
		super.beforeScript(script, driver);
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub
		super.afterScript(script, driver);
	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		// TODO Auto-generated method stub
		testlogger.error("\nError Occured while performing some operation:",
				throwable);
	}
	@SuppressWarnings("deprecation")
	public void quitlogger(){
		fileAppender.close();
		Logger.shutdown();
		
	}

	public void write(String message) {
		// TODO Auto-generated method stub
		testlogger.warn("<b>"+message+"</b>");
	}

	public void writescenariosttus(String name, String status) {
		if(status.equalsIgnoreCase("failed"))
		testlogger.info("<b>Scenario :</b>"+name +".<b> Status :</b><b> <font color=\"#FF0000\">"+status.toUpperCase()+"</font></b>");
		else if(status.equalsIgnoreCase("passed"))
			testlogger.info("<b>Scenario :</b>"+name +".<b> Status :</b> <b><font color=\"#006600\">"+status.toUpperCase()+"</font></b>");
	}
/*	public String getcomplexelement(WebElement element){
		
		Pattern p= Pattern.compile("->.*]");
		Matcher elementinfo = p.matcher(element.toString());
		StringBuffer sb =new StringBuffer();
		while(elementinfo.find())
			sb.append(elementinfo.group());
		return sb.toString();
		}*/
	

}