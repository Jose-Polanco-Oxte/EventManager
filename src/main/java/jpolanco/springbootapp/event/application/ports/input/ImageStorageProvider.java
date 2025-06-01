package jpolanco.springbootapp.event.application.ports.input;

public interface ImageStorageProvider {
    String storeImage(byte[] imageData);

    byte[] retrieveImage(String fileName);

    void deleteImage(String fileName);
}
