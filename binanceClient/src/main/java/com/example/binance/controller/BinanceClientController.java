package com.example.binance.controller;

import com.example.binance.dto.LoadInfo;
import com.example.binance.util.TimeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
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
    TimeParser timeParser;

    @Value("${partitionNum}")
    int partitionNum;

    @PostMapping("load")
    public void load(@RequestParam(value = "symbol", required = true) String symbol,
                       @RequestParam(value = "startTime", required = true) Long startTime,
                       @RequestParam(value = "endTime", required = true) Long endTime){
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
}
