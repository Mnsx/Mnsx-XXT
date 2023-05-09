package top.mnsx.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.mnsx.auth.dto.LoginDto;
import top.mnsx.auth.service.LoginService;
import top.mnsx.auth.vo.LoginResponse;
import top.mnsx.model.RestResponse;
import top.mnsx.model.po.User;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/open/login/{phone}")
    public RestResponse<Boolean> sendSms(@PathVariable("phone") String phoneNumber) {
        return RestResponse.success(loginService.sendSms(phoneNumber));
    }

    @PostMapping("/open/login")
    public RestResponse<LoginResponse> login(@RequestBody LoginDto dto) {
        return RestResponse.success(loginService.login(dto));
    }

    @PostMapping("/logout/{id}")
    public RestResponse logout(@PathVariable Integer id) {
        loginService.logout(id);
        return RestResponse.success(null);
    }
}
