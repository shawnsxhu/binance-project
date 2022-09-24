package com.example.binance.controller;

import com.example.binance.dto.CandleItem;
import com.example.binance.dto.ConsumeInfo;
import com.example.binance.service.ConsumeService;
import com.example.binance.service.ValidationService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConsumeController {
    @Autowired
    ValidationService validationService;

    @Autowired
    ConsumeService consumeService;

    @Autowired
    KafkaTemplate<String, List<CandleItem>> kafkaTemplate;

    @Value("${LIMIT}")
    private int limit;

//
    @KafkaListener(
            topics = "binanceConsumerTopic",
            groupId = "consumer",
            containerFactory = "consumeInfoListenerContainerFactory")
    public void consume(ConsumeInfo consumeInfo, Acknowledgment acknowledgment){

        //TODO of todo Aggregation - stream - Redis -key (symbol, frequency, time span) - value
        //TODO client controller submit request with (symbol, frequency, time span) return list of requested candle item

        //TODO class diagram & data (database schema) diagram -> docs (diagram using draw.io)
        //TODO Jenkins: CI/CD pipeline (2 next time)
        //TODO Unit test, Integration test (1 Saturday check)
        //TODO docker compose (3)

        String symbol = consumeInfo.getSymbol();
        Long openTime = consumeInfo.getOpenTime();
        Long closeTime = consumeInfo.getCloseTime();
        String timeInterval = consumeInfo.getTimeInterval();
        validationService.isValid(symbol, openTime, closeTime, timeInterval);
        //acknowledge here
        acknowledgment.acknowledge();
        List<CandleItem> candleItems = consumeService.get(symbol, openTime, closeTime, timeInterval);
        for(List<CandleItem> candleItemPartition : ListUtils.partition(candleItems, limit)){
            kafkaTemplate.send("binanceConsumerClientTopic", candleItemPartition);
        }
    }
}
