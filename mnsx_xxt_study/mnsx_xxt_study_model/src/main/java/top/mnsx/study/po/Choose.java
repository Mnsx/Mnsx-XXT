package top.mnsx.study.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName study_choose
 */
@TableName(value ="study_choose")
@Data
public class Choose implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer courseId;

    private Integer userId;

    private Integer orderType;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Double price;

    @TableField(fill = FieldFill.INSERT)
    private Integer status;

    private static final long serialVersionUID = 1L;

}