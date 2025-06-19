package jpolanco.springbootapp.user.infrastructure.components.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRToSVG {
    public static String generateSVG(String data, int width, int height) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width/10, height/10);

        int scale = 6; // Escala 6x
        int scaledWidth = bitMatrix.getWidth() * scale;
        int scaledHeight = bitMatrix.getHeight() * scale;

        StringBuilder svg = new StringBuilder();
        svg.append(String.format("<svg xmlns='http://www.w3.org/2000/svg' width='%d' height='%d'>\n", scaledWidth, scaledHeight));
        svg.append("<rect width='100%' height='100%' fill='white'/>\n");

        for (int y = 0; y < bitMatrix.getHeight(); y++) {
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                if (bitMatrix.get(x, y)) {
                    svg.append(String.format(
                            "<rect x='%d' y='%d' width='%d' height='%d' fill='black'/>\n",
                            x * scale, y * scale, scale, scale
                    ));
                }
            }
        }

        svg.append("</svg>");
        return svg.toString();
    }
}
