package top.mnsx.media.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName media_process_history
 */
@TableName(value ="media_process_history")
@Data
public class MediaProcessHistory implements Serializable {
    /**
     * 媒体处理
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文件编号
     */
    private String fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 桶
     */
    private String bucket;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件范文路径
     */
    private String url;

    /**
     * 启用状态：1-启用，2-停用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 错误信息
     */
    private String failMsg;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}