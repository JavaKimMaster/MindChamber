package mind.chamber.authserver.controller;

import mind.chamber.authserver.dto.MemberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/server")
public class AuthController {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @GetMapping(value = "/mind-chamber")
    public String GetTest1() {
        LOGGER.info("GetTest1 호출");
        return "Hello. Mind Chamber!";
    }

    @GetMapping(value = "/name")
    public String GetTest2(@RequestParam String name) {
        LOGGER.info("GetTest2 호출");
        return "Hello World " + name + " !!";
    }

    @GetMapping(value = "/path-variable/{name}")
    public String GetTest3(@PathVariable String name) {
        LOGGER.info("GetTest3 호출");
        return "Hello World " + name + " !!!";
    }

    @PostMapping(value="/member")
    public ResponseEntity<MemberDTO> GetMember
            (@RequestBody MemberDTO memberDTO,
             @RequestParam String name,
             @RequestParam String email,
             @RequestParam String org
             )
    {
        LOGGER.info("GetMember 4 호출");

        LOGGER.info(memberDTO.toString());
        LOGGER.info("Requested name:" + name);
        LOGGER.info("Requested email:" + email);
        LOGGER.info("Requested org:" + org);

        return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
    }

    @PostMapping(value = "/add-header")
    public ResponseEntity<MemberDTO> addHeader(
            @RequestHeader("Authentification") String header,
            @RequestBody MemberDTO memberDTO) {
        LOGGER.info("add-header 5 호출");
        LOGGER.info("header 6 값 : {}", header);

        return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
    }
}
