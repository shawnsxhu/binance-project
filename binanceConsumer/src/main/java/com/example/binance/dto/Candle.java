package com.example.binance.dto;

import com.example.binance.util.Symbol;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Candle {
    public static Candle getCandle(List<Object> data, String loadId, Symbol symbol){
        int index = 0;
        Candle candle = Candle.builder()
                .loadId(loadId)
                .symbol(symbol.getSymbol())
                .openTime(data.get(index++).toString())
                .open(data.get(index++).toString())
                .high(data.get(index++).toString())
                .low(data.get(index++).toString())
                .close(data.get(index++).toString())
                .volume(data.get(index++).toString())
                .closeTime(data.get(index++).toString())
                .quoteAssetVolume(data.get(index++).toString())
                .numTrade(data.get(index++).toString())
                .takerBuyBaseAssetVolume(data.get(index++).toString())
                .takerBuyQuoteAssetVolume(data.get(index++).toString())
                .ignore(data.get(index++).toString())
                .build();
        return candle;
    }

    private String loadId;
    private String symbol;


    private String openTime;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;
    private String closeTime;
    private String quoteAssetVolume;
    private String numTrade;
    private String takerBuyBaseAssetVolume;
    private String takerBuyQuoteAssetVolume;
    private String ignore;
}
