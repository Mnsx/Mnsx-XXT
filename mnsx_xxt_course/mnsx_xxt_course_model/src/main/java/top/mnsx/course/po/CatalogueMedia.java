package top.mnsx.course.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName course_catalogue_media
 */
@TableName(value ="course_catalogue_media")
@Data
public class CatalogueMedia implements Serializable {
    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 目录编号
     */
    private Integer catalogueId;

    /**
     * 课程编号
     */
    private Integer courseId;

    /**
     * 媒体编号
     */
    private String mediaId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 创建时间
     */
    @TableField(fill= FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill= FieldFill.INSERT)
    private String creator;

    /**
     * 修改时间
     */
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private String updater;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}