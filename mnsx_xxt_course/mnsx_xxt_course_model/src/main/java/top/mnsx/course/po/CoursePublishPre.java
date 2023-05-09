package top.mnsx.course.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName course_publish_pre
 */
@TableName(value ="course_publish_pre")
@Data
public class CoursePublishPre implements Serializable {
    private Integer id;

    private String courseName;

    private String tags;

    private String firstCategory;

    private String firstName;

    private String secondCategory;

    private String secondName;

    private String picture;

    private String courseDesc;

    private String courseSale;

    private String courseCatalogue;

    private Integer auditStatus;

    private Integer ifCharge;

    private Double price;

    private Double originalPrice;

    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}