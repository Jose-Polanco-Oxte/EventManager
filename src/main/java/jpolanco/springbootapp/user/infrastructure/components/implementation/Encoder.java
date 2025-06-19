package jpolanco.springbootapp.user.infrastructure.components.implementation;

import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Encoder implements EncoderProvider {

    private final PasswordEncoder encoder;

    @Override
    public String encode(String data) {
        return encoder.encode(data);
    }

    @Override
    public boolean matches(String rawData, String encodedData) {
        return encoder.matches(rawData, encodedData);
    }
}
