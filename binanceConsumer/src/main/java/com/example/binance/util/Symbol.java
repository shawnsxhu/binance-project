package com.example.binance.util;

public enum Symbol {
    BTCUSDT("BTCUSDT"),
    LTCUSDT("LTCUSDT"),
    ETHUSDT("ETHUSDT")
    ;

    String symbol;
    Symbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
