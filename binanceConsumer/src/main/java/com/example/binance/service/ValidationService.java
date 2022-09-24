package com.example.binance.service;

import com.example.binance.util.Symbol;
import com.example.binance.util.TimeInterval;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class ValidationService {
    @Value("${SYMBOL_NOT_VALID_TEMPLATE}")
    private String symbolNotValidTemplate;

    @Value("${TIME_INTERVAL_NOT_VALID_TEMPLATE}")
    private String timeIntervalNotValidTemplate;

    @Value("${ILLEGAL_TIME_TEMPLATE}")
    private String illegalTimeTemplate;

    public void isValid(@NotNull String symbol, Long startTime, Long endTime, String timeInterval){
        boolean isValidSymbol = EnumUtils.isValidEnum(Symbol.class, symbol);
        boolean isValidTimeInterval = false;
        for (TimeInterval timeIntervals : TimeInterval.values()) {
            if (timeIntervals.getInterval().equalsIgnoreCase(timeInterval)) {
                isValidTimeInterval = true;
                break;
            }
        }

        if (!isValidSymbol) {
            StringBuilder msg = new StringBuilder(String.format(symbolNotValidTemplate, symbol));
            for (Symbol sym : Symbol.values()){
                msg.append(sym).append("\n");
            }
            throw new IllegalArgumentException(msg.toString());
        }

        if (!isValidTimeInterval) {
            StringBuilder msg = new StringBuilder(String.format(timeIntervalNotValidTemplate, timeInterval));
            for (TimeInterval interval : TimeInterval.values()){
                msg.append(interval).append("\n");
            }
            throw new IllegalArgumentException(msg.toString());
        }

        if(endTime < startTime) {
            throw new IllegalArgumentException(illegalTimeTemplate);
        }

        //TODO limit return data to 1G per time
        //pagination
    }
}
