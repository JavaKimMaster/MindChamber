package mind.chamber.naversearchdb.dao.impl;

import mind.chamber.naversearchdb.dao.NaverBlogDAO;
import mind.chamber.naversearchdb.entity.NaverBlogEntity;
import mind.chamber.naversearchdb.repository.NaverBlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NaverBlogDAOImpl implements NaverBlogDAO {

    NaverBlogRepository naverBlogRepository;

    @Autowired
    public void NaverBlogDAOImpl(NaverBlogRepository naverBlogRepository) {
        this.naverBlogRepository = naverBlogRepository;
    }

    @Override
    public boolean saveBlogItem(NaverBlogEntity naverBlogEntity) {
        naverBlogRepository.save(naverBlogEntity);
        return true;
    }
}
