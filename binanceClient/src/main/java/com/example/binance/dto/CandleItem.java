package com.example.binance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

@Data
@Getter
@Setter
public class CandleItem implements Serializable {
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

    public CandleItem(){}
    public CandleItem(String loadId, String symbol, Long openTime, BigDecimal open, BigDecimal high, BigDecimal low,
                      BigDecimal close, BigDecimal volume, Long closeTime,
                      BigDecimal quoteAssetVolume, BigInteger numTrade) {
        this.loadId = loadId;
        this.symbol = symbol;
        this.openTime = openTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.closeTime = closeTime;
        this.quoteAssetVolume = quoteAssetVolume;
        this.numTrade = numTrade;
    }

    public static CandleItem combineCandleItems(List<CandleItem> candleItems){
        Comparator<CandleItem> comparator = new Comparator<CandleItem>() {
            @Override
            public int compare(CandleItem o1, CandleItem o2) {
                return o1.getHigh().subtract(o2.getHigh()).compareTo(BigDecimal.valueOf(0));
            }
        };
        return new CandleItem(
                candleItems.get(0).getLoadId(),
                candleItems.get(0).getSymbol(),
                candleItems.get(0).getOpenTime(),
                candleItems.get(0).getOpen(),
                candleItems.stream().max(comparator).get().getHigh(),
                candleItems.stream().min(comparator).get().getHigh(),
                candleItems.get(candleItems.size() - 1).getClose(),
                candleItems.stream()
                        .map(CandleItem::getVolume)
                        .reduce(BigDecimal.valueOf(0.0), BigDecimal::add),
                candleItems.get(candleItems.size() - 1).getCloseTime(),
                candleItems.stream()
                        .map(CandleItem::getQuoteAssetVolume)
                        .reduce(BigDecimal.valueOf(0.0), BigDecimal::add),
                candleItems.stream()
                        .map(CandleItem::getNumTrade)
                        .reduce(BigInteger.valueOf(0), BigInteger::add)
        );
    }

    @Override
    public String toString() {
        return "CandleItem{" +
                "loadId='" + loadId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", openTime=" + openTime +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                ", closeTime=" + closeTime +
                ", quoteAssetVolume=" + quoteAssetVolume +
                ", numTrade=" + numTrade +
                '}';
    }
}
