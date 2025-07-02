package jpolanco.springbootapp.unit.infrastructure.user.components;

import com.google.zxing.WriterException;
import jpolanco.springbootapp.user.infrastructure.components.utils.QRToSVG;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("QRToSVG Tests")
public class QRToSVGTest {

    @Test
    @DisplayName("generateSVG should create a valid SVG string for a given QR code data")
    public void generateSVG_ShouldCreateValidSVG() throws WriterException {
        // Arrange
        String data = "https://example.com";
        int width = 512;
        int height = 512;

        // Act
        String svg = QRToSVG.generateSVG(data, width, height);

        // Assert
        assertNotNull(svg, "SVG should not be null");
        assertTrue(svg.contains("<svg"), "SVG should start with <svg tag");
        assertTrue(svg.contains("<rect width='100%' height='100%' fill='white'/>"), "SVG should contain a white background rectangle");
        assertTrue(svg.contains("<rect x='"), "SVG should contain rectangles for QR code modules");
        assertTrue(svg.contains("fill='black'"), "SVG should contain black rectangles for QR code modules");
        assertTrue(svg.contains("</svg>"), "SVG should end with </svg> tag");
        // Print the SVG for visual inspection (optional)
        System.out.println(svg);
    }
}
