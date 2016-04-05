# Telegram bot example  
[![Build Status](https://travis-ci.org/forcemax/botexample.svg?branch=master)](https://travis-ci.org/forcemax/botexample) [![Circle CI](https://circleci.com/gh/forcemax/botexample.svg?style=svg)](https://circleci.com/gh/forcemax/botexample) [![Download](https://api.bintray.com/packages/forcemax/maven/botexample/images/download.svg) ](https://bintray.com/forcemax/maven/botexample/_latestVersion)


Telegram bot example using [Simple Java API for Telegram bot](https://github.com/pengrad/java-telegram-bot-api)

## Create a new bot and **token**
1. Create a new bot. [link](https://core.telegram.org/bots#botfather)
2. BotFather gave **token**. seems like `110201543:AAHdqTcvCH1vGWJxfSeofSAs0K5PALDsaw`
3. Replace `<BOTTOKEN>` to **token** in source code (i.e: echobot/src/main/java/com/embian/botexample/echobot/EchoBotListener.java Line 15)
   `private static final String BOT_TOKEN = "<BOTTOKEN>";`

![alt tag](https://github.com/forcemax/botexample/blob/master/doc/newbot.png)

## EchoBot
EchoBot echo message.

![alt tag](https://github.com/forcemax/botexample/blob/master/doc/echobot.png)

## 