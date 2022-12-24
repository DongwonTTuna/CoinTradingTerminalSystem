package net.dongwontuna.TradeSystem;

import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.gateio.GateioExchange;

import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static HashMap<String,Exchange> exchangeHashMap = new HashMap<>();
    public static HashMap<String,HashMap<String,String>> API_KEYS = new HashMap<>();

    static{
        API_KEYS.put("GATEIO", SqlManager.getAPI("GATEIO"));
        API_KEYS.put("BINANCE", SqlManager.getAPI("BINANCE"));
        API_KEYS.put("TELEGRAM",SqlManager.getAPI("TELEGRAM"));
    }

    private static void CheckTelegramToken(){
        if (SqlManager.getAPI("TELEGRAM").isEmpty()) {
            Scanner input = new Scanner(System.in);
            System.out.println("TelegramのBotに接続するためのトークンを入力してください。\n");
            System.out.print("TOKEN キー : ");
            String APIKEY = input.next();
            System.out.println();
            SqlManager.setAPI("TELEGRAM",APIKEY,"0");
            System.out.println("セットアップされました。");
            System.out.println("Botに「/start」を入力して初めてみましょう！");
        }
    }

    private static void CreateExchangesInstance(){
        exchangeHashMap.put("BINANCE",new Exchange(BinanceExchange.class.getName(),"BINANCE"));
        exchangeHashMap.put("GATEIO",new Exchange(GateioExchange.class.getName(),"GATEIO"));
    }

    public static void main(String[] args) {
        CheckTelegramToken();
        CreateExchangesInstance();
    }
}
