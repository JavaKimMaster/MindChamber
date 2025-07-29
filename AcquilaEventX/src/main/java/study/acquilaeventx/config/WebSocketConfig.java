package study.acquilaeventx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import study.acquilaeventx.handler.EventWebSocketHandler;
import lombok.RequiredArgsConstructor;

@Configuration // 스프링 설정 클래스임을 나타냄
@EnableWebSocket // WebSocket 기능을 활성화
@RequiredArgsConstructor // final 필드를 생성자로 주입
public class WebSocketConfig implements WebSocketConfigurer {

    // WebSocket 핸들러 주입
    private final EventWebSocketHandler eventWebSocketHandler;

    // WebSocket 핸들러 등록 메서드
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // "/ws/event" 엔드포인트로 WebSocket 연결 허용
        registry.addHandler(eventWebSocketHandler, "/ws/event")
                .setAllowedOrigins("*");  // 모든 도메인에서의 접근 허용 (테스트용 CORS 설정)
    }
}
