package com.example.binance.repository;

import com.example.binance.dao.ICandleItemDAO;
import com.example.binance.dto.CandleItem;
import com.example.binance.dto.ConsumeInfo;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class CandleItemRedisRepository implements ICandleItemDAO {
    private final String hashReference = "CandleItem";

    @Resource(name="redisTemplate")
    private HashOperations<String, ConsumeInfo, List<CandleItem>> hashOperations;

    @Override
    public void saveCandleItem(List<CandleItem> candleItem, ConsumeInfo id) {
        hashOperations.putIfAbsent(hashReference, id, candleItem);
    }

    @Override
    public List<CandleItem> getOneCandleItem(ConsumeInfo id) {
        return hashOperations.get(hashReference, id);
    }

    @Override
    public void updateCandleItem(List<CandleItem> candleItem, ConsumeInfo id) {
        hashOperations.put(hashReference, id, candleItem);
    }

    @Override
    public Map<ConsumeInfo, List<CandleItem>> getAllCandleItems() {
        return hashOperations.entries(hashReference);
    }

    @Override
    public void deleteCandleItem(ConsumeInfo id) {
        hashOperations.delete(hashReference, id);
    }

    @Override
    public void saveAllCandleItems(Map<ConsumeInfo, List<CandleItem>> map) {
        hashOperations.putAll(hashReference, map);
    }
}
