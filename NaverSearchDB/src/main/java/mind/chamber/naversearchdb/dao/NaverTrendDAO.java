package mind.chamber.naversearchdb.dao;

import mind.chamber.naversearchdb.entity.NaverTrendEntity;

import java.util.List;


public interface NaverTrendDAO {
    boolean saveTrendData(List<NaverTrendEntity> trendList);
}