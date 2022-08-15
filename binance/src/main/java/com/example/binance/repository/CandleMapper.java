package com.example.binance.repository;

import com.example.binance.dto.Candle;
import org.apache.ibatis.annotations.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Mapper
public interface CandleMapper {
    @Select("SELECT * FROM candles")
    public List<Candle> findAll();

    @Select("SELECT * FROM candles WHERE id = #{id}")
    public Candle findById(Long id);

    @Delete("DELETE FROM candles WHERE id = #{id}")
    public int deleteById(Long id);

    @Insert("INSERT INTO candles(symbol, load_id, open_time, open, " +
            "high, low, close, volume, close_time, quote_asset_volume, num_trade, " +
            "taker_buy_base_asset_volume, taker_buy_quote_asset_volume, _ignore)" +
            " VALUES (#{symbol}, #{loadId}, #{openTime}, #{open}, " +
            "#{high}, #{low}, #{close}, #{volume}, #{closeTime}, #{quoteAssetVolume}, #{numTrade}, " +
            "#{takerBuyBaseAssetVolume}, #{takerBuyQuoteAssetVolume}, #{ignore})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    public Long insert(Candle candle);

    @Insert({
            "<script>",
            "insert into candles (symbol, load_id, open_time, open, " +
                    "high, low, close, volume, close_time, quote_asset_volume, num_trade, " +
                    "taker_buy_base_asset_volume, taker_buy_quote_asset_volume, _ignore)",
            "values ",
            "<foreach  collection='candleList' item='candle' separator=','>",
            "( #{candle.symbol}, #{candle.loadId}, #{candle.openTime}, #{candle.open}, " +
                    "#{candle.high}, #{candle.low}, #{candle.close}, #{candle.volume}, " +
                    "#{candle.closeTime}, #{candle.quoteAssetVolume}, #{candle.numTrade}, " +
                    "#{candle.takerBuyBaseAssetVolume}, #{candle.takerBuyQuoteAssetVolume}, #{candle.ignore})",
            "</foreach>",
            "</script>"
    })
    int insertBatch(@Param("candleList") @NotEmpty List<Candle> candleList);
}
