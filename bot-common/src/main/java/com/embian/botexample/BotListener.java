package com.embian.botexample;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

public abstract class BotListener implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(BotListener.class);
	
    private static final int UPDATE_MESSAGE_LIMIT = 10;
    private static final int LONG_POLLING_TIMEOUT = 0; // in seconds

	protected TelegramBot bot;

	public BotListener(String botToken) {
		bot = TelegramBotAdapter.build(botToken);
	}

	@Override
	public void run() {
		int offset = 0;
        int maxUpdateId = 0;
        boolean alive = true;
        
		List<Update> updates;
        while(true) {
			if (!alive)
				break;

			try {
				GetUpdatesResponse updatesResponse = bot.getUpdates(offset, UPDATE_MESSAGE_LIMIT, LONG_POLLING_TIMEOUT);
				updates = updatesResponse.updates();
			} catch(Exception e) {
				LOGGER.error("Telegram getUpdates failed. we'll retry.");
				continue;
			}
	        
	        if (updates == null || updates.isEmpty()) {
	        	try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					alive = false;
				}
	        	continue;
	        }
	        
	        for(Update update : updates) {
	        	maxUpdateId = maxUpdateId >= update.updateId() ? maxUpdateId : update.updateId();
	        	
	        	InlineQuery inlineQuery = update.inlineQuery();
	        	Message message = update.message();
	        	
	        	LOGGER.debug("inlineQuery : {}", inlineQuery);
	        	LOGGER.debug("message : {}", message);
	        	
	        	if (inlineQuery != null) { // inline bot mode
	        		inlineQuery(inlineQuery);
	        	} else if (message != null) { // channel bot mode
	        		message(message);
	        	}
	        }
	        
	        offset = maxUpdateId + 1;
        }
	}

	abstract protected void inlineQuery(InlineQuery inlineQuery);
	abstract protected void message(Message message);
}
