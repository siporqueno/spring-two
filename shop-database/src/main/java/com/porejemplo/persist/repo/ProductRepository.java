package com.porejemplo.persist.repo;

import com.porejemplo.persist.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


//        "milk","3.2%",  50
//        "cheese","Gauda", 200
//        "meat", "pork",500

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("select p from Product p left join fetch p.pictures")
    List<Product> findAllWithPictureFetch();

}
