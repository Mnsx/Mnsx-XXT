package top.mnsx.course.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName course_publish
 */
@TableName(value ="course_publish")
@Data
public class CoursePublish implements Serializable {
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

    private Integer status;

    private Integer ifCharge;

    private Double price;

    private Double originalPrice;

    private String creator;

    private Date createTime;

    private String updater;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}