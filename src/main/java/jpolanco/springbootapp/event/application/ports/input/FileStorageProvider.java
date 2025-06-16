package jpolanco.springbootapp.event.application.ports.input;

import java.io.InputStream;

public interface FileStorageProvider {
    String storeImage(String fileName, InputStream inputStream);

    boolean deleteImage(String fileName);

    boolean exists(String fileName);
}
