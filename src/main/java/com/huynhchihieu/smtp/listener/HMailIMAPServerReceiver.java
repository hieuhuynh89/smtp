package com.huynhchihieu.smtp.listener;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;

public class HMailIMAPServerReceiver {
	public static void main(String[] args) throws InterruptedException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException, UnrecoverableKeyException {
		final String host = "testmail.com";
		final String port = "143";
		final String username = "user2@testmail.com";
		final String password = "user2";
		
		Properties properties = new Properties();
		// server setting
		properties.put("mail.imap.host", host);
		properties.put("mail.imap.port", port);
		// SSL setting
		properties.put("mail.imap.timeout", 5000);
		// add trust certificate for this host
		properties.put("mail.imap.ssl.trust", "testmail.com");
		// only add this property if mail_server require ssl/tls for connection security.
//		properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
		properties.put("mail.imap.socketFactory.port", port);
//		If set to true, failure to create a socket using the specified socket factory class 
//		will cause the socket to be created using the java.net.Socket class. Defaults to true.
		// => set to true in case email_server support both non and ssl/tls on the same port => tls/ssl is optional
		properties.put("mail.imap.socketFactory.fallback", false);
		/**
		 * If true, enables the use of the STARTTLS command (if supported by the server) 
		 * to switch the connection to a TLS-protected connection before issuing any login commands. Default is false.
		 */
		properties.put("mail.imap.starttls.enable", "true");
		
		Session session = Session.getDefaultInstance(properties);
		
		try {
			// connects to the message store
			Store store = session.getStore("imap");
			store.connect(username, password);

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
	}
	
	
}
