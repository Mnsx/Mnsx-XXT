package top.mnsx.auth.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import top.mnsx.auth.dto.LoginDto;
import top.mnsx.auth.enums.UserTypeEnum;
import top.mnsx.auth.mapper.UserMapper;
import top.mnsx.model.po.User;
import top.mnsx.config.RedisConstant;
import top.mnsx.exception.XXTException;

import javax.annotation.Resource;
import java.util.UUID;

@Slf4j
@Component
@DependsOn("usernameLoginHandler")
public class PhoneLoginHandler extends AbstractLoginHandler {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate template;

    @Override
    protected User loginFun(LoginDto loginDto) {

        // 获取参数
        String phoneNumber = loginDto.getPhoneNumber();
        String code = loginDto.getCode();

        // 判断参数是否为空
        if (phoneNumber == null || code == null) {
            XXTException.cast("手机号或验证码为空");
            return null;
        }

        // 改手机号是否发送验证码，否，返回一场
        String tCode = "";
        if (Boolean.TRUE.equals(template.hasKey(RedisConstant.KEY_AUTH_LOGIN_PHONE + phoneNumber))) {
            tCode = template.boundValueOps(RedisConstant.KEY_AUTH_LOGIN_PHONE + phoneNumber).get();
        } else {
            XXTException.cast("该手机号并未发送验证请求");
            return null;
        }

        // 验证验证码是否正确
        if (code.equals(tCode)) {
            // 正确，查询用户是否存在
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhoneNumber, phoneNumber);
            User user = userMapper.selectOne(wrapper);
            if (user != null) {
                // 存在返回
                return user;
            }
            // 不存在自动注册
            User dbUser = new User();
            dbUser.setPhoneNumber(phoneNumber);
            dbUser.setUserName(generateUsername());
            dbUser.setUserType(UserTypeEnum.USER.getCode());
            dbUser.setPassword("f7266e6ee3e88b58c87c7264a5477c13");
            userMapper.insert(dbUser);
            return dbUser;
        }
        XXTException.cast("手机号或验证码错误");
        return null;
    }

    private String generateUsername() {
        StringBuffer sb = new StringBuffer();
        sb.append("Mnsx-");
        sb.append(UUID.randomUUID().toString().substring(0,6));
        return sb.toString();
    }
}
