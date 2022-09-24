package com.example.binance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {

    @InjectMocks
    ValidationService validationService;

    @BeforeEach
    void setup(){
        validationService = new ValidationService();
        ReflectionTestUtils.setField(validationService, "symbolNotValidTemplate",
                "[ %s ] is not a valid symbol, please choose from\\n");
        ReflectionTestUtils.setField(validationService, "illegalTimeTemplate",
                "end time cannot be less than start time");
    }

    @Test
    void isValid() {
        //Separate test for separate cases
        String symbol1 = "BTCUSDT";
        String symbol2 = "ABCDEFG";
        Long startTime = 1523577600000L;
        Long endTime1 = 1523577600000L;
        Long endTime2 = 1523567600000L;

        validationService.isValid(symbol1, startTime, endTime1);

        Exception e1 = assertThrows(IllegalArgumentException.class,
                ()->{validationService.isValid(symbol1, startTime, endTime2);});
        Exception e2 = assertThrows(IllegalArgumentException.class,
                ()->{validationService.isValid(symbol2, startTime, endTime1);});
        String expectedE1Message = "end time cannot be less than start time";
        String expectedE2Message = "not a valid symbol";
        assertThat(e1.getMessage()).contains(expectedE1Message);
        assertThat(e2.getMessage()).contains(expectedE2Message);
    }
}