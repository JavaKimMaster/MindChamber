package mind.chamber.naversearch.service;

import mind.chamber.naversearch.data.NaverBlogItem;

import java.util.List;

public interface NaverSearchService {
    public String getSearchBlog( String query, String display, String start, String sort, String naverClientID, String naverClientSecret);
    public String getSearchBlog2( String query, String display, String start, String sort, String naverClientID, String naverClientSecret);
    public List<NaverBlogItem> getSearchBlogItems(String query, String display, String start, String sort, String naverClientID, String naverClientSecret);
    public List<NaverBlogItem> getSearchBlogItems2(String query, String display, String start, String sort, String naverClientID, String naverClientSecret);

}
