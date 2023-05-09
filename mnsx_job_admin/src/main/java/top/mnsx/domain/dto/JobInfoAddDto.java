package top.mnsx.domain.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Data
@ToString
public class JobInfoAddDto {
    // 任务编号
    private Integer appId;

    // 任务名称
    private String jobName;

    // 任务描述
    private String jobDesc;

    // 任务执行CRON
    private String runCron;

    // 任务执行策略
    private Integer runStrategy;

    // 润物执行参数
    private String runParam;

    // 任务执行超时时间
    private Integer runTimeout;

    // 任务执行失败重试次数
    private Integer failRetryMaxCount;

    // 启用状态
    private Integer enabled;
}
