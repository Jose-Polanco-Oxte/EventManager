package jpolanco.applicationcore.user.application.ports.input;

import com.google.zxing.WriterException;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;

import java.io.IOException;

/**
 * Interface for generating and saving QR codes.
 * Implementations should provide methods to generate QR codes from content
 * and save them to a specified file.
 */
public interface QRProvider {
    void save(String content, String fileName) throws WriterException, IOException;

    Result<Void, ServiceError> generate(String content);
}
