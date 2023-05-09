package top.mnsx.auth.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.mnsx.auth.dto.LoginDto;
import top.mnsx.model.po.User;

@Component
public abstract class AbstractLoginHandler {
    public static final Integer USERNAME_TYPE = 0;
    public static final Integer PHONE_TYPE = 1;
    public static final Integer EMAIL_TYPE = 2;
    public static final Integer WECHAT_TYPE = 3;

    @Autowired
    private UsernameLoginHandler usernameLoginHandler;
    @Autowired
    private PhoneLoginHandler phoneLoginHandler;

    public User doLogin(LoginDto loginDto) {
        Integer loginType = loginDto.getLoginType();
        if (loginType.equals(USERNAME_TYPE)) {
            return usernameLoginHandler.loginFun(loginDto);
        } else if (loginType.equals(PHONE_TYPE)) {
            return phoneLoginHandler.loginFun(loginDto);
        }
        return null;
    }

    abstract protected User loginFun(LoginDto loginDto);
}
