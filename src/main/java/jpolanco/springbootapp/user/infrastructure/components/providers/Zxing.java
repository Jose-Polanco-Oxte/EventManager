package jpolanco.springbootapp.user.infrastructure.components.providers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jpolanco.springbootapp.shared.infrastructure.errors.ProviderException;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.utils.QRResult;
import jpolanco.springbootapp.user.infrastructure.components.utils.QRToSVG;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

@Component
@RequiredArgsConstructor
public class Zxing implements QRProvider {

    @Value("${QRPATH}")
    private String contextPath;

    @Override
    public QRResult generate(String content) throws RuntimeException {
        int width = 512;
        int height = 512;
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

            // BufferedImage generado en memoria
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // SVG generado en memoria
            String svg = QRToSVG.generateSVG(content, width, height);

            // Devolver el record con ambos formatos, sin guardar en disco
            return new QRResult(svg, bufferedImage);

        } catch (WriterException e) {
            throw new ProviderException("Image QR generation error", 500, e);
        }
    }

    @Override
    public void save(QRResult qr, String fileName) {
        try {
            // Save png as file
            File outputImage = new File(contextPath + fileName + ".png");
            ImageIO.write(qr.image(), "png", outputImage);

            // save svg as file
            File outputSvg = new File(contextPath + fileName + ".svg");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputSvg))) {
                writer.write(qr.svg());
            }

        } catch (IOException e) {
            throw new ProviderException("Failed to save QR files", 500, e);
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
            throw new ProviderException("Image QR deletion error", 500, e);
        }
    }
}