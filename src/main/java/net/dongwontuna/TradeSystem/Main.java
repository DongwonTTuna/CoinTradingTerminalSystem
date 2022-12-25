package net.dongwontuna.TradeSystem;

import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.gateio.GateioExchange;

import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static String currentExchange;
    public static HashMap<String,Exchange> exchangeHashMap = new HashMap<>();
    public static HashMap<String,HashMap<String,String>> API_KEYS = new HashMap<>();

    static{
        API_KEYS.put("GATEIO", SqlManager.getAPI("GATEIO"));
        API_KEYS.put("BINANCE", SqlManager.getAPI("BINANCE"));
        API_KEYS.put("TELEGRAM",SqlManager.getAPI("TELEGRAM"));
    }

    private static void ChangeTelegramToken(){
        Scanner input = new Scanner(System.in);
        System.out.println("TelegramのBotに接続するためのトークンを入力してください。\n");
        System.out.print("TOKEN キー : ");
        String APIKEY = input.next();
        System.out.println();
        SqlManager.setAPI("TELEGRAM",APIKEY,"0");
        System.out.println("セットアップされました。");
        System.out.println("Botに「/start」を入力して初めてみましょう！");
    }
    private static void CheckTelegramToken(){
        if (SqlManager.getAPI("TELEGRAM").isEmpty()) {
            ChangeTelegramToken();
        }
    }

    private static void CreateExchangesInstance(){
        CompletableFuture.runAsync(()->exchangeHashMap.put("BINANCE",new Exchange(BinanceExchange.class.getName(),"BINANCE")));
        CompletableFuture.runAsync(()->exchangeHashMap.put("GATEIO",new Exchange(GateioExchange.class.getName(),"GATEIO")));
    }

    private static void CLIFunction(){

        while (true){
            Scanner input = new Scanner(System.in);
            System.out.println("" +
                    " _____       _       \n" +
                    "/  __ \\     (_)      \n" +
                    "| /  \\/ ___  _ _ __  \n" +
                    "| |    / _ \\| | '_ \\ \n" +
                    "| \\__/\\ (_) | | | | |\n" +
                    " \\____/\\___/|_|_| |_|\n" +
                    "                     \n" +
                    "                     \n" +
                    "\n" +
                    "      _____             _ _               _____           _                 \n" +
                    "     |_   _|           | (_)             /  ___|         | |                \n" +
                    "       | |_ __ __ _  __| |_ _ __   __ _  \\ `--. _   _ ___| |_ ___ _ __ ___  \n" +
                    "       | | '__/ _` |/ _` | | '_ \\ / _` |  `--. \\ | | / __| __/ _ \\ '_ ` _ \\ \n" +
                    "       | | | | (_| | (_| | | | | | (_| | /\\__/ / |_| \\__ \\ ||  __/ | | | | |\n" +
                    "       \\_/_|  \\__,_|\\__,_|_|_| |_|\\__, | \\____/ \\__, |___/\\__\\___|_| |_| |_|\n" +
                    "                                   __/ |         __/ |                      \n" +
                    "                                  |___/         |___/                       \n");
            System.out.println("                                                    By DongwonTTuna\n\n");
            System.out.println("メニューから選択してください。\n");
            System.out.println("1.Telegramのトークンの変更");
            System.out.println("2.データの抹消\n");
            System.out.print("入力：");
            String inputedString = input.next();

            switch (inputedString){
                case "1" -> ChangeTelegramToken();
                case "2" -> System.out.println(SqlManager.deleteDBFile() ? "削除されました。": "削除が出来ませんでした。");
                default -> System.out.println("正しいメニュを入力してください。");
            }
        }
    }
    public static void main(String[] args) {
        CheckTelegramToken();
        CreateExchangesInstance();
        CLIFunction();
    }
}
