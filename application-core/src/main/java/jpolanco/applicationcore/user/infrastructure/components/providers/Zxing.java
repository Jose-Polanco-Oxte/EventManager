package jpolanco.applicationcore.user.infrastructure.components.providers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.annotation.PostConstruct;
import jpolanco.applicationcore.shared.application.errors.Internal;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.application.ports.input.QRProvider;
import jpolanco.applicationcore.user.infrastructure.components.utils.QRToSVG;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Zxing implements QRProvider {

    @Value("${QRPATH}")
    private String contextPath;

    @PostConstruct
    public void init() {
        contextPath = contextPath.endsWith("\\") ? contextPath : contextPath + "\\";
    }


    @Override
    public void save(String content, String fileName) throws WriterException, IOException {
        int width = 512;
        int height = 512;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        String svg = QRToSVG.generateSVG(content, width, height);

        store(svg, bufferedImage, fileName);
    }

    @Override
    public Result<Void, ServiceError> generate(String content) {
        String fileName = UUID.randomUUID().toString(); // Unique filename
        try {
            save(content, fileName);
            return Result.success();
        } catch (WriterException e) {
            return Result.failure(Internal.internalError("QR generation failed " + e.getMessage()));
        } catch (Exception e) {
            return Result.failure(Internal.internalError("Unexpected error during QR generation " + e.getMessage()));
        }
    }

    // Store the SVG and PNG files
    public void store(String svg, BufferedImage image, String fileName) throws IOException {

        // Save SVG file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(contextPath + fileName + ".svg"))) {
            writer.write(svg);
        }

        // Save PNG file
        File pngFile = new File(contextPath + fileName + ".png");
        ImageIO.write(image, "png", pngFile);
    }
}