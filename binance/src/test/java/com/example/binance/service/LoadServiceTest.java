package com.example.binance.service;

import com.example.binance.dto.Candle;
import com.example.binance.dto.CandleItem;
import com.example.binance.repository.CandleItemMapper;
import com.example.binance.repository.CandleMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoadServiceTest {

    @Mock
    private CandleItemMapper candleItemMapper;

    @Mock
    private CandleMapper candleMapper;

    @Mock
    private RestTemplate binanceRestTemplate;

    @Captor
    private ArgumentCaptor<List<Candle>> candleCaptor;

    @Captor
    private ArgumentCaptor<List<CandleItem>> candleItemCaptor;

    @InjectMocks
    private LoadService loadService;

    @BeforeEach
    void setup(){
        ReflectionTestUtils.setField(loadService, "minToMillisecond", 60000);
        ReflectionTestUtils.setField(loadService, "requestLimit", 500);
        ReflectionTestUtils.setField(loadService, "binanceUrlTemplate",
                "https://www.binance.com/api/v3/klines?symbol=%1$s&interval=%2$s&startTime=%3$d&endTime=%4$d");
    }

    @Test
    void loadFromBinanceApi() {
        String symbol = "BTCUSDT";
        Long startTime = 1523577600000L;
        Long endTime = 1523577600000L;

        List<Long> timestamp = new ArrayList<>();
        timestamp.add(startTime);
        timestamp.add(endTime);

        List<List<Object>> body = new ArrayList<>();
        List<Object> candleAsList = new ArrayList<>();
        candleAsList.add(startTime.toString());
        candleAsList.add("7922.99000000");
        candleAsList.add("7936.99000000");
        candleAsList.add("7919.84000000");
        candleAsList.add("7935.61000000");
        candleAsList.add("53.68618000");
        candleAsList.add("1523577659999");
        candleAsList.add("425682.91899601");
        candleAsList.add("230");
        candleAsList.add("45.80239200");
        candleAsList.add("363185.75390966");
        candleAsList.add("0");
        body.add(candleAsList);

        ResponseEntity<List<List<Object>>> entity = new ResponseEntity<>(body, HttpStatus.OK);

        when(binanceRestTemplate.exchange(
                ArgumentMatchers.<RequestEntity<?>> any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<List<Object>>>>any()))
                .thenReturn(entity);


        String uuid = loadService.loadFromBinanceApi(symbol, startTime, endTime);

        Candle candle = Candle.builder()
                .symbol(symbol)
                .loadId(uuid)
                .openTime(startTime.toString())
                .open("7922.99000000")
                .high("7936.99000000")
                .low("7919.84000000")
                .close("7935.61000000")
                .volume("53.68618000")
                .closeTime("1523577659999")
                .quoteAssetVolume("425682.91899601")
                .numTrade("230")
                .takerBuyBaseAssetVolume("45.80239200")
                .takerBuyQuoteAssetVolume("363185.75390966")
                .ignore("0")
                .build();
        CandleItem candleItem = new CandleItem(candle);

        verify(candleMapper).insertBatch(candleCaptor.capture());
        verify(candleItemMapper).insertBatch(candleItemCaptor.capture());
        verify(candleMapper, times(1)).insertBatch(anyList());
        verify(candleItemMapper, times(1)).insertBatch(anyList());

        assertThat(candleCaptor.getValue().size()).isEqualTo(1);
        assertThat(candleCaptor.getValue().get(0)).isEqualTo(candle);
        assertThat(candleItemCaptor.getValue().size()).isEqualTo(1);
        assertThat(candleItemCaptor.getValue().get(0)).isEqualTo(candleItem);
    }

    @Test
    void loadItemsFromCandles() {
        String id = loadService.loadItemsFromCandles();
        verify(candleItemMapper, times(1)).insertFromCandles(id);
    }

    @Test
    void generateTimeStamp() {
        Long startTime = 1523577600000L;
        Long endTime1 = 1523577600000L;
        Long endTime2 = 1523677600000L;

        List<Long> timestamp1 = loadService.generateTimeStamp(startTime, endTime1);
        List<Long> expectedTimestamp1 = new ArrayList<>();
        expectedTimestamp1.add(1523577600000L);
        expectedTimestamp1.add(1523577600000L);
        assertThat(timestamp1.size()).isEqualTo(2);
        assertThat(timestamp1).isEqualTo(expectedTimestamp1);

        List<Long> timestamp2 = loadService.generateTimeStamp(startTime, endTime2);
        List<Long> expectedTimestamp2 = new ArrayList<>();
        expectedTimestamp2.add(1523577600000L);
        expectedTimestamp2.add(1523607600000L);
        expectedTimestamp2.add(1523637600000L);
        expectedTimestamp2.add(1523667600000L);
        expectedTimestamp2.add(1523677600000L);
        assertThat(timestamp2.size()).isEqualTo(5);
        assertThat(timestamp2).isEqualTo(expectedTimestamp2);
    }
}