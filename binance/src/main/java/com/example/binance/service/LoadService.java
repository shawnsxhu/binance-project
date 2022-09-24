package com.example.binance.service;

import com.example.binance.dto.Candle;
import com.example.binance.dto.CandleItem;
import com.example.binance.repository.CandleItemMapper;
import com.example.binance.repository.CandleMapper;
import com.example.binance.util.*;
import com.sun.istack.NotNull;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class LoadService {
    @Value("${binanceUrlTemplate}")
    private String binanceUrlTemplate;

    @Autowired
    private CandleMapper candleMapper;

    @Autowired
    private CandleItemMapper candleItemMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private RestTemplate binanceRestTemplate;

    @Value("${minToMillisecond}")
    private int minToMillisecond;

    @Value("${requestLimit}")
    private int requestLimit;

    @Transactional
    public String loadFromBinanceApi(@NotNull String symbol, Long startTime, Long endTime) {
        //TODO setup Spring Kafka - See BinanceController
        //TODO exception handler in Spring
        //TODO log4J

        //Generate loadId
        String loadId = UUID.randomUUID().toString();

        //Load from API every 500 data
        List<Long> timeStampList = generateTimeStamp(startTime, endTime);

        System.out.println("Batch inserting data from API");

        for(int i = 0; i < timeStampList.size() - 1; i++) {
            System.out.println("Inserting timestamp No." + i + ": ");
            System.out.println("Start: " + timeStampList.get(i) + " End: " + timeStampList.get(i+1));
            //Prepare URI for Binance API get exchange
            String resourceUrl = String.format(binanceUrlTemplate,
                    Symbol.valueOf(symbol).getSymbol(),
                    TimeInterval._1m.getInterval(),
                    timeStampList.get(i),
                    timeStampList.get(i + 1));

            URI url = UriComponentsBuilder.fromHttpUrl(resourceUrl).build().toUri();

            //Use RestTemplate to get list of candle data list
            ResponseEntity<List<List<Object>>> responseEntity
                    = binanceRestTemplate.exchange(new RequestEntity<>(HttpMethod.GET, url),
                    new ParameterizedTypeReference<>() {
                    });
            List<List<Object>> response = responseEntity.getBody();

            List<Candle> candles = response.stream()
                    .map(l -> Candle.getCandle(l, loadId, Symbol.valueOf(symbol)))
                    .collect(Collectors.toList());
            if (candles.size() != 0) {
                candleMapper.insertBatch(candles);
            }

            List<CandleItem> candleItemList = new ArrayList<>();
            for (Candle candle : candles) {
                candleItemList.add(new CandleItem(candle));
            }

            if (candleItemList.size() != 0) {
                candleItemMapper.insertBatch(candleItemList);
            }
        }
        return loadId;
    }

    @Transactional
    public String loadItemsFromCandles() {
        String loadId = UUID.randomUUID().toString();

        candleItemMapper.insertFromCandles(loadId);

        return loadId;
    }
//
//    @Transactional
//    public String loadItemsFromCandles(Long openTime, Long closeTime) {
//        String loadId = UUID.randomUUID().toString();
//
//        candleItemMapper.insertFromCandles(loadId, openTime, closeTime);
//
//        return loadId;
//    }

    public List<Long> generateTimeStamp(Long startTime, Long endTime){
        Set<Long> timeStampList = new LinkedHashSet<>();
        final int maxMillisecond = minToMillisecond * requestLimit;
        if (startTime.equals(endTime)){
            timeStampList.add(startTime);
            List<Long> timestamps = new ArrayList<>(timeStampList);
            timestamps.add(endTime);
            return timestamps;
        }
        while(startTime < endTime){
            timeStampList.add(startTime);
            startTime += maxMillisecond;
        }
        timeStampList.add(endTime);
        return new ArrayList<>(timeStampList);
    }
}
