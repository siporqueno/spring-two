package com.porejemplo.service;

import com.porejemplo.controller.repr.ProductRepr;
import com.porejemplo.persist.model.Picture;
import com.porejemplo.persist.model.Product;
import com.porejemplo.persist.repo.ProductRepository;
import com.porejemplo.persist.repo.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductRepr> findAll() {
        return productRepository.findAll().stream()
                .map(ProductServiceImpl::mapToRepr)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductRepr> findById(Long id) {
        return productRepository.findById(id)
                .map(ProductServiceImpl::mapToRepr);
    }

    @Override
    public Page<ProductRepr> findByFilter(Long categoryId, Integer page, Integer size) {
        Specification<Product> spec = Specification.where(null); // ProductSpecification.fetchPictures();
        if (categoryId != null) {
            spec = spec.and(ProductSpecification.byCategory(categoryId));
        }
        Page<Long> ids = productRepository.findAll(spec, PageRequest.of(page - 1, size))
                .map(Product::getId);

        List<ProductRepr> allByIds = productRepository.findAllByIds(ids.getContent()).stream()
                .map(ProductServiceImpl::mapToRepr)
                .collect(Collectors.toList());
        return new PageImpl<>(allByIds, PageRequest.of(page - 1, size), ids.getTotalElements());
    }

    private static ProductRepr mapToRepr(Product p) {
        return new ProductRepr(
                p.getId(),
                p.getTitle(),
                p.getDescription(),
                p.getPrice(),
                p.getCategory().getName(),
                p.getBrand().getName(),
                p.getPictures().size() > 0 ? p.getPictures().get(0).getId() : null,
                p.getPictures().stream().map(Picture::getId).collect(Collectors.toList())
        );
    }
}
