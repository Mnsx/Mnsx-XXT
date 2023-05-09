package top.mnsx.auth.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Component;
import top.mnsx.auth.dto.LoginDto;
import top.mnsx.auth.mapper.UserMapper;
import top.mnsx.model.po.User;
import top.mnsx.utils.MD5Util;
import top.mnsx.exception.XXTException;

import javax.annotation.Resource;

@Component
public class UsernameLoginHandler extends AbstractLoginHandler {
    @Resource
    private UserMapper userMapper;

    @Override
    protected User loginFun(LoginDto loginDto) {
        // 获取参数
        String username = loginDto.getUserName();
        String password = loginDto.getPassword();

        // 查询是否存在该用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            XXTException.cast("账号不存在或密码错误");
        }
        // 对比密码是否真确
        String dbPassword = user.getPassword();
        String tPassword = MD5Util.inputPassToTPass(password, "7655d825");

        return tPassword.equals(dbPassword) ? user : null;
    }
}
