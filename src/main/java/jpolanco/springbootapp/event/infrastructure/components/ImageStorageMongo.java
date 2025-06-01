package jpolanco.springbootapp.event.infrastructure.components;

import jpolanco.springbootapp.event.application.ports.input.ImageStorageProvider;
import org.springframework.stereotype.Component;

@Component
public class ImageStorageMongo implements ImageStorageProvider {
    @Override
    public String storeImage(byte[] imageData) {
        return "";
    }

    @Override
    public byte[] retrieveImage(String fileName) {
        return new byte[0];
    }

    @Override
    public void deleteImage(String fileName) {

    }
}
