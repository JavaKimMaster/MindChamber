package study.acquilaeventx.config;




import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.*;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import study.acquilaeventx.dto.EventMessageDto;

import java.util.*;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    // ✅ DTO 역직렬화 및 LocalDateTime 처리를 위한 ObjectMapper 설정
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // LocalDateTime 지원
        return mapper;
    }

    // ✅ ConsumerFactory 설정: Kafka로부터 EventMessageDto 수신
    @Bean
    public ConsumerFactory<String, EventMessageDto> consumerFactory(ObjectMapper objectMapper) {
        JsonDeserializer<EventMessageDto> deserializer = new JsonDeserializer<>(EventMessageDto.class, objectMapper);
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "acquila-event-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    // ✅ Kafka Listener Factory 등록
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventMessageDto> kafkaListenerContainerFactory(
            ConsumerFactory<String, EventMessageDto> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, EventMessageDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
