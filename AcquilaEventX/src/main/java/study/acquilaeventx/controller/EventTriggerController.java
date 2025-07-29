package study.acquilaeventx.controller;

import study.acquilaeventx.dto.EventMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.acquilaeventx.producer.KafkaEventProducer;

// EventTriggerController.java는 POST 요청을 받아서 Kafka로 메시지를 보내는 역할을 합니다.

@RestController  // 이 클래스는 REST API 컨트롤러로 동작합니다
@RequestMapping("/api/event")  // 기본 URL 경로 설정: /api/event
@RequiredArgsConstructor  // 생성자 주입을 위한 Lombok 어노테이션
public class EventTriggerController {

    private final KafkaEventProducer producer;  // Kafka 메시지 전송을 담당할 프로듀서

    @PostMapping  // HTTP POST 요청 처리
    public ResponseEntity<String> sendEvent(@RequestBody EventMessageDto dto) {
        // 수신한 DTO를 Kafka에 전송
        producer.sendEvent("event-topic", dto);
        // 클라이언트에 성공 응답 반환
        return ResponseEntity.ok("✅ Kafka에 이벤트 전송 완료!");
    }
}
