package study.acquilaeventx.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RedisTestController {

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/redis/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable String eventId) {
        String key = "event:" + eventId;
        log.info("🔍 Redis에서 이벤트 조회 요청 - eventId: {}", eventId);

        Object result = redisTemplate.opsForValue().get(key);

        if (result == null) {
            log.warn("❌ Redis에 저장된 이벤트가 없습니다. (key: {})", key);
            return ResponseEntity
                    .status(404)
                    .body("해당 이벤트 ID에 대한 데이터가 Redis에 존재하지 않습니다.");
        }

        log.info("✅ Redis 조회 성공 - key: {}, value: {}", key, result);
        return ResponseEntity.ok(result);
    }
}
