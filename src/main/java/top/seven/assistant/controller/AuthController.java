package top.seven.assistant.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.seven.assistant.common.model.Result;
import top.seven.assistant.service.AuthServiceImpl;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginReq req) {
        authService.login(req.getUserNumber(), req.getPassword());
        return Result.success("登录成功");
    }

    @PostMapping("/reset-password")
    public Result<String> resetPassword(@RequestBody ResetReq req) {
        authService.resetPassword(req.getUserNumber(), req.getCurrentPassword(), req.getNewPassword());
        return Result.success("密码修改成功");
    }

    @PostMapping("/mobile/code")
    public Result<String> sendMobileCode(@RequestBody MobileCodeReq req) {
        authService.sendLoginCode(req.getMobile());
        return Result.success("验证码发送成功");
    }

    @Data
    public static class LoginReq {
        private String userNumber;
        private String password;
    }

    @Data
    public static class ResetReq {
        private String userNumber;
        private String currentPassword;
        private String newPassword;
    }

    @Data
    public static class MobileCodeReq {
        private String mobile;
    }
}
