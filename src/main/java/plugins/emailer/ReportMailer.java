package plugins.emailer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.cfg4j.provider.GenericType;

import init.PropertyLoader;

public class ReportMailer {

	public static void sendemail() throws EmailException, IOException {

		// Create the email message
		HtmlEmail email = new HtmlEmail();
		email.setHostName(PropertyLoader.provider.getProperty("MailServer", String.class));
		if (PropertyLoader.provider.getProperty("Authentication", Boolean.class))
			email.setAuthentication(PropertyLoader.provider.getProperty("MailUsername", String.class),
					PropertyLoader.provider.getProperty("MailPassword", String.class));

		email.setSubject(PropertyLoader.provider.getProperty("MailSubject", String.class));
		email.setFrom(PropertyLoader.provider.getProperty("MailFrom", String.class));
		List<String> SendToList = PropertyLoader.provider.getProperty("SendToList", new GenericType<List<String>>() {
		});
		for (String emailid : SendToList)
			email.addTo(emailid);
		List<String> SendCCList = PropertyLoader.provider.getProperty("SendCCList", new GenericType<List<String>>() {
		});
		for (String emailid : SendCCList)
			email.addCc(emailid);

		// set the html message
		email.attach(new File("./target/cucumber-html-reports/cucumber-html-reports/overview-features.html")
				.getAbsoluteFile());

		// set the alternative message
		email.setTextMsg("Your email client does not support HTML messages");

		// send the email
		email.send();
	}

	public static void main(String[] args) throws EmailException, IOException {
		sendemail();
	}

}