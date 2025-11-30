package top.seven.assistant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import top.seven.assistant.exception.BusinessException;
import top.seven.assistant.mapper.UserMapper;
import top.seven.assistant.model.RequestCode;
import top.seven.assistant.model.User;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void login(String userNumber, String password) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserNumber, userNumber)
                .last("limit 1"));
        if (user == null) {
            throw new BusinessException(RequestCode.USER_NOT_FOUND);
        }
        if (Boolean.TRUE.equals(user.getDeleted())) {
            throw new BusinessException(RequestCode.USER_NOT_FOUND);
        }
        String encoded = user.getPassword();
        System.out.println(encoded);
        if (encoded == null || encoded.isEmpty()) {
            throw new BusinessException(RequestCode.INVALID_CREDENTIALS);
        }
        if (!passwordEncoder.matches(password, encoded)) {
            throw new BusinessException(RequestCode.INVALID_CREDENTIALS);
        }
    }

    public String encodePassword(String raw) {
        return passwordEncoder.encode(raw);
    }

    public void resetPassword(String userNumber, String currentPassword, String newPassword) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserNumber, userNumber)
                .last("limit 1"));
        String encoded = user.getPassword();
        if (encoded != null && !encoded.isEmpty()) {
            if (currentPassword == null || !passwordEncoder.matches(currentPassword, encoded)) {
                throw new BusinessException(RequestCode.WRONG_CURRENT_PASSWORD);
            }
        }
        String newEncoded = encodePassword(newPassword);
        User update = new User();
        update.setId(user.getId());
        update.setPassword(newEncoded);
        userMapper.updateById(update);
    }
}
