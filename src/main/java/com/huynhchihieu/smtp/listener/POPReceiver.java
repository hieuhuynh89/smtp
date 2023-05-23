package com.huynhchihieu.smtp.listener;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import com.sun.mail.pop3.POP3Store;

public class POPReceiver {
	public void listen() throws Exception {
		Properties properties = new Properties();
		Session session = null;
		POP3Store pop3Store = null;
		String host = "pop.gmail.com";
		String user = "huynhchihieu75@gmail.com";
		properties.put("mail.pop3.host", host);
		properties.put("mail.pop3.port", "995");
		session = Session.getDefaultInstance(properties);
		pop3Store = (POP3Store) session.getStore("pop3");
		pop3Store.connect(user, "fxyapjhuahjwsjsj");
		Folder folder = pop3Store.getFolder("INBOX");
		folder.addMessageCountListener(new EmailListener());
//		sendEmail();
	}

//	public void sendEmail() {
//		// not added code, but the email sends
//	}

	public static void main(String[] args) throws Exception {
		POPReceiver i = new POPReceiver();
		i.listen();
	}
}
