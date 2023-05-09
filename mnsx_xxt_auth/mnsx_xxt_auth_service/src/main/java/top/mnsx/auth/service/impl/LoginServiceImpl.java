package top.mnsx.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.mnsx.auth.dto.LoginDto;
import top.mnsx.auth.handler.PhoneLoginHandler;
import top.mnsx.auth.vo.LoginResponse;
import top.mnsx.model.po.User;
import top.mnsx.auth.service.LoginService;
import top.mnsx.utils.JwtUtil;
import top.mnsx.config.RedisConstant;
import top.mnsx.exception.XXTException;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private PhoneLoginHandler loginHandler;
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private AsyncClient asyncClient;

    @Value("${sms.sign-name}")
    private String signName;
    @Value("${sms.template-code}")
    private String templateCode;

    @Override
    public LoginResponse login(LoginDto loginDto) {
        User user = loginHandler.doLogin(loginDto);
        if (user != null) {
            Integer id = user.getId();
            // 将用户信息保存再redis中
            template.boundValueOps(RedisConstant.KEY_AUTH_LOGIN + id)
                    .set(JSON.toJSONString(user), 60*60*24*7, TimeUnit.SECONDS);
            // 返回jwt
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(JwtUtil.createJWT(String.valueOf(id), 100*60*60*24*7L));
            loginResponse.setUserId(user.getId());
            return loginResponse;
        }

        XXTException.cast("账号不存在或密码错误");
        return null;
    }

    @Override
    public void logout(Integer id) {
        // 删除redis中的数据
        template.delete(RedisConstant.KEY_AUTH_LOGIN + id);
    }

    @Override
    public Boolean sendSms(String phoneNumber) {
        String code = getCode();

        SendSmsRequest request = SendSmsRequest.builder()
                .phoneNumbers(phoneNumber)
                .signName(signName)
                .templateCode(templateCode)
                .templateParam("{\"code\": \"" + code + "\"}")
                .build();

        CompletableFuture<SendSmsResponse> response = asyncClient.sendSms(request);
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = response.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        template.boundValueOps(RedisConstant.KEY_AUTH_LOGIN_PHONE + phoneNumber).set(code, 60*2, TimeUnit.SECONDS);

        return sendSmsResponse != null && sendSmsResponse.getBody().getCode().equals("OK");
    }

    private String getCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }

        return str.toString();
    }
}
