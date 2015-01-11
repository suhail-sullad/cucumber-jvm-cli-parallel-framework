package plugins.emailer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class ReportMailer {

	public static void sendemail() throws EmailException, IOException {

		// Load properties from a file
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(new File(
				"./mailer.properties").getAbsoluteFile());
		prop.load(input);
		// Create the email message
		HtmlEmail email = new HtmlEmail();
		email.setHostName(prop.getProperty("MailServer"));
		if (Boolean.parseBoolean(prop.getProperty("Authentication")))
			email.setAuthentication(prop.getProperty("MailUsername"),
					prop.getProperty("MailPassword"));

		email.setSubject(prop.getProperty("MailSubject"));
		email.setFrom(prop.getProperty("MailFrom"));
		String[] SendToList = prop.getProperty("SendToList").split(",");
		for (String emailid : SendToList)
			email.addTo(emailid);
		String[] SendCCList = prop.getProperty("SendCCList").split(",");
		for (String emailid : SendCCList)
			email.addCc(emailid);

		// set the html message
		email.attach(new File(
				"./target/cucumber-html-reports/feature-overview.html")
				.getAbsoluteFile());

		// set the alternative message
		email.setTextMsg("Your email client does not support HTML messages");

		// send the email
		email.send();
	}

}