package study.acquilaeventx.config;

import study.acquilaeventx.dto.EventMessageDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


// ✅ 완료 후 체크리스트
// KafkaEventProducer.java가 정상 작동할 수 있음
// Kafka 메시지 전송 시, KafkaTemplate.send()를 통해 event-topic으로 DTO 전송 가능

@Configuration
public class KafkaConfig {

    // 1. ProducerFactory 설정 → Kafka로 보낼 때 사용할 직렬화 설정
    @Bean
    public ProducerFactory<String, EventMessageDto> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka 서버 주소
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // key는 문자열
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // value는 JSON (DTO)

        return new DefaultKafkaProducerFactory<>(config);
    }

    // 2. KafkaTemplate Bean 등록 → Kafka로 메시지를 보낼 때 사용
    @Bean
    public KafkaTemplate<String, EventMessageDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
