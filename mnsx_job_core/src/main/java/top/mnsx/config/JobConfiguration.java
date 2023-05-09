package top.mnsx.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 防止程序没有注入RestTemplate，如果没有自动注入
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
public class JobConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
