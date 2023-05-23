package com.huynhchihieu.smtp;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SimpleEmail {
	public static void main(String[] args) throws AddressException, MessagingException, UnsupportedEncodingException {
		
		final String from = "huynhchihieu75@gmail.com";
		final String to = "huynhchihieu75@gmail.com";
		final String password = "fxyapjhuahjwsjsj";

		System.out.println("SimpleEmail Start");

		String smtpHostServer = "smtp.gmail.com";
		String emailID = "email_me@example.com";

		Properties props = System.getProperties();

		props.put("mail.smtp.host", smtpHostServer);

		Session session = Session.getInstance(props, null);

//		EmailUtil.sendEmail(session, emailID, "SimpleEmail Testing Subject", "SimpleEmail Testing Body");

		MimeMessage msg = new MimeMessage(session);
		// set message headers
		msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		msg.addHeader("format", "flowed");
		msg.addHeader("Content-Transfer-Encoding", "8bit");

		msg.setFrom(new InternetAddress(from, "NoReply-JD"));

		msg.setReplyTo(InternetAddress.parse(to, false));

		msg.setSubject("SimpleEmail Testing Subject", "UTF-8");

		msg.setText("SimpleEmail Testing Body", "UTF-8");

		msg.setSentDate(new Date());

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
		System.out.println("Message is ready");
		Transport.send(msg);

		System.out.println("EMail Sent Successfully!!");
		
//		Exception in thread "main" com.sun.mail.smtp.SMTPSendFailedException: 530 5.7.0 Must issue a STARTTLS command first
	
//		You are probably attempting to use Gmail's servers on port 25 to deliver mail to a third party over an unauthenticated connection. 
//		Gmail doesn't let you do this, because then anybody could use Gmail's servers to send mail to anybody else. 
//		This is called an open relay and was a common enabler of spam in the early days. 
//		Open relays are no longer acceptable on the Internet.
	
	}
}
