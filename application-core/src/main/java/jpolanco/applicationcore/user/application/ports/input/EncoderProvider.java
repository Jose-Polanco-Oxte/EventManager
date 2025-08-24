package jpolanco.applicationcore.user.application.ports.input;

/**
 * Interface for encoding and matching encoded data, such as passwords.
 * Implementations should provide methods to encode raw data and verify matches.
 */
public interface EncoderProvider {
    String encode(String data);

    boolean matches(String rawData, String encodedData);
}
