package top.mnsx.config;

import lombok.Data;

/**
 * 任务调度配置属性类
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Data
public class JobProperties {
    // admin端Ip地址
    private String adminIp;

    // admin端端口
    private Integer adminPort;

    // 应用名称
    private String appName;

    // 应用简介
    private String appDesc;

    // Ip地址
    private String ip;

    // 端口
    private Integer port;
}
