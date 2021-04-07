package com.porejemplo.persist.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.porejemplo.persist.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
