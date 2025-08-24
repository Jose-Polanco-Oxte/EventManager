package jpolanco.applicationcore.user.application.ports.input;

/**
 * Authentication provider interface for user authentication.
 * This interface defines the contract for authenticating users based on their email and password.
 */
public interface AuthProvider {
    void authenticate(String email, String password);
}
