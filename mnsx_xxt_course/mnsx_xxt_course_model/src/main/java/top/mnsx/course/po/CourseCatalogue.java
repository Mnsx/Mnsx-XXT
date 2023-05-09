package top.mnsx.course.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName couse_catalogue
 */
@TableName(value ="course_catalogue")
@Data
public class CourseCatalogue implements Serializable {
    /**
     * 课程目录编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 课程编号
     */
    private Integer courseId;

    /**
     * 课程目录名称
     */
    private String catalogueName;

    /**
     * 父类编号
     */
    private Integer parentId;

    /**
     * 课程类型：1-视频，2-文档，3-图片
     */
    private Integer mediaType;

    /**
     * 是否支持预览：1-支持，0-不支持
     */
    private Integer isPreview;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 目录描述
     */
    private String catalogueDesc;

    /**
     * 视频时长
     */
    private String videoTime;

    /**
     * 排序
     */
    private Integer orderBy;

    /**
     * 状态：1-启用，2-停用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}