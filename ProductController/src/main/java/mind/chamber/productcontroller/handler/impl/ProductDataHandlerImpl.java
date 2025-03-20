package mind.chamber.productcontroller.handler.impl;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import mind.chamber.productcontroller.handler.ProductDataHandler;
import mind.chamber.productcontroller.data.dao.ProductDAO;
import mind.chamber.productcontroller.data.entity.ProductEntity;

@Service
@Transactional
public class ProductDataHandlerImpl implements ProductDataHandler {

    ProductDAO productDAO;

    @Autowired
    public ProductDataHandlerImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public ProductEntity saveProductEntity(
            String productId, String productName, int productPrice, int productStock) {

        ProductEntity productEntity = new ProductEntity(productId, productName, productPrice, productStock);

        return productDAO.saveProduct(productEntity);
    }

    @Override
    public ProductEntity getProductEntity(String productId) {
        return productDAO.getProduct(productId);
    }
}
