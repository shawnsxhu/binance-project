package com.example.binance.repository;

import com.example.binance.dto.Candle;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
class CandleMapperTest {

    @Autowired
    private CandleMapper candleMapper;

    @Test
    void insert() {
        //given
        String loadId = UUID.randomUUID().toString();
        Candle candle = Candle.builder()
                .loadId(loadId)
                .symbol("BTCUSDT")
                .openTime("12345678")
                .open("55555")
                .high("99999")
                .low("11111")
                .close("90909")
                .volume("555")
                .closeTime("23456")
                .quoteAssetVolume("666")
                .numTrade("777")
                .takerBuyBaseAssetVolume("222")
                .takerBuyQuoteAssetVolume("333")
                .ignore("ignore")
                .build();

        //process
        Exception exception = assertThrows(DataIntegrityViolationException.class,
                ()->{candleMapper.insert(null);});
        candleMapper.insert(candle);

        //expect
        String errorMessage = exception.getMessage();
        String expectedMessage = "NULL not allowed";
        assertThat(errorMessage).contains(expectedMessage);
        assertThat(candleMapper.findAll().size()).isEqualTo(2);
        assertThat(candleMapper.findById(null)).isEqualTo(null);
        assertThat(candleMapper.findById(loadId)).isEqualTo(candle);
    }

    @Test
    void insertBatch() {
        String loadId = UUID.randomUUID().toString();
        Candle candle1 = Candle.builder()
                .loadId(loadId)
                .symbol("BTCUSDT")
                .openTime("12345678")
                .open("55555555")
                .high("99999999")
                .low("11111111")
                .close("90909090")
                .volume("555")
                .closeTime("23456789")
                .quoteAssetVolume("666")
                .numTrade("777")
                .takerBuyBaseAssetVolume("222")
                .takerBuyQuoteAssetVolume("333")
                .ignore("ignore")
                .build();

        Candle candle2 = Candle.builder()
                .loadId(loadId)
                .symbol("BTCUSDT")
                .openTime("22345678")
                .open("55555555")
                .high("99999999")
                .low("11111111")
                .close("90909090")
                .volume("555")
                .closeTime("33456789")
                .quoteAssetVolume("666")
                .numTrade("777")
                .takerBuyBaseAssetVolume("222")
                .takerBuyQuoteAssetVolume("333")
                .ignore("ignore")
                .build();

        Candle candle3 = Candle.builder()
                .loadId(loadId)
                .symbol("BTCUSDT")
                .openTime("32345678")
                .open("55555555")
                .high("99999999")
                .low("11111111")
                .close("90909090")
                .volume("555")
                .closeTime("43456789")
                .quoteAssetVolume("666")
                .numTrade("777")
                .takerBuyBaseAssetVolume("222")
                .takerBuyQuoteAssetVolume("333")
                .ignore("ignore")
                .build();

        List<Candle> candles = new ArrayList<>();
        candles.add(candle1);
        candles.add(candle2);
        candles.add(candle3);
        List<Candle> emptyCandles = new ArrayList<>();

        //process
        candleMapper.insertBatch(candles);
        Exception exception = assertThrows(Exception.class, ()->{candleMapper.insertBatch(emptyCandles);});

        //expect
        List<Candle> candlesExpect = candleMapper.findAll();
        assertThat(candlesExpect.get(1)).isEqualTo(candle1);
        assertThat(candlesExpect.get(2)).isEqualTo(candle2);
        assertThat(candlesExpect.get(3)).isEqualTo(candle3);

        String errorMessage = exception.getMessage();
        String expectedMessage = "insert into candles (symbol, load_id, open_time, open, high, " +
                "low, close, volume, close_time, quote_asset_volume, num_trade, " +
                "taker_buy_base_asset_volume, taker_buy_quote_asset_volume, _ignore) values";
        assertThat(errorMessage).contains(expectedMessage);
    }
}