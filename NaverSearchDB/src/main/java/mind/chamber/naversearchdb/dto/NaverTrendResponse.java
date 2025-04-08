package mind.chamber.naversearchdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaverTrendResponse {

    private String startDate;
    private String endDate;
    private String timeUnit;
    private List<Result> results;

    @Data
    public static class Result {
        private String title;
        private List<String> keywords;
        private List<DataPoint> data;

        @Data
        public static class DataPoint {
            private String period;
            private double ratio;
        }
    }
}
