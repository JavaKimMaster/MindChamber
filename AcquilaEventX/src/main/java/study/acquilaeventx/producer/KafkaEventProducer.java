package study.acquilaeventx.producer;

//import com.example.eventx.dto.EventMessageDto;

import study.acquilaeventx.dto.EventMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component  // Spring Bean으로 등록
@RequiredArgsConstructor  // 생성자 주입을 자동으로 생성
public class KafkaEventProducer {

    // Kafka 메시지를 보내는 객체 (Spring에서 자동 주입)
    private final KafkaTemplate<String, EventMessageDto> kafkaTemplate;

    // 토픽명과 메시지를 받아 Kafka로 전송
    public void sendEvent(String topic, EventMessageDto message) {
        kafkaTemplate.send(topic, message);  // Kafka에 메시지 전송
        System.out.println("✅ Kafka로 메시지를 보냈습니다 → topic: " + topic + ", payload: " + message);  // 콘솔 로그 출력
    }
}
