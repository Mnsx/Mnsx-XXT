package top.mnsx.course.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName course_category
 */
@TableName(value ="course_category")
@Data
public class CourseCategory implements Serializable {
    /**
     * 课程分类
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 父节点编号
     */
    private String parentId;

    /**
     * 是否展示：1-展示，0-不展示
     */
    private Integer isShow;

    /**
     * 排序
     */
    private Integer orderBy;

    /**
     * 是否叶子：1-叶子，0-非叶子
     */
    private Integer isLeaf;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}