package emailer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class ReportMailer {

	public static void sendemail() throws EmailException, IOException {
		
		// Load properties from a file
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream("./mailer.properties");
		prop.load(input);
		// Create the email message
		HtmlEmail email = new HtmlEmail();
		email.setHostName(prop.getProperty("MailServer"));
		if(Boolean.parseBoolean(prop.getProperty("Authentication")))
		email.setAuthentication(prop.getProperty("MailUsername"), prop.getProperty("MailPassword"));
		
		email.setSubject(prop.getProperty("MailSubject"));
		email.setFrom(prop.getProperty("MailFrom"));
		String[]SendToList = prop.getProperty("SendToList").split(",");
		for (String emailid : SendToList)
			email.addTo(emailid);
		String[]SendCCList = prop.getProperty("SendCCList").split(",");
		for (String emailid : SendCCList)
			email.addCc(emailid);
		
		// set the html message
		email.attach(new File(
				"./target/cucumber-html-reports/feature-overview.html"));
		
		// set the alternative message
		email.setTextMsg("Your email client does not support HTML messages");

		// send the email
		email.send();
	}

}