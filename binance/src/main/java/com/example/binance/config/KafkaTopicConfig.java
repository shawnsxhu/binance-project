package com.example.binance.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("binanceTopic")
                .partitions(10)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "60000")
                .build();
    }

    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name("binanceConsumerTopic")
                .partitions(5)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "60000")
                .build();
    }
}
