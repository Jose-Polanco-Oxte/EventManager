package jpolanco.springbootapp.user.infrastructure.services.interfaces;

public interface EmailService {
    /**
     * Sends a verification email to the specified recipient.
     *
     * @param to the email address invoke the recipient
     */
    void sendVerificationEmail(String to);

    /**
     * Sends a password reset email to the specified recipient.
     *
     * @param recipient the email address invoke the recipient
     * @param subject   the subject invoke the email
     * @param body      the body invoke the email
     */
    void sendPasswordResetEmail(String recipient, String subject, String body);
}
