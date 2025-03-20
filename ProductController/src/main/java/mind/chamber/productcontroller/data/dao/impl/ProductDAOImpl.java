package mind.chamber.productcontroller.data.dao.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import mind.chamber.productcontroller.data.dao.ProductDAO;
import mind.chamber.productcontroller.data.entity.ProductEntity;
import mind.chamber.productcontroller.data.repository.ProductRepository;

@Service
public class ProductDAOImpl implements ProductDAO {

    ProductRepository productRepository;

    @Autowired
    public ProductDAOImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductEntity saveProduct(ProductEntity productEntity) {
        productRepository.save(productEntity);
        return productEntity;
    }

    @Override
    public ProductEntity getProduct(String productId) {
        return productRepository.getReferenceById(productId);
    }
}
