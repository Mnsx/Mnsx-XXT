package top.mnsx.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadLocalBean {
    @Bean
    public ThreadLocal<Integer> threadLocal() {
        return new ThreadLocal<>();
    }
}
