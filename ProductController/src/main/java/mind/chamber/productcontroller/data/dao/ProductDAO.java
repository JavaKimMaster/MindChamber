package mind.chamber.productcontroller.data.dao;

import mind.chamber.productcontroller.data.entity.ProductEntity;

public interface ProductDAO {

    ProductEntity saveProduct(ProductEntity product);

    ProductEntity getProduct(String productId);
}
