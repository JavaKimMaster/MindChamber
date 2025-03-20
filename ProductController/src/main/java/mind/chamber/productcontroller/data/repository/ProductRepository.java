package mind.chamber.productcontroller.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mind.chamber.productcontroller.data.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

}
