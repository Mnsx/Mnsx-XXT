package top.mnsx.study.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName study_record
 */
@TableName(value ="study_record")
@Data
public class Record implements Serializable {
    private Integer id;

    private Integer chooseId;

    private Integer userId;

    private Integer courseId;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
