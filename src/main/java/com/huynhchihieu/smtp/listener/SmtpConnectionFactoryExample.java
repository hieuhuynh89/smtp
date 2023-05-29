package com.huynhchihieu.smtp.listener;

import java.util.Arrays;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import org.nlab.smtp.pool.SmtpConnectionPool;
import org.nlab.smtp.transport.connection.ClosableSmtpConnection;
import org.nlab.smtp.transport.factory.SmtpConnectionFactories;
import org.nlab.smtp.transport.factory.SmtpConnectionFactory;
import org.nlab.smtp.transport.factory.SmtpConnectionFactoryBuilder;

public class SmtpConnectionFactoryExample {
	public static void main(String[] args) throws Exception {
		Properties properties = new Properties();

		// server setting
		properties.put("mail.imap.host", "testmail.com");
		properties.put("mail.imap.port", 143);

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
		properties.put("mail.imap.socketFactory.port", 143);

//		Session session = Session.getDefaultInstance(properties);
		
		
//		SmtpConnectionFactory factory = SmtpConnectionFactories.newSmtpFactory(session);
		
		
		SmtpConnectionFactory factory = SmtpConnectionFactoryBuilder.newSmtpBuilder()
                .session(properties)
                .username("user2@testmail.com")
                .password("user2").build();
		
		SmtpConnectionPool smtpConnectionPool = new SmtpConnectionPool(factory);
		Session session = smtpConnectionPool.getSession();
		
		try {
			// connects to the message store
			Store store = session.getStore("imap");
			store.connect("user2@testmail.com", "user2");
			// opens the inbox folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);
			
			// Fetch unseen messages from inbox folder
		    Message[] messages = folderInbox.search(
		        new FlagTerm(new Flags(Flags.Flag.SEEN), false));

		    // Sort messages from recent to oldest
		    Arrays.sort( messages, ( m1, m2 ) -> {
		      try {
		        return m2.getSentDate().compareTo( m1.getSentDate() );
		      } catch ( MessagingException e ) {
		        throw new RuntimeException( e );
		      }
		    } );

		    for ( Message message : messages ) {
		      System.out.println( 
		          "sendDate: " + message.getSentDate()
		          + " subject:" + message.getSubject() );
		    }
		    
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider for protocol: IMAP");
			ex.printStackTrace();
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		}
		
		smtpConnectionPool.close();
		
	}
}
