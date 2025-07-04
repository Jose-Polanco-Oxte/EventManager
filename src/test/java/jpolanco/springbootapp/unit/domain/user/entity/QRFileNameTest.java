package jpolanco.springbootapp.unit.domain.user.entity;

import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("QRFileName interaction tests")
public class QRFileNameTest {
    private User userEntity;
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "johndoe@gmail.com";
    private String encodedPassword = "52mfasih5234_23nF(#1hre";

    @BeforeEach
    void setUp() {
        // Initialize the UserEntity instance before each test
        userEntity = User.create(
                firstName,
                lastName,
                email,
                encodedPassword
        ).getSuccess();
        userEntity.clearEvents();
    }

    @Test
    @DisplayName("Should change QR file name successfully")
    void shouldChangeQRFileNameSuccessfully() {
        var oldQRFileName = userEntity.getQRFileName();
        userEntity.newQRFileName();
        assertNotEquals(oldQRFileName, userEntity.getQRFileName(), "QR file name should be updated");
        var checkQRUUID = UUID.fromString(userEntity.getQRFileName());
        assertNotNull(checkQRUUID, "QR file name should be a valid UUID");
    }
}
