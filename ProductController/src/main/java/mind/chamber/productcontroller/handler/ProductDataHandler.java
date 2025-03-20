package mind.chamber.productcontroller.handler;

import mind.chamber.productcontroller.data.entity.ProductEntity;

public interface ProductDataHandler {

    ProductEntity saveProductEntity(
            String productId, String productName, int productPrice, int productStock);

    ProductEntity getProductEntity(String productId);
}
