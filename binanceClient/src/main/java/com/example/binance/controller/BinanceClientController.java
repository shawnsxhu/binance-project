package com.example.binance.controller;

import com.example.binance.dto.CandleItem;
import com.example.binance.dto.ConsumeInfo;
import com.example.binance.dto.LoadInfo;
import com.example.binance.util.TimeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
public class BinanceClientController {
    @Autowired
    KafkaTemplate<String, LoadInfo> kafkaTemplate;

    @Autowired
    KafkaTemplate<String, ConsumeInfo> kafkaConsumerTemplate;

    @Autowired
    TimeParser timeParser;

    @Value("${partitionNum}")
    int partitionNum;

    @PostMapping("load")
    public void load(@RequestParam(value = "symbol", required = true) String symbol,
                     @RequestParam(value = "startTime", required = true) Long startTime,
                     @RequestParam(value = "endTime", required = true) Long endTime) {
        List<Long> yearList = timeParser.getYears(startTime, endTime);
        String message;
        for (int i = 0; i < yearList.size() - 1; i++) {
            LoadInfo sentMessage = new LoadInfo(symbol, yearList.get(i), yearList.get(i + 1));
            ListenableFuture<SendResult<String, LoadInfo>> future =
                    kafkaTemplate.send("binanceTopic", i % partitionNum,
                            Instant.now().toString(), sentMessage);

            future.addCallback(new ListenableFutureCallback<SendResult<String, LoadInfo>>() {
                @Override
                public void onSuccess(SendResult<String, LoadInfo> result) {
                    System.out.println("Sent message=[" + sentMessage.toString() +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                }

                @Override
                public void onFailure(Throwable ex) {
                    System.out.println("Unable to send message=["
                            + sentMessage.toString() + "] due to : " + ex.getMessage());
                }
            });
            //return loadId, load status, load...
        }
    }

    @PostMapping("get")
    public void get(@RequestParam(value = "symbol", required = true) String symbol,
                                @RequestParam(value = "startTime", required = true) Long startTime,
                                @RequestParam(value = "endTime", required = true) Long endTime,
                                @RequestParam(value = "interval", required = true) String interval) {

        String message;
        ConsumeInfo sentMessage = new ConsumeInfo(symbol, interval, startTime, endTime);
        ListenableFuture<SendResult<String, ConsumeInfo>> future =
                kafkaConsumerTemplate.send("binanceConsumerTopic",1, Instant.now().toString(), sentMessage);

        future.addCallback(new ListenableFutureCallback<SendResult<String, ConsumeInfo>>() {
            @Override
            public void onSuccess(SendResult<String, ConsumeInfo> result) {
                System.out.println("Sent message=[" + sentMessage.toString() +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                        + sentMessage.toString() + "] due to : " + ex.getMessage());
            }
        });
    }

    @KafkaListener(topics = "binanceConsumerClientTopic",
            groupId = "consumer",
            containerFactory = "candleItemListenerContainerFactory")
    public List<CandleItem> getFromConsumer(Acknowledgment acknowledgment, List<CandleItem> candleItems){
        System.out.println(candleItems.toString());
        acknowledgment.acknowledge();
        return candleItems;
    }
}

