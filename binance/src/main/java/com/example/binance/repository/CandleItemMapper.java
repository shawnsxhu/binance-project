package com.example.binance.repository;

import com.example.binance.dto.Candle;
import com.example.binance.dto.CandleItem;
import org.apache.ibatis.annotations.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Mapper
public interface CandleItemMapper {
    @Select("SELECT * FROM candle_items")
    public List<Candle> findAll();

    @Select("SELECT * FROM candle_items WHERE id = #{id}")
    public Candle findById(Long id);

    @Delete("DELETE FROM candle_items WHERE id = #{id}")
    public int deleteById(Long id);

    @Insert("INSERT INTO candle_items(load_id, open_time, open, " +
            "high, low, close, volume, close_time, quote_asset_volume, num_trade)" +
            " VALUES (#{loadId}, #{openTime}, #{open}, " +
            "#{high}, #{low}, #{close}, #{volume}, #{closeTime}, #{quoteAssetVolume}, #{numTrade})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    public Long insert(CandleItem candleItem);

    @Insert({
            "<script>",
            "insert into candle_items (load_id, symbol, open_time, open, " +
                    "high, low, close, volume, close_time, quote_asset_volume, num_trade)",
            "values ",
            "<foreach  collection='candleItemList' item='candleItem' separator=','>",
            "( #{candleItem.loadId}, #{candleItem.symbol}, #{candleItem.openTime}, #{candleItem.open}, " +
                    "#{candleItem.high}, #{candleItem.low}, #{candleItem.close}, #{candleItem.volume}, " +
                    "#{candleItem.closeTime}, #{candleItem.quoteAssetVolume}, #{candleItem.numTrade})",
            "</foreach>",
            "</script>"
    })
    int insertBatch(@Param("candleItemList") @NotEmpty List<CandleItem> candleItemList);

    @Insert("INSERT INTO candle_items (load_id, symbol, open_time, open, " +
            "high, low, close, volume, close_time, quote_asset_volume, num_trade)" +
            " SELECT #{loadId}, candles.symbol, CAST(candles.open_time AS UNSIGNED)," +
            " CAST(candles.open AS DECIMAL), CAST(candles.high AS DECIMAL), CAST(candles.low AS DECIMAL)," +
            " CAST(candles.close AS DECIMAL), CAST(candles.volume AS DECIMAL)," +
            " CAST(candles.close_time AS UNSIGNED), CAST(candles.quote_asset_volume AS DECIMAL)," +
            " CAST(candles.num_trade AS UNSIGNED)" +
            " FROM candles")
    int insertFromCandles(String loadId);
//
//    @Insert("INSERT INTO candle_items (load_id, symbol, open_time, open, " +
//            "high, low, close, volume, close_time, quote_asset_volume, num_trade)" +
//            " SELECT #{loadId}, candles.symbol, CAST(candles.open_time AS UNSIGNED)," +
//            " CAST(candles.open AS DECIMAL), CAST(candles.high AS DECIMAL), CAST(candles.low AS DECIMAL)," +
//            " CAST(candles.close AS DECIMAL), CAST(candles.volume AS DECIMAL)," +
//            " CAST(candles.close_time AS UNSIGNED), CAST(candles.quote_asset_volume AS DECIMAL)," +
//            " CAST(candles.num_trade AS UNSIGNED)" +
//            " FROM candles" +
//            " WHERE CAST(candles.open_time AS UNSIGNED) >= #{openTime}" +
//            " AND CAST(candles.close_time AS UNSIGNED) <= #{closeTime}")
//    int insertFromCandles(String loadId, Long openTime, Long closeTime);
}
