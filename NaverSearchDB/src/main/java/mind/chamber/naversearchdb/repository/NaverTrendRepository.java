package mind.chamber.naversearchdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mind.chamber.naversearchdb.entity.NaverTrendEntity;

public interface NaverTrendRepository extends JpaRepository<NaverTrendEntity, Long> {
}
