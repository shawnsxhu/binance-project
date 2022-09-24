package com.example.binance.repository;

import com.example.binance.dto.Candle;
import com.example.binance.dto.CandleItem;
import org.h2.jdbc.JdbcSQLSyntaxErrorException;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
class CandleItemMapperTest {
    @Autowired
    private CandleItemMapper candleItemMapper;

    @Test
    void insert() {
        String loadId = UUID.randomUUID().toString();
        CandleItem candleItem = new CandleItem(
                loadId,
                "BTCUSDT",
                12345678L,
                new BigDecimal(55555),
                new BigDecimal(99999),
                new BigDecimal(11111),
                new BigDecimal(90909),
                new BigDecimal(555),
                23456789L,
                new BigDecimal(666),
                new BigInteger("777")
                );

        //process
        Exception exception = assertThrows(DataIntegrityViolationException.class,
                ()->{candleItemMapper.insert(null);});
        candleItemMapper.insert(candleItem);

        //expect
        String errorMessage = exception.getMessage();
        String expectedMessage = "NULL not allowed";
        assertThat(errorMessage).contains(expectedMessage);
        assertThat(candleItemMapper.findAll().size()).isEqualTo(2);
        assertThat(candleItemMapper.findById(null)).isEqualTo(null);

        CandleItem candleItemTest = candleItemMapper.findById(loadId);
        assertThat(candleItemTest.getLoadId()).isEqualTo(candleItem.getLoadId());
        assertThat(candleItemTest.getSymbol()).isEqualTo(candleItem.getSymbol());
        assertThat(candleItemTest.getOpenTime()).isEqualTo(candleItem.getOpenTime());
        //decimal compare abs(a - b) threshold
        assertThat(candleItemTest.getOpen().compareTo(candleItem.getOpen())).isEqualTo(0);
        assertThat(candleItemTest.getCloseTime()).isEqualTo(candleItem.getCloseTime());
        assertThat(candleItemTest.getClose().compareTo(candleItem.getClose())).isEqualTo(0);
        assertThat(candleItemTest.getHigh().compareTo(candleItem.getHigh())).isEqualTo(0);
        assertThat(candleItemTest.getLow().compareTo(candleItem.getLow())).isEqualTo(0);
        assertThat(candleItemTest.getVolume().compareTo(candleItem.getVolume())).isEqualTo(0);
        assertThat(candleItemTest.getQuoteAssetVolume().compareTo(candleItem.getQuoteAssetVolume())).isEqualTo(0);
        assertThat(candleItemTest.getNumTrade()).isEqualTo(candleItem.getNumTrade());
    }

