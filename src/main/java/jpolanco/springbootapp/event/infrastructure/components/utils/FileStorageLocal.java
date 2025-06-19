package jpolanco.springbootapp.event.infrastructure.components.utils;

import jpolanco.springbootapp.event.application.ports.input.providers.FileStorageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileStorageLocal implements FileStorageProvider {
    @Value("${IMAGEPATH}")
    private String IMAGE_DIRECTORY;

    @Override
    public String storeImage(String fileName, InputStream inputStream) {
        Path path = Paths.get(IMAGE_DIRECTORY).resolve(fileName);
        try {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return path.toString();
    }

    @Override
    public boolean deleteImage(String fileName) {
        Path path = Paths.get(IMAGE_DIRECTORY).resolve(fileName);
        try {
            Files.deleteIfExists(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean exists(String fileName) {
        Path path = Paths.get(IMAGE_DIRECTORY).resolve(fileName);
        return Files.exists(path);
    }
}
