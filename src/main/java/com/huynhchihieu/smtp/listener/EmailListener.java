package com.huynhchihieu.smtp.listener;

import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

public class EmailListener extends MessageCountAdapter {
	@Override
	public void messagesAdded(MessageCountEvent e) {
		System.out.println("I");
	}

	@Override
	public void messagesRemoved(MessageCountEvent e) {
		System.out.println("J");
	}

}
