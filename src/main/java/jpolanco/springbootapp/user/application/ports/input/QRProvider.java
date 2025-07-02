package jpolanco.springbootapp.user.application.ports.input;

import jpolanco.springbootapp.user.application.utils.QRResult;

public interface QRProvider {
    QRResult generate(String content);
    void save(QRResult qr, String fileName);
    boolean exist(String fileName);
    void delete(String fileName);

}
