package top.mnsx.auth.service;

import top.mnsx.model.po.User;
import top.mnsx.model.RestResponse;

public interface UserService {
    /**
     * 通过用户编号查询用户信息
     * @param userId 用户编号
     * @return User
     */
    RestResponse<User> getUserById(Integer userId);
}
