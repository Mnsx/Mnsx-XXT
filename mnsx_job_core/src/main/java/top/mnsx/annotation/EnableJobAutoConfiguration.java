package top.mnsx.annotation;

import org.springframework.context.annotation.Import;
import top.mnsx.config.JobAutoConfigurationRegistrar;
import top.mnsx.config.JobConfiguration;

import java.lang.annotation.*;

/**
 * 开启任务自动配置的注解, 主要JobAutoConfigurationRegistrar类和JobConfiguration类
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({JobAutoConfigurationRegistrar.class, JobConfiguration.class})
public @interface EnableJobAutoConfiguration {

    // admin端IP地址
    String adminIp();

    // admin端端口
    int adminPort();

    // 应用名称
    String appName();

    // 应用简介
    String appDesc();
}
