package top.mnsx.study.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.mnsx.model.RestResponse;
import top.mnsx.model.po.User;

@Component
@FeignClient("auth-api")
public interface AuthFeignService {
    @GetMapping("/auth/user/{userId}")
    public RestResponse<User> getUserById(@PathVariable("userId") Integer id);
}
