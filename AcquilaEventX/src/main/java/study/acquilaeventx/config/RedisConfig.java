package study.acquilaeventx.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    // Redis 연결을 위한 Lettuce 커넥션 팩토리 빈 등록
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    // RedisTemplate 빈 등록 → Redis 연동 시 사용
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        // Jackson ObjectMapper 설정 (LocalDateTime 직렬화 지원)
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());  // JSR310 모듈 등록

        // JSON 직렬화를 위한 Redis Serializer 설정
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);

        // 키와 해시 키는 문자열로 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // 값과 해시 값은 Jackson을 사용하여 JSON 직렬화
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        // 설정 적용
        template.afterPropertiesSet();
        return template;
    }
}
