package com.embian.botexample.echobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App 
{
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	private static Thread listener;
	
	public static void main( String[] args )
    {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                System.out.println("Shutdown hook ran!");
                shutdown();
            }
        });

        listener = new Thread(new EchoBotListener());
        listener.start();
        
        while(true) {
	        try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				shutdown();
				break;
			}
        }
    }
	
	private static void shutdown() {
        try {
        	listener.interrupt();
        	listener.join();
		} catch (InterruptedException e) {
			LOGGER.error("Interrupted Exception : {}", e);
		}
	}
}