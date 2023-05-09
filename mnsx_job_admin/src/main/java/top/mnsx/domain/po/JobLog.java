package top.mnsx.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName mnsx_job_log
 */
@TableName(value ="job_log")
@Data
public class JobLog implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 任务ID
     */
    private Integer jobId;

    /**
     * 本次运行的地址
     */
    private String addressList;

    /**
     * 任务执行失败重试次数
     */
    private Integer failRetryCount;

    /**
     * 调度开始时间
     */
    private Date triggerStartTime;

    /**
     * 调度结束时间
     */
    private Date triggerEndTime;

    /**
     * 调度结果：1-成功，0-失败
     */
    private Integer triggerResult;

    /**
     * 调度日志
     */
    private String triggerMsg;

    /**
     * 任务执行结果：1-成功，0-失败
     */
    private Integer runResult;

    /**
     * 任务执行日志
     */
    private String runMsg;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}