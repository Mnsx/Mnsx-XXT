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
 * @TableName course_catalogue_work
 */
@TableName(value ="course_catalogue_work")
@Data
public class CatalogueWork implements Serializable {
    /**
     * 作业编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 作业名称
     */
    private String workName;

    /**
     * 目录编号
     */
    private Integer catalogueId;

    /**
     * 课程编号
     */
    private Integer courseId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String creator;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}