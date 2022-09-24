package com.example.binance.controller;

import com.example.binance.dto.LoadInfo;
import com.example.binance.service.LoadService;
import com.example.binance.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BinanceControllerTest {
    @Mock
    LoadService loadService;

    @Mock
    ValidationService validationService;

    @InjectMocks
    BinanceController binanceController;

    @Captor
    ArgumentCaptor<String> symbolCaptor;

    @Captor
    ArgumentCaptor<Long> startTimeCaptor;

    @Captor
    ArgumentCaptor<Long> endTimeCaptor;

    @Captor
    ArgumentCaptor<String> loadSymbolCaptor;

    @Captor
    ArgumentCaptor<Long> loadStartTimeCaptor;

    @Captor
    ArgumentCaptor<Long> loadEndTimeCaptor;

//    @BeforeEach
//    void setup(){
//        binanceController = new BinanceController();
//    }

    @Test
    void load() {
        String symbol = "BTCUSDT";
        Long startTime = 1523577600000L;
        Long endTime = 1523577600000L;
        String id = "test";

        LoadInfo loadInfo = LoadInfo.builder()
                .symbol(symbol)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        when(loadService.loadFromBinanceApi(ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenReturn(id);

        String idExpected = binanceController.load(loadInfo, new Acknowledgment() {
            @Override
            public void acknowledge() {
            }
        });

        verify(validationService).isValid(symbolCaptor.capture(),
                startTimeCaptor.capture(), endTimeCaptor.capture());
        verify(loadService).loadFromBinanceApi(loadSymbolCaptor.capture(),
                loadStartTimeCaptor.capture(), loadEndTimeCaptor.capture());

        assertThat(symbolCaptor.getValue()).isEqualTo(loadInfo.getSymbol());
        assertThat(startTimeCaptor.getValue()).isEqualTo(loadInfo.getStartTime());
        assertThat(endTimeCaptor.getValue()).isEqualTo(loadInfo.getEndTime());

        assertThat(loadSymbolCaptor.getValue()).isEqualTo(loadInfo.getSymbol());
        assertThat(loadStartTimeCaptor.getValue()).isEqualTo(loadInfo.getStartTime());
        assertThat(loadEndTimeCaptor.getValue()).isEqualTo(loadInfo.getEndTime());

        assertThat(idExpected).isEqualTo(id);
    }
}