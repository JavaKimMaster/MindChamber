package study.acquilaeventx.handler;

//package study.acquilaeventx.websocket;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j  // 로그 사용을 위한 어노테이션
@RequiredArgsConstructor  // 생성자 주입을 위한 어노테이션
@Component  // Spring Bean 등록
public class EventWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper; // 전역 설정된 ObjectMapper 사용
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet(); // 접속된 클라이언트 세션 저장소

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);  // 새로운 세션 등록
        log.info("🟢 WebSocket 연결됨: {}", session.getId());  // 연결 로그 출력
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("📩 클라이언트로부터 메시지 수신: {}", message.getPayload());  // 클라이언트 메시지 수신 로그
        // 일반적으로 클라이언트가 메시지를 보내는 경우는 없음 → 무시
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);  // 세션 종료 시 목록에서 제거
        log.info("🔴 WebSocket 연결 종료: {}", session.getId());  // 종료 로그 출력
    }

    // 👉 외부에서 호출하여 모든 연결된 클라이언트에게 메시지를 브로드캐스트
    public void broadcast(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {  // 연결이 살아있는 경우만 전송
                try {
                    session.sendMessage(new TextMessage(message));  // 메시지 전송
                } catch (Exception e) {
                    log.error("❌ WebSocket 메시지 전송 오류", e);  // 에러 로그 출력
                }
            }
        }
    }
}
