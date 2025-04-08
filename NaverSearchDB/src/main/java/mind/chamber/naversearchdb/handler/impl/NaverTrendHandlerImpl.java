package mind.chamber.naversearchdb.handler.impl;

import jakarta.transaction.Transactional;
import mind.chamber.naversearchdb.dao.NaverTrendDAO;
import mind.chamber.naversearchdb.dto.NaverTrendResponse;
import mind.chamber.naversearchdb.entity.NaverTrendEmbeddedEntity;
import mind.chamber.naversearchdb.entity.NaverTrendEntity;
import mind.chamber.naversearchdb.entity.NaverTrendEmbeddedId;
import mind.chamber.naversearchdb.handler.NaverTrendHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NaverTrendHandlerImpl implements NaverTrendHandler {

    NaverTrendDAO naverTrendDAO;

    @Autowired
    public void NaverTrendHandlerImpl(NaverTrendDAO naverTrendDAO) {
        this.naverTrendDAO = naverTrendDAO;
    }

    @Override
    public boolean saveTrendData(List<NaverTrendResponse.Result> results) {
        try {
            List<NaverTrendEntity> trendList = new ArrayList<>();

            for (NaverTrendResponse.Result result : results) {
                String groupTitle = result.getTitle();
                for (String keyword : result.getKeywords()) {
                    for (NaverTrendResponse.Result.DataPoint dataPoint : result.getData()) {
                        NaverTrendEntity entity = NaverTrendEntity.builder()
                                .groupTitle(groupTitle)
                                .keyword(keyword)
                                .period(dataPoint.getPeriod())
                                .ratio(dataPoint.getRatio())
                                .build();
                        trendList.add(entity);
                    }
                }
            }

            boolean bSave =  naverTrendDAO.saveTrendData(trendList);

            if (bSave) { return true; }
            else { return false; }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveTrendData2(NaverTrendResponse responseBody) {
        try {
            List<NaverTrendEmbeddedEntity> trendEmbeddedList = new ArrayList<>();

            String startDate = responseBody.getStartDate();
            String endDate = responseBody.getEndDate();
            String timeUnit = responseBody.getTimeUnit();

            for (NaverTrendResponse.Result result : responseBody.getResults()) {
                String groupTitle = result.getTitle();
                for (String keyword : result.getKeywords()) {
                    for (NaverTrendResponse.Result.DataPoint dataPoint : result.getData()) {

                        NaverTrendEmbeddedId id = new NaverTrendEmbeddedId(
                                startDate,
                                endDate,
                                timeUnit,
                                groupTitle,
                                keyword,
                                dataPoint.getPeriod()
                        );

                        NaverTrendEmbeddedEntity entity = NaverTrendEmbeddedEntity.builder()
                                .id(id)
                                .ratio(dataPoint.getRatio())
                                .build();

                        trendEmbeddedList.add(entity);
                    }
                }
            }

            boolean bSave =  naverTrendDAO.saveTrendData2(trendEmbeddedList);

            if (bSave) { return true; }
            else { return false; }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
