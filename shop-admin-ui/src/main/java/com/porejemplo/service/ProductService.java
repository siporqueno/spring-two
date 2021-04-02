package com.porejemplo.service;

import com.porejemplo.error.NotFoundException;
import com.porejemplo.controller.repr.ProductRepr;
import com.porejemplo.persist.model.Picture;
import com.porejemplo.persist.model.Product;
import com.porejemplo.persist.repo.ProductRepository;
import com.porejemplo.persist.repo.ProductSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements ItemService<ProductRepr> {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final PictureService pictureService;

    @Autowired
    public ProductService(ProductRepository productRepository, PictureService pictureService) {
        this.productRepository = productRepository;
        this.pictureService = pictureService;
    }

    @Transactional
    @Override
    public List<ProductRepr> findAll() {
        return productRepository.findAll().stream()
                .map(ProductRepr::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductRepr> findWithFilter(String likeTitle, BigDecimal minPrice, BigDecimal maxPrice, Integer page, Integer size, String sortField) {
        Specification<Product> spec = Specification.where(null);

        if (likeTitle != null && !likeTitle.isBlank()) {
            spec = spec.and(ProductSpecification.titleLike(likeTitle));
        }

        if (minPrice != null) {
            spec = spec.and(ProductSpecification.minPrice(minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and(ProductSpecification.maxPrice(maxPrice));
        }

        if (sortField != null && !sortField.isBlank()) {
            return productRepository.findAll(spec, PageRequest.of(page, size, Sort.by(sortField)))
                    .map(ProductRepr::new);
        }

        return productRepository.findAll(spec, PageRequest.of(page, size))
                .map(ProductRepr::new);
    }

    @Transactional
    @Override
    public Optional<ProductRepr> findById(long id) {
        return productRepository.findById(id)
                .map(ProductRepr::new);
    }

    @Transactional
    @Override
    public void save(ProductRepr productRepr) throws IOException{
        Product product = (productRepr.getId() != null) ? productRepository.findById(productRepr.getId())
                .orElseThrow(NotFoundException::new) : new Product();
        product.setTitle(productRepr.getTitle());
        product.setCategory(productRepr.getCategory());
        product.setBrand(productRepr.getBrand());
        product.setPrice(productRepr.getPrice());

        if (productRepr.getNewPictures() != null) {
            for (MultipartFile newPicture : productRepr.getNewPictures()) {
                logger.info("Product {} file {} size {} contentType {}", productRepr.getId(),
                        newPicture.getOriginalFilename(), newPicture.getSize(), newPicture.getContentType());

                if (product.getPictures() == null) {
                    product.setPictures(new ArrayList<>());
                }

                product.getPictures().add(new Picture(
                        newPicture.getOriginalFilename(),
                        newPicture.getContentType(),
                        pictureService.createPictureData(newPicture.getBytes()),
                        product
                ));
            }
        }

        productRepository.save(product);
    }

    @Transactional
    @Override
    public void delete(long id) {
        productRepository.deleteById(id);
    }

}
