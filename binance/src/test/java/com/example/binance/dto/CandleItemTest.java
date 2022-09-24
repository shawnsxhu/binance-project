package com.example.binance.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CandleItemTest {
    @Test
    void constructFromCandleTest(){
        String loadId = "test";
        String symbol = "BTCUSDT";
        Long openTime = 12345L;
        BigDecimal open = new BigDecimal("123.123");
        BigDecimal high = new BigDecimal("321.321");
        BigDecimal low = new BigDecimal("122.122");
        BigDecimal close = new BigDecimal("231.231");
        BigDecimal volume = new BigDecimal("75.75");
        Long closeTime = 54321L;
        BigDecimal quoteAssetVolume = new BigDecimal("9090.9090");
        BigInteger numTrade = new BigInteger("700");

        String openTimeString = "12345";
        String openString = "123.123";
        String highString = "321.321";
        String lowString = "122.122";
        String closeString = "231.231";
        String volumeString = "75.75";
        String closeTimeString = "54321";
        String quoteAssetVolumeString = "9090.9090";
        String numTradeString = "700";

        CandleItem candleItemExpected = CandleItem.builder()
                .loadId(loadId)
                .symbol(symbol)
                .openTime(openTime)
                .open(open)
                .close(close)
                .high(high)
                .low(low)
                .volume(volume)
                .closeTime(closeTime)
                .quoteAssetVolume(quoteAssetVolume)
                .numTrade(numTrade)
                .build();

        Candle candle = Candle.builder()
                .loadId(loadId)
                .symbol(symbol)
                .openTime(openTimeString)
                .open(openString)
                .close(closeString)
                .high(highString)
                .low(lowString)
                .volume(volumeString)
                .closeTime(closeTimeString)
                .quoteAssetVolume(quoteAssetVolumeString)
                .numTrade(numTradeString)
                .build();

        CandleItem candleItem = new CandleItem(candle);
        assertThat(candleItem).isEqualTo(candleItemExpected);
    }
}