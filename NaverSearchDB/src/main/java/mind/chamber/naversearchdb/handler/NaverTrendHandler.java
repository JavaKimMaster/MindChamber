package mind.chamber.naversearchdb.handler;

import com.fasterxml.jackson.databind.JsonNode;
import mind.chamber.naversearchdb.dto.NaverTrendResponse;
import mind.chamber.naversearchdb.entity.NaverTrendEntity;

import java.util.List;

public interface NaverTrendHandler {
    boolean saveTrendData(List<NaverTrendResponse.Result> results);
}
