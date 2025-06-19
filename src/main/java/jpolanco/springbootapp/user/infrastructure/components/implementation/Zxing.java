package jpolanco.springbootapp.user.infrastructure.components.implementation;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.infrastructure.components.utils.QRToSVG;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class Zxing implements QRProvider {

    @Value("${QRPATH}")
    private String contextPath;

    @Override
    public void generate(String fileName, String content) throws RuntimeException {
        fileName = fileName + ".png";
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 1000; // ancho del QR
        int height = 1000; // alto del QR
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
            Path path = FileSystems.getDefault().getPath(contextPath + fileName);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            String svg = QRToSVG.generateSVG(content, width, height);
            String svgFileName = fileName.replace(".png", ".svg");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(contextPath + svgFileName))) {
                writer.write(svg);
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar el archivo SVG", e);
            }
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error al generar el código QR", e);
        }
    }

    @Override
    public boolean exist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    @Override
    public void delete(String fileName) throws RuntimeException {
        try {
            Files.deleteIfExists(FileSystems.getDefault().getPath(contextPath + fileName + ".png"));
            Files.deleteIfExists(FileSystems.getDefault().getPath(contextPath + fileName + ".svg"));
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo QR", e);
        }
    }
}