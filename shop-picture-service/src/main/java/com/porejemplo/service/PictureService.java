package com.porejemplo.service;

import com.porejemplo.persist.model.Picture;
import com.porejemplo.persist.model.PictureData;
import com.porejemplo.persist.model.Product;

import java.util.List;
import java.util.Optional;

public interface PictureService {

    Optional<String> getPictureContentTypeById(long id);

    Optional<byte[]> getPictureDataById(long id);

    PictureData createPictureData(byte[] picture);

    Optional<Product> getProductByPictureId(long id);

    void removePicture(long id);

    List<Picture> findPicturesByIds(List<Long> pictureIds);
}
