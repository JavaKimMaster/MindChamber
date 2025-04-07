package mind.chamber.naversearchdb.service;

import mind.chamber.naversearchdb.data.NaverBlogItem;
import mind.chamber.naversearchdb.dto.NaverTrendRequest;
import mind.chamber.naversearchdb.dto.NaverTrendResponse;

import java.util.List;

public interface NaverSearchService {
    public List<NaverBlogItem> getSearchBlogItems(String query, String display, String start, String sort, String naverClientID, String naverClientSecret);
    public NaverTrendResponse getTrend(NaverTrendRequest requestDto, String naverClientID, String naverClientSecret);
}

