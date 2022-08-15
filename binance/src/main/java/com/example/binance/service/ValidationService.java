package com.example.binance.service;

import com.example.binance.util.Symbol;
import com.sun.istack.NotNull;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    @Value("${SYMBOL_NOT_VALID_TEMPLATE}")
    private String symbolNotValidTemplate;

    @Value("${ILLEGAL_TIME_TEMPLATE}")
    private String illegalTimeTemplate;

    public void isValid(@NotNull String symbol, Long startTime, Long endTime){
        boolean isValidSymbol = EnumUtils.isValidEnum(Symbol.class, symbol);
        if (!isValidSymbol) {
            //TODO mention illegal symbol, enumerate legal symbols
            StringBuilder msg = new StringBuilder(String.format(symbolNotValidTemplate, symbol));
            for (Symbol sym : Symbol.values()){
                msg.append(sym).append("\n");
            }
            throw new IllegalArgumentException(msg.toString());
        }
        //TODO mention what is wrong with current time
        if(endTime < startTime) {
            throw new IllegalArgumentException(illegalTimeTemplate);
        }
    }
}
