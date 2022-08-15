package com.example.binance.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TimeParser {
    @Value("${yearToMilli}")
    private long yearToMilli;

    public List<Long> getYears(long startTime, long endTime){
        if (startTime > endTime){
            throw new IllegalArgumentException("Start time cannot less than end time!");
        }

        List<Long> years = new ArrayList<>();

        if (startTime == endTime){
            years.add(startTime);
            years.add(endTime);
            return years;
        }

        while (startTime < endTime){
            years.add(startTime);
            startTime += yearToMilli;
        }

        years.add(endTime);
        return years;
    }
}
