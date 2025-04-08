package mind.chamber.naversearchdb.handler;

import mind.chamber.naversearchdb.data.NaverBlogItem;

import java.util.List;

public interface NaverBlogHandler {
    boolean saveBlogItem(List<NaverBlogItem> itemList);
}
