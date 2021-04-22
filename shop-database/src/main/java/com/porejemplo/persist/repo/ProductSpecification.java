package com.porejemplo.persist.repo;

import com.porejemplo.persist.model.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> titleLike(String title) {
        return (root, query, cb) -> cb.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Product> minPrice(BigDecimal minPrice) {
        return (root, query, cb) -> cb.ge(root.get("price"), minPrice);
    }

    public static Specification<Product> maxPrice(BigDecimal maxPrice) {
        return (root, query, cb) -> cb.le(root.get("price"), maxPrice);
    }

    public static Specification<Product> byId(long id) {
        return (root, query, builder) -> builder.equal(root.get("id"), id);
    }

    public static Specification<Product> byCategory(long categoryId) {
        return (root, query, builder) -> builder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> fetchPictures() {
        return (root, query, builder) -> {
            // Don't do fetch in case of count() query for pagination
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("pictures", JoinType.LEFT);
                root.fetch("brand", JoinType.LEFT);
                query.distinct(true);
            }
            return builder.isTrue(builder.literal(true));
        };
    }
}
