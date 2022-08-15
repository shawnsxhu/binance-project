package com.example.binance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ConsumeInfo implements Serializable {
    String symbol;
    String timeInterval;
    Long openTime;
    Long closeTime;
}
