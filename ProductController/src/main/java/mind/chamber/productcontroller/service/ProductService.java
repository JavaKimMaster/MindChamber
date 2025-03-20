package mind.chamber.productcontroller.service;

import mind.chamber.productcontroller.data.dto.ProductDto;

public interface ProductService {
    ProductDto saveProduct(String productId, String productName, int productPrice, int productStock);

    ProductDto getProduct(String productId);
}

