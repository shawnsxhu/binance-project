package com.example.binance.dao;

import java.util.List;
import java.util.Map;
import com.example.binance.dto.CandleItem;
import com.example.binance.dto.ConsumeInfo;

public interface ICandleItemDAO {
    void saveCandleItem(List<CandleItem> candleItem, ConsumeInfo id);
    List<CandleItem> getOneCandleItem(ConsumeInfo id);
    void updateCandleItem(List<CandleItem> candleItem, ConsumeInfo id);
    Map<ConsumeInfo, List<CandleItem>> getAllCandleItems();
    void deleteCandleItem(ConsumeInfo id);
    void saveAllCandleItems(Map<ConsumeInfo, List<CandleItem>> map);
}

