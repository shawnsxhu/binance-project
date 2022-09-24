package com.example.binance.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
public class CandleItem {
    private String loadId;
    private String symbol;
    private Long openTime;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal volume;
    private Long closeTime;
    private BigDecimal quoteAssetVolume;
    private BigInteger numTrade;

    public CandleItem(Candle candle){
        this.symbol = candle.getSymbol();
        this.loadId = candle.getLoadId();
        this.openTime = Long.parseLong(candle.getOpenTime());
        this.open = new BigDecimal(candle.getOpen());
        this.high = new BigDecimal(candle.getHigh());
        this.low = new BigDecimal(candle.getLow());
        this.close = new BigDecimal(candle.getClose());
        this.volume = new BigDecimal(candle.getVolume());
        this.closeTime = Long.parseLong(candle.getCloseTime());
        this.quoteAssetVolume = new BigDecimal(candle.getQuoteAssetVolume());
        this.numTrade = new BigInteger(candle.getNumTrade());
    }

//    public CandleItem(String loadId, String symbol, Long openTime, BigDecimal open, BigDecimal high, BigDecimal low,
//                      BigDecimal close, BigDecimal volume, Long closeTime,
//                      BigDecimal quoteAssetVolume, BigInteger numTrade) {
//        this.loadId = loadId;
//        this.symbol = symbol;
//        this.openTime = openTime;
//        this.open = open;
//        this.high = high;
//        this.low = low;
//        this.close = close;
//        this.volume = volume;
//        this.closeTime = closeTime;
//        this.quoteAssetVolume = quoteAssetVolume;
//        this.numTrade = numTrade;
//    }
//
//    @Override
//    public String toString() {
//        return "CandleItem{" +
//                "loadId='" + loadId + '\'' +
//                ", openTime=" + openTime +
//                ", closeTime=" + closeTime +
//                '}';
//    }
}
