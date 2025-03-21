package mind.chamber.resttemplatecontroller.service;

import org.springframework.http.ResponseEntity;
import mind.chamber.resttemplatecontroller.dto.MemberDTO;

public interface RestTemplateService {

    public String getMindChamber();
    public String getName();
    public String getName2();
    public ResponseEntity<MemberDTO> postDto();
    public ResponseEntity<MemberDTO> addHeader();
}
