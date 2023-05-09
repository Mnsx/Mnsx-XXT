package top.mnsx.auth.service.impl;

import org.springframework.stereotype.Service;
import top.mnsx.auth.mapper.UserMapper;
import top.mnsx.model.po.User;
import top.mnsx.auth.service.UserService;
import top.mnsx.model.RestResponse;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public RestResponse<User> getUserById(Integer userId) {
        User user = userMapper.selectById(userId);
        return RestResponse.success(user);
    }
}
