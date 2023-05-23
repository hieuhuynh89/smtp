package com.huynhchihieu.smtp.listener;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.search.FlagTerm;

//https://www.codejava.net/java-ee/javamail/receive-e-mail-messages-from-a-pop3-imap-server
//https://www.javatpoint.com/example-of-receiving-email-using-java-mail-api

public class EmailReceiver {
	/**
	 * Returns a Properties object which is configured for a POP3/IMAP server
	 *
	 * @param protocol either "imap" or "pop3"
	 * @param host
	 * @param port
	 * @return a Properties object
	 */
	private Properties getServerProperties(String protocol, String host, String port) {
		Properties properties = new Properties();

		// server setting
		properties.put(String.format("mail.%s.host", protocol), host);
		properties.put(String.format("mail.%s.port", protocol), port);

		// SSL setting
		properties.setProperty(String.format("mail.%s.socketFactory.class", protocol),
				"javax.net.ssl.SSLSocketFactory");
		properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
		properties.setProperty(String.format("mail.%s.socketFactory.port", protocol), String.valueOf(port));

		return properties;
	}

	/**
	 * Downloads new messages and fetches details for each message.
	 * 
	 * @param protocol
	 * @param host
	 * @param port
	 * @param userName
	 * @param password
	 * @throws InterruptedException 
	 */
	public void downloadEmails(String protocol, String host, String port, String userName, String password) throws InterruptedException {
		Properties properties = getServerProperties(protocol, host, port);
		Session session = Session.getDefaultInstance(properties);

		try {
			// connects to the message store
			Store store = session.getStore(protocol);
			store.connect(userName, password);

			// opens the inbox folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);
			
			// Add messageCountListener to listen for new messages
			folderInbox.addMessageCountListener(new MessageCountAdapter(){
		         
		        public void messagesAdded(MessageCountEvent ev) {
		            System.out.println("message listner invoked.");
		            Message[] messages = ev.getMessages();
		            try {
		            	System.out.println("Got " + messages.length + " new messages");
						printDetailsMessages(messages);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            
		        }
		 
		        /* (non-Javadoc)
		         * @see javax.mail.event.MessageCountListener#messagesRemoved(javax.mail.event.MessageCountEvent)
		         */
		        public void messagesRemoved(MessageCountEvent arg0) {
		            // TODO Auto-generated method stub
		             
		        }
		    });
			
			// Check mail once in "freq" MILLIseconds
		    int freq = Integer.parseInt("5000");
		 
		    for (;;) {
		        System.out.println("Theread will sleep for 5 seconds");
		        Thread.sleep(freq); // sleep for freq milliseconds
		        System.out.println("Thread awake after 5 seconds");
		        System.out.println("message count in folder is "+folderInbox.getMessageCount());
		        System.out.println();
		        System.out.println();
		    }

			// fetches new messages from server
//			Message[] messages = folderInbox.getMessages();
			
			/* Get the messages which is unread in the Inbox */
//			
//			Message messages[] = folderInbox.search(new FlagTerm(new Flags(
//                    Flags.Flag.SEEN), false));
//            System.out.println("No. of Unread Messages : " + messages.length);
//            
//            printDetailsMessages(messages);

			

			// disconnect
//			folderInbox.close(false);
//			store.close();
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider for protocol: " + protocol);
			ex.printStackTrace();
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		}
	}

	private void printDetailsMessages(Message[] messages) throws MessagingException {
		for (int i = 0; i < messages.length; i++) {
			Message msg = messages[i];
			Address[] fromAddress = msg.getFrom();
			String from = fromAddress[0].toString();
			String subject = msg.getSubject();
			String toList = parseAddresses(msg.getRecipients(RecipientType.TO));
			String ccList = parseAddresses(msg.getRecipients(RecipientType.CC));
			String sentDate = msg.getSentDate().toString();

			String contentType = msg.getContentType();
			String messageContent = "";

			if (contentType.contains("text/plain") || contentType.contains("text/html")) {
				try {
					Object content = msg.getContent();
					if (content != null) {
						messageContent = content.toString();
					}
				} catch (Exception ex) {
					messageContent = "[Error downloading content]";
					ex.printStackTrace();
				}
			}

			// print out details of each message
			System.out.println("Message #" + (i + 1) + ":");
			System.out.println("\t From: " + from);
			System.out.println("\t To: " + toList);
			System.out.println("\t CC: " + ccList);
			System.out.println("\t Subject: " + subject);
			System.out.println("\t Sent Date: " + sentDate);
			System.out.println("\t Message: " + messageContent);
		}
	}

	/**
	 * Returns a list of addresses in String format separated by comma
	 *
	 * @param address an array of Address objects
	 * @return a string represents a list of addresses
	 */
	private String parseAddresses(Address[] address) {
		String listAddress = "";

		if (address != null) {
			for (int i = 0; i < address.length; i++) {
				listAddress += address[i].toString() + ", ";
			}
		}
		if (listAddress.length() > 1) {
			listAddress = listAddress.substring(0, listAddress.length() - 2);
		}

		return listAddress;
	}

	/**
	 * Test downloading e-mail messages
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// for POP3
//		 String protocol = "pop3s";
//		 String host = "pop.gmail.com";
//		 String port = "995";

		// for IMAP
		String protocol = "imap";
		String host = "imap.gmail.com";
		String port = "993";

		String userName = "huynhchihieu75@gmail.com";
		String password = "fxyapjhuahjwsjsj";

		EmailReceiver receiver = new EmailReceiver();
//		receiver.downloadEmails(protocol, host, port, userName, password);
		
		
		receiver.downloadEmails("imap", "testmail.com", "143", "user2", "user2");
	}
}
