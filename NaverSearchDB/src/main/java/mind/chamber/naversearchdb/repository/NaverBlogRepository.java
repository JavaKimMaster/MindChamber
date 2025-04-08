package mind.chamber.naversearchdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mind.chamber.naversearchdb.entity.NaverBlogEntity;

public interface NaverBlogRepository extends JpaRepository<NaverBlogEntity, String> {
}
