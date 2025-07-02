package jpolanco.springbootapp.user.application.ports.input;


public interface AuthenticatorProvider {
    void authenticate(String email, String password);
}
