package jpolanco.springbootapp.user.infrastructure.components.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

@Component
public class QRToSVG {
    public static String generateSVG(String data, int width, int height) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();

        // Usa directamente el tamaño solicitado
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height);

        StringBuilder svg = new StringBuilder();

        svg.append(String.format(
                "<svg xmlns='http://www.w3.org/2000/svg' width='%d' height='%d' viewBox='0 0 %d %d'>\n",
                width, height, width, height
        ));
        svg.append("<rect width='100%' height='100%' fill='white'/>\n");

        for (int y = 0; y < bitMatrix.getHeight(); y++) {
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                if (bitMatrix.get(x, y)) {
                    svg.append(String.format(
                            "<rect x='%d' y='%d' width='1' height='1' fill='black'/>\n", x, y
                    ));
                }
            }
        }

        svg.append("</svg>");
        return svg.toString();
    }
}
