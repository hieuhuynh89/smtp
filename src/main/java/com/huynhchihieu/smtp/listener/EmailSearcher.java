package com.huynhchihieu.smtp.listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

public class EmailSearcher {
	/**
	 * Searches for e-mail messages containing the specified keyword in Subject
	 * field.
	 * 
	 * @param host
	 * @param port
	 * @param userName
	 * @param password
	 * @param keyword
	 */
	public void searchEmail(String host, String port, String userName, String password, final String keyword) {
		// https://www.tutorialspoint.com/javamail_api/javamail_api_imap_servers.htm
		Properties properties = new Properties();

		// server setting
		properties.put("mail.imap.host", host);
		properties.put("mail.imap.port", port);

		// SSL setting
		properties.put("mail.imap.timeout", 5000);
		// add trust for the certificate of this host. if no will throw authenticate error messages
		properties.put("mail.imap.ssl.trust", "testmail.com");
		
		properties.put("mail.imap.starttls.enable", true);
		properties.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		
//		If set to true, failure to create a socket using the specified socket factory class 
//		will cause the socket to be created using the java.net.Socket class. Defaults to true.
		// => set to true in case email_server support both non and ssl/tls on the same port => tls/ssl is optional
		properties.put("mail.imap.socketFactory.fallback", true);
		properties.put("mail.imap.socketFactory.port", port);

		Session session = Session.getDefaultInstance(properties);

		try {
			// connects to the message store
			Store store = session.getStore("imap");
			store.connect(userName, password);

			// opens the inbox folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);

			// creates a search criterion
			SearchTerm searchCondition = new SearchTerm() {
				@Override
				public boolean match(Message message) {
					try {
//						if (message.getSubject().contains(keyword)) {
//							return true;
//						}
						SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
						Date date = sdformat.parse("2023-05-22");
						if (message.getSentDate().compareTo(date) > 0) {
							return true;
						}
					} catch (MessagingException | ParseException ex) {
						ex.printStackTrace();
					}
					return false;
				}
			};

			// performs search through the folder
			Message[] foundMessages = folderInbox.search(searchCondition);

			for (int i = 0; i < foundMessages.length; i++) {
				Message message = foundMessages[i];
				String subject = message.getSubject();
				System.out.println("Found message #" + i + ": " + subject);
			}

			// disconnect
			folderInbox.close(false);
			store.close();
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider.");
			ex.printStackTrace();
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store.");
			ex.printStackTrace();
		}
	}

	/**
	 * Test this program with a Gmail's account
	 */
	public static void main(String[] args) {
		String host = "testmail.com";
		String port = "143";
		String userName = "user2@testmail.com";
		String password = "user2";
		EmailSearcher searcher = new EmailSearcher();
		String keyword = "JavaMail";
		searcher.searchEmail(host, port, userName, password, keyword);
	}
}
