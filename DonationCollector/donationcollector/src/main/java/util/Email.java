package util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import entity.FirebaseUser;

public class Email {

	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;

	public void sendNotificationEmail(String itemName, String posterId, String pickUpTime, String ngoName)
			throws Exception {

		try {
			Firebase firebaseConn = new Firebase();
			FirebaseUser poster = firebaseConn.getFirebaseUserInfo(posterId);

			// Step1
			System.out.println("\n 1st ===> setup Mail Server Properties..");
			mailServerProperties = System.getProperties();
			mailServerProperties.put("mail.smtp.port", "587");
			mailServerProperties.put("mail.smtp.auth", "true");
			mailServerProperties.put("mail.smtp.starttls.enable", "true");
			System.out.println("Mail Server Properties have been setup successfully..");

			// Step2
			System.out.println("\n\n 2nd ===> get Mail Session..");
			getMailSession = Session.getDefaultInstance(mailServerProperties, null);
			generateMailMessage = new MimeMessage(getMailSession);
			System.out.println("email is: " + poster.getEmailAddress());
			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(poster.getEmailAddress()));
			generateMailMessage.setSubject("Your item " + itemName + " will be collected on " + pickUpTime);
			StringBuilder emailBodyBuilder = new StringBuilder();
			emailBodyBuilder.append("Hello " + poster.getFirstName() + ",");
			emailBodyBuilder.append("<br>" + ngoName + " will be picking up your item - ");
			emailBodyBuilder.append(itemName + " on " + pickUpTime + ". ");
			emailBodyBuilder.append("Please leave the item outside your door the night before.");
			emailBodyBuilder.append("<br><br> Regards, <br> Donation Collector Admin");

			generateMailMessage.setContent(emailBodyBuilder.toString(), "text/html");
			System.out.println("Mail Session has been created successfully..");

			// Step3
			System.out.println("\n\n 3rd ===> Get Session and Send mail");
			Transport transport = getMailSession.getTransport("smtp");

			// Enter your correct gmail UserID and Password
			// if you have 2FA enabled then provide App Specific Password
			transport.connect("smtp.gmail.com", "laioffer.donation.collector@gmail.com", EmailUtil.PASSWORD);
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();
			System.out.print("Email sent successfully");
		} catch (Exception e) {
			System.out.println("Exception in FirebaseClientGet:- " + e);
			throw e;
		}
	}
}