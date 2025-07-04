package jpolanco.springbootapp.unit.domain.user.value_objects;

import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.domain.model.valueobjects.QRFileName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("QRFIleName Value Object Tests")
public class QRFIleNameTest {
    @Test
    @DisplayName("Should fail for invalid QR file names")
    void shouldFailForInvalidQRFileNames() {
        List<String> invalidNames = Arrays.asList(
                "",                     // Empty name
                " ",                    // Whitespace
                null,                   // Null string
                "qr/invalid/name.png"   // Contain extension
        );
        for (String name : invalidNames) {
            var result = QRFileName.create(name);
            assertTrue(result.isFailure(), "Expected failure for: " + name);
        }
    }


    @Test
    @DisplayName("Should create valid QR file names")
    void shouldCreateValidQRFileNames() {
        List<String> validNames = List.of(
                "qr_file_name",
                "QRFileName123",
                "valid_name_456",
                "Gaw GAWm dtwa_ gw", // Valid, spaces replaced with underscores
                "another_valid_name",
                "qr_file_name_with_underscores",
                "valid-name-789",
                "qr-file-name-with-dashes",
                "validNameWithCamelCase",
                "validNameWithNumbers123"
        );

        for (String name : validNames) {
            Result<QRFileName> result = QRFileName.create(name);
            assertTrue(result.isSuccess(), "Expected success for: " + name + " but got failure: " + result.getMessage());
            // Verify the value is set correctly
            assertEquals(name.trim().replaceAll(" ", "_"), result.getValue().getValue());
        }
    }

    @Test
    @DisplayName("Two QRFileName objects with same value should be equal")
    void shouldBeEqualForSameQRFileNameValues() {
        var qrFileNameOne = QRFileName.create("same_qr_file_name").getValue();
        var qrFileNameTwo = QRFileName.create("same_qr_file_name").getValue();
        assertEquals(qrFileNameOne, qrFileNameTwo, "Expected both QRFileName objects to be equal");
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        var qrFileName = QRFileName.create("same_qr_file_name").getValue();
        assertEquals(qrFileName, qrFileName, "Expected QRFileName to be equal to itself");
    }
}
