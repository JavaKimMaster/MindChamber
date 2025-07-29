package study.acquilaeventx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data  // Getter, Setter, toString, equals, hashCode 자동 생성
@NoArgsConstructor  // 기본 생성자 생성
@AllArgsConstructor  // 모든 필드를 매개변수로 받는 생성자 생성
public class EventMessageDto {
    private String eventId;             // 이벤트 ID
    private String message;             // 전달할 메시지 내용
    private String type;                // 이벤트 유형 (예: alert, info 등)
    private LocalDateTime timestamp;   // 이벤트 발생 시간
}
