package com.porejemplo.service;

import com.porejemplo.persist.model.Picture;
import com.porejemplo.persist.model.PictureData;
import com.porejemplo.persist.model.Product;
import com.porejemplo.persist.repo.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PictureServiceBlobImpl implements PictureService {

    private final PictureRepository repository;

    @Autowired
    public PictureServiceBlobImpl(PictureRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<String> getPictureContentTypeById(long id) {
        return repository.getContentTypeForBlob(id);
    }
    
    @Override
    public Optional<byte[]> getPictureDataById(long id) {
        return repository.getDataForBlob(id);
    }

    @Override
    public PictureData createPictureData(byte[] picture) {
        return new PictureData(picture);
    }

    @Override
    public Optional<Product> getProductByPictureId(long id) {
        return repository.findById(id)
                .map(Picture::getProduct);
    }

    @Override
    @Transactional
    public void removePicture(long id) {
        repository.deleteById(id);
    }
}
