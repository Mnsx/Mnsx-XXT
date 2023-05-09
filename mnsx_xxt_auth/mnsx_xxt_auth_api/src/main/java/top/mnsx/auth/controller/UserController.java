package top.mnsx.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mnsx.model.po.User;
import top.mnsx.auth.service.UserService;
import top.mnsx.model.RestResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public RestResponse<User> getUserById(@PathVariable("userId") Integer userId) {
        return userService.getUserById(userId);
    }
}
