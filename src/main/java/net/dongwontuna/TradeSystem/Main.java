package net.dongwontuna.TradeSystem;

import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.gateio.GateioExchange;

import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Main {

    private static volatile HashMap<String,Exchange> exchangeHashMap = new HashMap<>();
    public static HashMap<String,HashMap<String,String>> API_KEYS = new HashMap<>();

    static{
        API_KEYS.put("GATEIO", SqlManager.getAPI("GATEIO"));
        API_KEYS.put("BINANCE", SqlManager.getAPI("BINANCE"));
        API_KEYS.put("TELEGRAM",SqlManager.getAPI("TELEGRAM"));
    }

    private static void CheckTelegramToken(){
        if (SqlManager.getAPI("TELEGRAM").isEmpty()) {
            Scanner input = new Scanner(System.in);
            System.out.println("Please input the Telegram bot Token\n");
            System.out.print("TOKEN KEY : ");
            String APIKEY = input.nextLine();
            System.out.println();
            SqlManager.setAPI("TELEGRAM",APIKEY,"0");
            System.out.println("THE TELEGRAM TOKEN IS SETUPED");
            System.out.println("Now you can enter the /start to bot to Start!");
        }
    }

    private static void CreateExchangesInstance(){
        CompletableFuture.runAsync(()->exchangeHashMap.put("BINANCE",new Exchange(BinanceExchange.class.getName())));
        CompletableFuture.runAsync(()->exchangeHashMap.put("GATEIO",new Exchange(GateioExchange.class.getName())));
    }

    public static void main(String[] args) {
        CheckTelegramToken();
        CreateExchangesInstance();



    }
}
