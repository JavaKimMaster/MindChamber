package study.acquilaeventx.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import study.acquilaeventx.dto.EventMessageDto;
import study.acquilaeventx.handler.EventWebSocketHandler;
import study.acquilaeventx.service.RedisService;

@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaConsumerService {

    private final RedisService redisService;
    private final EventWebSocketHandler webSocketHandler;
//    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectMapper objectMapper;  // ✅ Bean 주입받기

    // DTO로 직접 받도록 수정 + containerFactory 명시
    @KafkaListener(
            topics = "event-topic",
            groupId = "acquila-event-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(EventMessageDto message) {
        log.info("✅ Kafka 메시지 수신 → {}", message);

        // 여기에 처리 로직 추가 가능

        // 1️⃣ Redis 저장
        redisService.cacheEvent(message);
        log.info("✅ Redis 저장 완료");

        // 2️⃣ WebSocket 브로드캐스트
        try {
            String json = objectMapper.writeValueAsString(message);
            webSocketHandler.broadcast(json);
            log.info("📡 WebSocket 브로드캐스트 완료");
        } catch (Exception e) {
            log.error("❌ WebSocket 브로드캐스트 중 오류", e);
        }


    }
}

