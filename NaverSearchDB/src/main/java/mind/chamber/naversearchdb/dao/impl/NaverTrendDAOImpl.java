package mind.chamber.naversearchdb.dao.impl;

import mind.chamber.naversearchdb.dao.NaverTrendDAO;
import mind.chamber.naversearchdb.entity.NaverTrendEntity;
import mind.chamber.naversearchdb.repository.NaverTrendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NaverTrendDAOImpl implements NaverTrendDAO {

    NaverTrendRepository naverTrendRepository;

    @Autowired
    public void NaverTrendDAOImpl(NaverTrendRepository naverTrendRepository) {
        this.naverTrendRepository = naverTrendRepository;
    }

    @Override
    public boolean saveTrendData(List<NaverTrendEntity> trendList) {
        try {
            naverTrendRepository.saveAll(trendList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
