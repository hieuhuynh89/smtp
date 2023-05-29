package com.huynhchihieu.smtp;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SSLEmail {
	
//	final static String from = "huynhchihieu75@gmail.com";
//	final static String to = "huynhchihieu75@gmail.com";
//	final static String password = "fxyapjhuahjwsjsj";
	
	final static String host = "testmail.com";
	final static String from = "user1@testmail.com";
	final static String fromPassword = "1234";
	final static int port = 465;
	
	final static String to = "user2@testmail.com";
	
	/**
	 * Outgoing Mail (SMTP) Server requires TLS or SSL: smtp.gmail.com (use
	 * authentication) Use Authentication: Yes Port for SSL: 465
	 */
	public static void main(String[] args) {
		
		
		System.out.println("SSLEmail Start");
		
		System.setProperty("javax.net.ssl.trustStore", "D:\\working\\java\\keystore\\hmailserver\\hmailserver.jks");
		
		
		System.out.println(System.getProperty("javax.net.ssl.trustStore"));
		
		Properties props = new Properties();
		props.put("mail.smtp.host", host); // SMTP Host
		props.put("mail.smtp.timeout", 5000);
//		props.put("mail.smtp.ssl.trust", "testmail.com");
//		props.put("mail.smtps.ssl.enable", true);
		props.put("mail.smtp.port", port); // SMTP Port
//		props.put("mail.smtp.socketFactory.port", port); // SSL Port
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
		props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication

		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, fromPassword);
			}
		};

		Session session = Session.getDefaultInstance(props, auth);
		System.out.println("Session created");

		sendAttachmentEmail(session, to, "SSLEmail Testing Subject with Attachment",
				"SSLEmail Testing Body with Attachment");

	}
	
	private static void sendAttachmentEmail(Session session, String toEmail, String subject, String body){
		try{
	         MimeMessage msg = new MimeMessage(session);
	         msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		     msg.addHeader("format", "flowed");
		     msg.addHeader("Content-Transfer-Encoding", "8bit");
		      
		     msg.setFrom(new InternetAddress(from, "NoReply-JD"));

		     msg.setReplyTo(InternetAddress.parse(to, false));

		     msg.setSubject(subject, "UTF-8");

		     msg.setSentDate(new Date());

		     msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
		      
	         // Create the message body part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Fill the message
	         messageBodyPart.setText(body);
	         
	         // Create a multipart message for attachment
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Second part is attachment
	         messageBodyPart = new MimeBodyPart();
	         String file = "D:\\working\\java\\github-repository\\smtp\\src\\main\\resources\\testattachment.txt";
	         String filename = "testattachment.txt";
	         DataSource source = new FileDataSource(file);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         msg.setContent(multipart);

	         // Send message
	         Transport.send(msg);
	         System.out.println("EMail Sent Successfully with attachment!!");
	      }catch (MessagingException e) {
	         e.printStackTrace();
	      } catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
		}
	}
}
