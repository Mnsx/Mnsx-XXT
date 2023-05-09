package top.mnsx.media.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName media_file
 */
@TableName(value ="media_file")
@Data
public class MediaFile implements Serializable {
    /**
     * 文件编号
     */
    private String id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 课程类型：1-视频，2-文档，3-图片
     */
    private Integer fileType;

    /**
     * 桶
     */
    private String bucket;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件md5
     */
    private String fileId;

    /**
     * 文件访问路径
     */
    private String url;

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
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 启用状态：1-启用，0-停用
     */
    private Integer status;

    /**
     * 审核状态：0-未审核，1-已经审核，2-审核不通过
     */
    private Integer auditStatus;

    /**
     * 审核信息
     */
    private String auditMsg;

    /**
     * 文件大小
     */
    private Long fileSize;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}