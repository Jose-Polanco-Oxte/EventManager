package jpolanco.springbootapp.event.infrastructure.services;

import jpolanco.springbootapp.event.infrastructure.services.interfaces.ImageStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {
    @Value("${IMAGEPATH}")
    private String IMAGE_DIRECTORY;

    @Override
    public boolean storeImage(String imageFileName, MultipartFile file) {
        try {
            Path path = Paths.get(IMAGE_DIRECTORY).resolve(imageFileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteImage(String imageFileName) {
        try {
            Path path = Paths.get(IMAGE_DIRECTORY).resolve(imageFileName);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean imageExists(String imageFileName) {
        Path path = Paths.get(IMAGE_DIRECTORY).resolve(imageFileName);
        return Files.exists(path);
    }
}
