package top.mnsx.course.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName course_audit
 */
@TableName(value ="course_audit")
@Data
public class CourseAudit implements Serializable {
    /**
     * 课程审核信息编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 课程编号
     */
    private Integer courseId;

    /**
     * 审核信息
     */
    private String auditMsg;

    /**
     * 审核状态：0-未审核，1-已审核，2-审核未通过
     */
    private Integer auditStatus;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核时间
     */
    private Date auditTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}