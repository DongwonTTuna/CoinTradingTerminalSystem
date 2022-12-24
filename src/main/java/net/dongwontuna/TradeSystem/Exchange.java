package net.dongwontuna.TradeSystem;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;

import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Exchange {

    private final org.knowm.xchange.Exchange exchange;

    private HashMap<String, Instrument> symbolsHashMap = new HashMap<>();
    private MarketDataService marketDataService;
    private TradeService tradeService;
    private AccountService accountService;


    private static void setupAPI(String Exchange){
        Scanner input = new Scanner(System.in);

        System.out.println(Exchange + "に接続するためにAPI KEYとSECRET KEYを入力してください。\n");
        System.out.print("API KEY : ");
        String APIKEY = input.next();

        System.out.println();
        System.out.print("SECRET KEY : ");
        String SECRETKEY = input.next();

        HashMap<String,String> tmp = new HashMap<>();
        tmp.put("API_KEY",APIKEY);
        tmp.put("SECRET_KEY",SECRETKEY);

        // Update API_KEYS in Main.java
        Main.API_KEYS.put(Exchange,tmp);

        // Update Sql
        SqlManager.setAPI(Exchange,APIKEY,SECRETKEY);
        System.out.println();

    }
    public Exchange(String cls,String Exchange){
        // Create exchange Instance
        exchange = ExchangeFactory.INSTANCE.createExchangeWithoutSpecification(cls);
        ExchangeSpecification exSpec = exchange.getDefaultExchangeSpecification();

        // If the API key is empty, get API keys from the user.
        if (Main.API_KEYS.get(Exchange).isEmpty()){
            setupAPI(Exchange);
        }

        // Loop if the API KEY is not valid.
        while(true){
            exSpec.setApiKey(Main.API_KEYS.get(Exchange).get("API_KEY"));
            exSpec.setSecretKey(Main.API_KEYS.get(Exchange).get("SECRET_KEY"));
            try{
                exchange.applySpecification(exSpec);
                break;
            }catch (Exception e){
                setupAPI(Exchange);
            }
        }

        // Load the exchange services.
        CompletableFuture.runAsync(()->this.marketDataService = exchange.getMarketDataService());
        CompletableFuture.runAsync(()->this.accountService = exchange.getAccountService());
        CompletableFuture.runAsync(()->this.tradeService = exchange.getTradeService());

        // Put the Symbols and Instructment to Hashmap.
        exchange.getExchangeInstruments().forEach(item -> {
            if (item.toString().matches("[a-zA-Z0-9]+/USDT") && !item.toString().contains("UP") && !item.toString().contains("DOWN")){
                symbolsHashMap.put(item.toString(),item);
            }
        });

    }

}
