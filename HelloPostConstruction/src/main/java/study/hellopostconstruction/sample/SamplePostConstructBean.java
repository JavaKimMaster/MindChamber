package study.hellopostconstruction.sample;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class SamplePostConstructBean {

    public SamplePostConstructBean() {
        System.out.println("🧱 생성자 호출");
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ PostConstruct 메서드 호출 - 초기화 작업 수행");
    }

    public void doWork() {
        System.out.println("🛠 실제 로직 실행");
    }
}
