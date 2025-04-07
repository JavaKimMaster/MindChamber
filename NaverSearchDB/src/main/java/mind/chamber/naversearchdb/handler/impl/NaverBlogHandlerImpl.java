package mind.chamber.naversearchdb.handler.impl;

import jakarta.transaction.Transactional;
import mind.chamber.naversearchdb.dao.NaverBlogDAO;
import mind.chamber.naversearchdb.entity.NaverBlogEntity;
import mind.chamber.naversearchdb.data.NaverBlogItem;
import mind.chamber.naversearchdb.handler.NaverBlogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class NaverBlogHandlerImpl implements NaverBlogHandler {

    NaverBlogDAO naverBlogDAO;

    @Autowired
    public NaverBlogHandlerImpl(NaverBlogDAO naverBlogDAO) {
        this.naverBlogDAO = naverBlogDAO;
    }
    public boolean saveBlogItem(List<NaverBlogItem> itemList) {

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        for (int i = 0; i < itemList.size(); i++) {
            NaverBlogItem item = itemList.get(i);

            NaverBlogEntity entity = NaverBlogEntity.builder()
                    .id(now)
                    .idseq(String.valueOf(i + 1))
                    .title(item.getTitle())
                    .link(item.getLink())
                    .description(item.getDescription())
                    .bloggername(item.getBloggername())
                    .bloggerlink(item.getBloggerlink())
                    .postdate(item.getPostdate())
                    .build();

            naverBlogDAO.saveBlogItem(entity);
        }
        return true;
    }
}
