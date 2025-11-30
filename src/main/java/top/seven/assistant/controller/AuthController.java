package top.seven.assistant.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.seven.assistant.model.RequestCode;
import top.seven.assistant.model.User;
import top.seven.assistant.model.Result;
import top.seven.assistant.service.AuthService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest req) {
        authService.login(req.getUserNumber(), req.getPassword());
        return Result.success("登录成功");
    }

    @PostMapping("/reset-password")
    public Result<String> resetPassword(@RequestBody ResetRequest req) {
        authService.resetPassword(req.getUserNumber(), req.getCurrentPassword(), req.getNewPassword());
        return Result.success("密码修改成功");
    }

    @Data
    public static class LoginRequest {
        private String userNumber;
        private String password;
    }

    @Data
    public static class ResetRequest {
        private String userNumber;
        private String currentPassword;
        private String newPassword;
    }
}
