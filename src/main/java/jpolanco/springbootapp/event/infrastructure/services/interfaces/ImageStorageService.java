package jpolanco.springbootapp.event.infrastructure.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    /**
     * Stores an image file in the specified directory.
     *
     * @param imageFileName the name of the image file to be stored
     * @param file the MultipartFile containing the image data
     * @return true if the image was successfully stored, false otherwise
     */
    boolean storeImage(String imageFileName, MultipartFile file);

    /**
     * Deletes an image file from the storage.
     *
     * @param imageFileName the name of the image file to be deleted
     * @return true if the image was successfully deleted, false otherwise
     */
    boolean deleteImage(String imageFileName);

    /**
     * Checks if an image file exists in the storage.
     * @param imageFileName the name of the image file to check
     */
    boolean imageExists(String imageFileName);
}
