package mind.chamber.hellocontroller.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class HelloController {

    @RequestMapping(value = "/hello")
    public String hello() {
        Date myDate = new Date();
        return ("Hello World ~!!" + myDate.toString());
    }
}
