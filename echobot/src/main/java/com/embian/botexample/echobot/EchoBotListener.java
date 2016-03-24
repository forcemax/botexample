package com.embian.botexample.echobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Chat.Type;
import com.embian.botexample.BotListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.InlineQuery;

public class EchoBotListener extends BotListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(EchoBotListener.class);
	
	private static final String BOT_TOKEN = "<BOTTOKEN>";

	public EchoBotListener() {
		super(BOT_TOKEN);
	}
	
	@Override
	protected void inlineQuery(InlineQuery inlineQuery) {
		if (inlineQuery == null)
			return;
	}
	
	@Override
	protected void message(Message message) {
		if (message == null)
			return;
		
    	Chat chat = message.chat();
    	String text;
    	
    	if (chat.type() == Type.Private) { // Private
    		if (message.text() != null && message.text().equals("/start")) {
        		bot.sendMessage(chat.id(), "Thanks for using bot. Type(Text) a message.");
        		return;
    		}
    	}
    	
    	if (chat.type() == Type.group || chat.type() == Type.supergroup) { // Group && Supergroup
    		if (message.newChatParticipant() != null && message.newChatParticipant().username().equals("isaybot")) { 
        		bot.sendMessage(chat.id(), "Thanks for invite bot. Type(Text) a message.");
        		return;
    		}
    	}
    	
    	text = message.text();
		if (text != null) {
    		try {
       			bot.sendMessage(chat.id(), text);
    		} catch(Exception e) {
    			LOGGER.error("Exception on sendMessage : {}", e);
    		}
    	}
	}
}
