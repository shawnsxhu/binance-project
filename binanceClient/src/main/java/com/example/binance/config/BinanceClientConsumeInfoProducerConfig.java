package com.example.binance.config;

import com.example.binance.dto.ConsumeInfo;
import com.example.binance.dto.LoadInfo;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class BinanceClientConsumeInfoProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, ConsumeInfo> consumeInfoProducerFactory() {
        return new DefaultKafkaProducerFactory<>(consumeInfoProducerConfigs());
    }

    @Bean
    public Map<String, Object> consumeInfoProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, ConsumeInfo> kafkaConsumerTemplate() {
        return new KafkaTemplate<String, ConsumeInfo>(consumeInfoProducerFactory());
    }
}
