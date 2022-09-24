package com.example.binance.dto;

import com.example.binance.util.Symbol;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CandleTest {

    @Test
    void getCandle() {
        //Decrease hardcoding, use variables instead
        String symbol = "BTCUSDT";
        Candle candleExpected = Candle.builder()
                .symbol(symbol)
                .loadId("test")
                .openTime("12345")
                .open("12345.123")
                .high("23456.234")
                .low("11111.111")
                .close("22222.222")
                .volume("789.789")
                .closeTime("67890")
                .quoteAssetVolume("2323.2323")
                .numTrade("324")
                .takerBuyBaseAssetVolume("234")
                .takerBuyQuoteAssetVolume("345")
                .ignore("0")
                .build();

        List<Object> list = new ArrayList<>();
        list.add(12345);
        list.add(12345.123);
        list.add(23456.234);
        list.add(11111.111);
        list.add(22222.222);
        list.add(789.789);
        list.add(67890);
        list.add(2323.2323);
        list.add(324);
        list.add(234);
        list.add(345);
        list.add(0);

        Candle candle = Candle.getCandle(list, "test", Symbol.BTCUSDT);
        assertThat(candle).isEqualTo(candleExpected);
    }
}