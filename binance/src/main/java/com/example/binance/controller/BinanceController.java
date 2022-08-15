package com.example.binance.controller;

import com.example.binance.dto.Candle;
import com.example.binance.dto.LoadInfo;
import com.example.binance.service.LoadService;
import com.example.binance.service.ValidationService;
import com.example.binance.util.Symbol;
import com.example.binance.util.TimeInterval;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
public class BinanceController {
    @Autowired
    LoadService loadService;

    @Autowired
    ValidationService validationService;

    @Autowired
    KafkaAdmin admin;

    @KafkaListener(
            topics = "binanceTopic",
            groupId = "loader",
            containerFactory = "loadInfoListenerContainerFactory")
    public String load(LoadInfo loadInfo, Acknowledgment acknowledgment){

        //TODO of todo Aggregation - stream - Redis -key (symbol, frequency, time span) - value
        //TODO client controller submit request with (symbol, frequency, time span) return list of requested candle item

        //TODO docker file
        //TODO 刷题策略 excel

        String symbol = loadInfo.getSymbol();
        Long startTime = loadInfo.getStartTime();
        Long endTime = loadInfo.getEndTime();
        validationService.isValid(symbol, startTime, endTime);
        //acknowledge here
        String loadId = loadService.loadFromBinanceApi(symbol, startTime, endTime);
        acknowledgment.acknowledge();
        //return loadId, load status, load...
        return loadId;
    }
}
