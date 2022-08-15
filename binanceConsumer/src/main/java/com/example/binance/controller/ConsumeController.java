package com.example.binance.controller;

import com.example.binance.dto.CandleItem;
import com.example.binance.dto.ConsumeInfo;
import com.example.binance.service.ConsumeService;
import com.example.binance.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.kafka.annotation.KafkaListener;
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

//    @KafkaListener(
//            topics = "binanceTopic",
//            groupId = "consumer",
//            containerFactory = "consumeInfoListenerContainerFactory") ,
//            Acknowledgment acknowledgment, ConsumeInfo consumeInfo
    @PostMapping("load")
    public Pair<String, List<CandleItem>> consume(
            @RequestParam(value = "symbol", required = true) String symbol,
            @RequestParam(value = "startTime", required = true) Long openTime,
            @RequestParam(value = "endTime", required = true) Long closeTime,
            @RequestParam(value = "interval", required = true) String timeInterval){

        //TODO of todo Aggregation - stream - Redis -key (symbol, frequency, time span) - value
        //TODO client controller submit request with (symbol, frequency, time span) return list of requested candle item

        //TODO docker file
        //TODO 刷题策略 excel
//        String symbol = consumeInfo.getSymbol();
//        Long openTime = consumeInfo.getOpenTime();
//        Long closeTime = consumeInfo.getCloseTime();
//        String timeInterval = consumeInfo.getTimeInterval();
        validationService.isValid(symbol, openTime, closeTime, timeInterval);
        //acknowledge here
        //acknowledgment.acknowledge();
        return consumeService.get(symbol, openTime, closeTime, timeInterval);
    }
}
