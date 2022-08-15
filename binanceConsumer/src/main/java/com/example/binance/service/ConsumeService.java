package com.example.binance.service;

import com.example.binance.dto.CandleItem;
import com.example.binance.dto.ConsumeInfo;
import com.example.binance.repository.CandleItemMapper;
import com.example.binance.repository.CandleItemRedisRepository;
import com.example.binance.util.TimeInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;

@Service
public class ConsumeService {
    @Autowired
    CandleItemMapper candleItemMapper;

    @Autowired
    CandleItemRedisRepository candleItemRedisRepository;

    public Pair<String, List<CandleItem>> get(String symbol, Long openTime, Long closeTime, String timeInterval) {
        String consumeID = UUID.randomUUID().toString();

        ConsumeInfo candleItemKey = new ConsumeInfo(symbol, timeInterval, openTime, closeTime);

        List<CandleItem> candleListRedis = candleItemRedisRepository.getOneCandleItem(candleItemKey);

        if (candleListRedis != null && candleListRedis.size() != 0){
            return Pair.of(consumeID, candleListRedis);
        }else{
            List<CandleItem> candleList = candleItemMapper.findByTime(openTime, closeTime)
                    .stream()
                    .collect(blockCollector(TimeInterval.valueOfInterval(timeInterval).getMinutes()))
                    .stream()
                    .map(CandleItem::combineCandleItems)
                    .toList();
            if (candleList.size() != 0) {
                candleItemRedisRepository.saveCandleItem(candleList, candleItemKey);
            }
            return Pair.of(consumeID, candleList);
        }
    }

    public static Collector<CandleItem, List<List<CandleItem>>, List<List<CandleItem>>> blockCollector(int blockSize) {
        return Collector.of(
                ArrayList<List<CandleItem>>::new,
                (list, value) -> {
                    List<CandleItem> block = (list.isEmpty() ? null : list.get(list.size() - 1));
                    if (block == null || block.size() == blockSize)
                        list.add(block = new ArrayList<>(blockSize));
                    block.add(value);
                },
                (r1, r2) -> { throw new UnsupportedOperationException("Parallel processing not supported"); }
        );
    }


}
