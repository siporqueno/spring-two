package com.porejemplo.persist.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.porejemplo.persist.model.Picture;

public interface PictureRepository extends JpaRepository<Picture, Long> {
}
