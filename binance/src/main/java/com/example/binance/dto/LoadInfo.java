package com.example.binance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class LoadInfo {
    String symbol;
    Long startTime;
    Long endTime;

//    @Override
//    public String toString() {
//        return "LoadInfo{" +
//                "symbol='" + symbol + '\'' +
//                ", startTime=" + startTime +
//                ", endTime=" + endTime +
//                '}';
//    }

//    public LoadInfo(){}
//
//    public LoadInfo(String symbol, Long startTime, Long endTime) {
//        this.symbol = symbol;
//        this.startTime = startTime;
//        this.endTime = endTime;
//    }

//    public String getSymbol() {
//        return symbol;
//    }
//
//    public void setSymbol(String symbol) {
//        this.symbol = symbol;
//    }
//
//    public Long getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(Long startTime) {
//        this.startTime = startTime;
//    }
//
//    public Long getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(Long endTime) {
//        this.endTime = endTime;
//    }
}
