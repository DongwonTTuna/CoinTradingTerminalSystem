package net.dongwontuna.TradeSystem;

import com.pengrad.telegrambot.TelegramBot;

public class Telegram {
    TelegramBot bot = new TelegramBot(SqlManager.getAPI("TELEGRAM").get("APIKEY"));
}
