package study.hellopostconstruction.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.hellopostconstruction.sample.SamplePostConstructBean;

@RestController
public class MainController {

    private final SamplePostConstructBean sample;

    public MainController(SamplePostConstructBean sample) {
        this.sample = sample;
    }

    @GetMapping("/run")
    public String run() {
        sample.doWork();
        return "작업 완료!";
    }
}