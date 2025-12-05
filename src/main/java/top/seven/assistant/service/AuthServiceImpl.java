package top.seven.assistant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import top.seven.assistant.common.constant.RedisKey;
import top.seven.assistant.common.exception.BusinessException;
import top.seven.assistant.mapper.UserMapper;
import top.seven.assistant.common.constant.RequestCode;
import top.seven.assistant.entity.User;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final RedisService redisService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
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
        if (encoded == null || encoded.isEmpty()) {
            throw new BusinessException(RequestCode.INVALID_CREDENTIALS);
        }
        if (!passwordEncoder.matches(password, encoded)) {
            throw new BusinessException(RequestCode.INVALID_CREDENTIALS);
        }
    }

    @Override
    public String encodePassword(String raw) {
        return passwordEncoder.encode(raw);
    }

    @Override
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
        User newPwd = User.builder()
                .id(user.getId())
                .password(newEncoded)
                .build();
        userMapper.updateById(newPwd);
    }

    @Override
    public void sendLoginCode(String mobile) {
        String code = String.format("%04d", java.util.concurrent.ThreadLocalRandom.current().nextInt(10000));
        String key = RedisKey.LOGIN_CODE.format(mobile);
        redisService.set(key, code);
        redisService.expire(key, 60);
    }
}
