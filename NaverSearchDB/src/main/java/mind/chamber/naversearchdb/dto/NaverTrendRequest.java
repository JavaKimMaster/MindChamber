package mind.chamber.naversearchdb.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NaverTrendRequest {
    private String startDate;   // YYYY-MM-DD
    private String endDate;
    private String timeUnit;    // date | week | month

    private List<KeywordGroup> keywordGroups;
    private String device;      // pc | mo
    private List<String> ages;  // ["1","2","3",...]
    private String gender;      // "m" | "f"

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class KeywordGroup {
        private String groupName;
        private List<String> keywords;
    }
}
