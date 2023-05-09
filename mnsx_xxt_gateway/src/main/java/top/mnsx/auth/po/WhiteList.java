package top.mnsx.auth.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName gateway_white_list
 */
@TableName(value ="gateway_white_list")
@Data
public class WhiteList implements Serializable {
    private Integer id;

    private String path;

    private String description;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}