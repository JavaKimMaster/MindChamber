package mind.chamber.naversearchdb.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaverTrendEmbeddedId implements Serializable {

    private String startDate;   // 요청 startDate
    private String endDate;     // 요청 endDate
    private String timeUnit;    // 요청 timeUnit
    private String groupTitle;  // 응답 title (그룹명)
    private String keyword;     // keywords 배열의 keyword
    private String period;      // data 배열의 period
}
