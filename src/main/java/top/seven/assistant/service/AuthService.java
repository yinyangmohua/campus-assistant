package top.seven.assistant.service;

public interface AuthService {
    void login(String userNumber, String password);

    String encodePassword(String raw);

    void resetPassword(String userNumber, String currentPassword, String newPassword);
}
