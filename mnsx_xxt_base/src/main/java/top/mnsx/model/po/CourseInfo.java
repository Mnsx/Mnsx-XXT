package top.mnsx.model.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName course_info
 */
@TableName(value ="course_info")
@Data
public class CourseInfo implements Serializable {
    /**
     * 课程信息编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程标签
     */
    private String tags;

    /**
     * 一级分类
     */
    private String firstCategory;

    /**
     * 二级分类
     */
    private String secondCategory;

    /**
     * 是否收费：1-收费，0-免费
     */
    private Integer ifCharge;

    /**
     * 课程描述
     */
    private String courseDesc;

    /**
     * 课程图片
     */
    private String picture;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 更改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 课程审核状态：0-未审核，1-已审核，2-审核未通过
     */
    private Integer auditStatus;

    /**
     * 课程发布状态：0-未发布，1-已发布，2-下线
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}