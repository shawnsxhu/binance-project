package com.example.binance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

public class ConsumeInfo implements Serializable{
    String symbol;
    String timeInterval;
    Long openTime;
    Long closeTime;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Long openTime) {
        this.openTime = openTime;
    }

    public Long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    @Override
    public String toString() {
        return "ConsumeInfo{" +
                "symbol='" + symbol + '\'' +
                ", timeInterval='" + timeInterval + '\'' +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                '}';
    }

    public ConsumeInfo() {
    }

    public ConsumeInfo(String symbol, String timeInterval, Long openTime, Long closeTime) {
        this.symbol = symbol;
        this.timeInterval = timeInterval;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
