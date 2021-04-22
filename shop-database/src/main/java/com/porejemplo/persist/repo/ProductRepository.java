package com.porejemplo.persist.repo;

import com.porejemplo.persist.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("select distinct p " +
            "from Product p " +
            "left join fetch p.pictures " +
            "inner join fetch p.category " +
            "inner join fetch p.brand")
    List<Product> findAllWithPictureCategoryBrandFetch();

    @Query("select distinct p " +
            " from Product p " +
            " left join fetch p.pictures " +
            " inner join fetch p.category " +
            " inner join fetch p.brand " +
            "where p.id in (:ids)")
    List<Product> findAllByIds(@Param("ids") List<Long> ids);
}
