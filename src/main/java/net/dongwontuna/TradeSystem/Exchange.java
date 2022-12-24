package net.dongwontuna.TradeSystem;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;

import java.util.concurrent.CompletableFuture;

public class Exchange {

    private final org.knowm.xchange.Exchange exchange;
    private MarketDataService marketDataService;
    private TradeService tradeService;
    private AccountService accountService;


    public Exchange(String cls){
        exchange = ExchangeFactory.INSTANCE.createExchange(cls);
        CompletableFuture.runAsync(()->this.marketDataService = exchange.getMarketDataService());
        CompletableFuture.runAsync(()->this.accountService = exchange.getAccountService());
        CompletableFuture.runAsync(()->this.tradeService = exchange.getTradeService());
        System.out.println(exchange.getExchangeInstruments());
        System.out.println(exchange.getExchangeSymbols());
    }

}
