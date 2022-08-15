package com.example.binance.repository;

import com.example.binance.dto.CandleItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CandleItemMapper {
    @Select("SELECT * FROM candle_items")
    public List<CandleItem> findAll();

    @Select("SELECT * FROM candle_items WHERE id = #{id}")
    public CandleItem findById(Long id);

    @Select("SELECT * FROM candle_items WHERE open_time >= #{openTime} AND close_time <= #{closeTime} ORDER BY open_time")
    public List<CandleItem> findByTime(Long openTime, Long closeTime);
}
