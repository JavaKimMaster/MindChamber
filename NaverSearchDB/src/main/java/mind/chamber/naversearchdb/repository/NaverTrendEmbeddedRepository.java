package mind.chamber.naversearchdb.repository;

import mind.chamber.naversearchdb.entity.NaverTrendEmbeddedEntity;
import mind.chamber.naversearchdb.entity.NaverTrendEmbeddedId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaverTrendEmbeddedRepository extends JpaRepository<NaverTrendEmbeddedEntity, NaverTrendEmbeddedId> {
}
