package mind.chamber.productcontroller.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import mind.chamber.productcontroller.service.ProductService;
import mind.chamber.productcontroller.handler.ProductDataHandler;
import mind.chamber.productcontroller.data.dto.ProductDto;
import mind.chamber.productcontroller.data.entity.ProductEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    ProductDataHandler productDataHandler;

    @Autowired
    public ProductServiceImpl(ProductDataHandler productDataHandler) {
        this.productDataHandler = productDataHandler;
    }

    @Override
    public ProductDto saveProduct(
            String productId, String productName, int productPrice, int productStock) {
        ProductEntity productEntity =
                productDataHandler.saveProductEntity(productId, productName, productPrice, productStock);

        ProductDto productDto =
                new ProductDto(productEntity.getProductId(),
                        productEntity.getProductName(),
                        productEntity.getProductPrice(),
                        productEntity.getProductStock());

        return productDto;
    }

    @Override
    public ProductDto getProduct(String productId) {
        ProductEntity productEntity = productDataHandler.getProductEntity(productId);

        ProductDto productDto =
                new ProductDto(productEntity.getProductId(),
                        productEntity.getProductName(),
                        productEntity.getProductPrice(),
                        productEntity.getProductStock());

        return productDto;
    }
}
