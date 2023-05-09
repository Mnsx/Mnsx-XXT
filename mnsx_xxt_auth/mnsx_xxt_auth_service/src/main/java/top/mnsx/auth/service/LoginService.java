package top.mnsx.auth.service;

import top.mnsx.auth.dto.LoginDto;
import top.mnsx.auth.vo.LoginResponse;

public interface LoginService {
    /**
     * 登录
     * @param loginDto lo
     * @return String
     */
    public LoginResponse login(LoginDto loginDto);

    /**
     * @param id 编号
     * 登出
     */
    public void logout(Integer id);

    /**
     * 发送短信验证码
     * @param phoneNumber 手机号
     * @return Boolean
     */
    Boolean sendSms(String phoneNumber);
}
