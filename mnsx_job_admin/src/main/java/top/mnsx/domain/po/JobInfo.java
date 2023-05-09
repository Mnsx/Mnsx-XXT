package top.mnsx.domain.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName mnsx_job_info
 */
@TableName(value ="job_info")
@Data
public class JobInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 任务所属应用id
     */
    private Integer appId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务描述
     */
    private String jobDesc;

    /**
     * 报警邮件，多个逗号分隔
     */
    private String email;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建方式：1-自动，2-手工
     */
    private Integer createWay;

    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 任务执行CRON
     */
    private String runCron;

    /**
     * 任务执行策略：1-随机，2-轮询
     */
    private Integer runStrategy;

    /**
     * 任务执行参数
     */
    private String runParam;

    /**
     * 任务执行超时时间，单位秒
     */
    private Integer runTimeout;

    /**
     * 任务执行失败重试次数
     */
    private Integer failRetryMaxCount;

    /**
     * 上次调度时间
     */
    private Date triggerLastTime;

    /**
     * 下次调度时间
     */
    private Date triggerNextTime;

    /**
     * 启用状态：1-启用，0-停用
     */
    private Integer enabled;

    /**
     * 是否删除：1-是，0-否
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}