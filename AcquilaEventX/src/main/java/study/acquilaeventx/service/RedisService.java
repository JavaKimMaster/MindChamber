package study.acquilaeventx.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import study.acquilaeventx.dto.EventMessageDto;

import java.time.Duration;

@Service  // 비즈니스 로직을 담당하는 서비스 레이어로 등록
@RequiredArgsConstructor  // 생성자 주입 자동 생성
public class RedisService {

    // RedisTemplate은 Redis와의 상호작용을 처리하는 Spring 클래스
    private final RedisTemplate<String, Object> redisTemplate;

    // Redis에 메시지를 저장 (key는 eventId, value는 전체 DTO)
    public void cacheEvent(EventMessageDto dto) {
        String key = "event:" + dto.getEventId();  // 예: event:evt001 형태의 키 생성
        redisTemplate.opsForValue().set(key, dto, Duration.ofMinutes(10)); // 값 저장 및 10분 TTL 설정
    }

    // Redis에서 eventId로 데이터를 조회하는 메서드 (옵션)
    public EventMessageDto getEvent(String eventId) {
        String key = "event:" + eventId;  // 조회할 키
        Object value = redisTemplate.opsForValue().get(key);  // Redis에서 값 조회
        return value instanceof EventMessageDto ? (EventMessageDto) value : null;  // 형 변환 후 반환
    }
}