    @Test
    void insertBatch() {
        String loadId = UUID.randomUUID().toString();
        Candle candle1 = Candle.builder()
                .loadId(loadId)
                .symbol("BTCUSDT")
                .openTime("12345678")
                .open("55555")
                .high("99999")
                .low("11111")
                .close("90909")
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
                .open("55555")
                .high("99999")
                .low("11111")
                .close("90909")
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
                .open("55555")
                .high("99999")
                .low("11111")
                .close("90909")
                .volume("555")
                .closeTime("43456789")
                .quoteAssetVolume("666")
                .numTrade("777")
                .takerBuyBaseAssetVolume("222")
                .takerBuyQuoteAssetVolume("333")
                .ignore("ignore")
                .build();

        CandleItem candleItem1 = new CandleItem(candle1);
        CandleItem candleItem2 = new CandleItem(candle2);
        CandleItem candleItem3 = new CandleItem(candle3);
        List<CandleItem> candleItems = new ArrayList<>();
        candleItems.add(candleItem1);
        candleItems.add(candleItem2);
        candleItems.add(candleItem3);
        List<CandleItem> emptyCandleItems = new ArrayList<>();

        //process
        candleItemMapper.insertBatch(candleItems);
        Exception exception = assertThrows(BadSqlGrammarException.class, ()->{candleItemMapper.insertBatch(emptyCandleItems);});

        //expect
        List<CandleItem> candleItemExpect = candleItemMapper.findAll();
        
        CandleItem candleItemExpect1 = candleItemExpect.get(1);
        CandleItem candleItemExpect2 = candleItemExpect.get(2);
        CandleItem candleItemExpect3 = candleItemExpect.get(3);

        assertThat(candleItemExpect1.getLoadId()).isEqualTo(candleItem1.getLoadId());
        assertThat(candleItemExpect1.getSymbol()).isEqualTo(candleItem1.getSymbol());
        assertThat(candleItemExpect1.getOpenTime()).isEqualTo(candleItem1.getOpenTime());
        assertThat(candleItemExpect1.getOpen().compareTo(candleItem1.getOpen())).isEqualTo(0);
        assertThat(candleItemExpect1.getCloseTime()).isEqualTo(candleItem1.getCloseTime());
        assertThat(candleItemExpect1.getClose().compareTo(candleItem1.getClose())).isEqualTo(0);
        assertThat(candleItemExpect1.getHigh().compareTo(candleItem1.getHigh())).isEqualTo(0);
        assertThat(candleItemExpect1.getLow().compareTo(candleItem1.getLow())).isEqualTo(0);
        assertThat(candleItemExpect1.getVolume().compareTo(candleItem1.getVolume())).isEqualTo(0);
        assertThat(candleItemExpect1.getQuoteAssetVolume().compareTo(candleItem1.getQuoteAssetVolume())).isEqualTo(0);
        assertThat(candleItemExpect1.getNumTrade()).isEqualTo(candleItem1.getNumTrade());

        assertThat(candleItemExpect2.getLoadId()).isEqualTo(candleItem2.getLoadId());
        assertThat(candleItemExpect2.getSymbol()).isEqualTo(candleItem2.getSymbol());
        assertThat(candleItemExpect2.getOpenTime()).isEqualTo(candleItem2.getOpenTime());
        assertThat(candleItemExpect2.getOpen().compareTo(candleItem2.getOpen())).isEqualTo(0);
        assertThat(candleItemExpect2.getCloseTime()).isEqualTo(candleItem2.getCloseTime());
        assertThat(candleItemExpect2.getClose().compareTo(candleItem2.getClose())).isEqualTo(0);
        assertThat(candleItemExpect2.getHigh().compareTo(candleItem2.getHigh())).isEqualTo(0);
        assertThat(candleItemExpect2.getLow().compareTo(candleItem2.getLow())).isEqualTo(0);
        assertThat(candleItemExpect2.getVolume().compareTo(candleItem2.getVolume())).isEqualTo(0);
        assertThat(candleItemExpect2.getQuoteAssetVolume().compareTo(candleItem2.getQuoteAssetVolume())).isEqualTo(0);
        assertThat(candleItemExpect2.getNumTrade()).isEqualTo(candleItem2.getNumTrade());

        assertThat(candleItemExpect3.getLoadId()).isEqualTo(candleItem3.getLoadId());
        assertThat(candleItemExpect3.getSymbol()).isEqualTo(candleItem3.getSymbol());
        assertThat(candleItemExpect3.getOpenTime()).isEqualTo(candleItem3.getOpenTime());
        assertThat(candleItemExpect3.getOpen().compareTo(candleItem3.getOpen())).isEqualTo(0);
        assertThat(candleItemExpect3.getCloseTime()).isEqualTo(candleItem3.getCloseTime());
        assertThat(candleItemExpect3.getClose().compareTo(candleItem3.getClose())).isEqualTo(0);
        assertThat(candleItemExpect3.getHigh().compareTo(candleItem3.getHigh())).isEqualTo(0);
        assertThat(candleItemExpect3.getLow().compareTo(candleItem3.getLow())).isEqualTo(0);
        assertThat(candleItemExpect3.getVolume().compareTo(candleItem3.getVolume())).isEqualTo(0);
        assertThat(candleItemExpect3.getQuoteAssetVolume().compareTo(candleItem3.getQuoteAssetVolume())).isEqualTo(0);
        assertThat(candleItemExpect3.getNumTrade()).isEqualTo(candleItem3.getNumTrade());

        String errorMessage = exception.getMessage();
        String expectedMessage = "insert into candle_items (load_id, symbol, open_time, open, high, low, close, " +
                "volume, close_time, quote_asset_volume, num_trade) values";
        assertThat(errorMessage).contains(expectedMessage);
    }

    @Test
    void insertFromCandles() {
        int i = candleItemMapper.insertFromCandles("Test");
        Candle candle = Candle.builder()
                .symbol("BTCUSDT")
                .loadId("12345")
                .openTime("160000000")
                .open("123.123")
                .high("234.234")
                .low("78.78")
                .close("232.232")
                .volume("777.777")
                .closeTime("160001000")
                .quoteAssetVolume("277.277")
                .numTrade("34")
                .takerBuyBaseAssetVolume("23.23")
                .takerBuyQuoteAssetVolume("45.45")
                .ignore("0")
                .build();
        CandleItem candleItem = new CandleItem(candle);

        assertThat(i).isEqualTo(1);
        
        CandleItem candleItemExpected = candleItemMapper.findAll().get(1);
        assertThat(candleItemExpected.getLoadId()).isEqualTo("Test");
        assertThat(candleItemExpected.getSymbol()).isEqualTo(candleItem.getSymbol());
        assertThat(candleItemExpected.getOpenTime()).isEqualTo(candleItem.getOpenTime());
        assertThat(candleItemExpected.getOpen().compareTo(candleItem.getOpen())).isEqualTo(0);
        assertThat(candleItemExpected.getCloseTime()).isEqualTo(candleItem.getCloseTime());
        assertThat(candleItemExpected.getClose().compareTo(candleItem.getClose())).isEqualTo(0);
        assertThat(candleItemExpected.getHigh().compareTo(candleItem.getHigh())).isEqualTo(0);
        assertThat(candleItemExpected.getLow().compareTo(candleItem.getLow())).isEqualTo(0);
        assertThat(candleItemExpected.getVolume().compareTo(candleItem.getVolume())).isEqualTo(0);
        assertThat(candleItemExpected.getQuoteAssetVolume().compareTo(candleItem.getQuoteAssetVolume())).isEqualTo(0);
        assertThat(candleItemExpected.getNumTrade()).isEqualTo(candleItem.getNumTrade());
    }
}