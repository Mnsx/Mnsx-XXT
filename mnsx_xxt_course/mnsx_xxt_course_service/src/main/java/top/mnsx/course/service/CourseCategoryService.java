package top.mnsx.course.service;

import top.mnsx.course.dto.CourseCategoryTreeDto;

import java.util.List;

/**
 * 课程分类service
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public interface CourseCategoryService {
    /**
     * 树查询课程分类
     * @param id 机构Id
     * @return 集合
     */
    public List<CourseCategoryTreeDto> queryTreeNodes(String id);

    /**
     * 通过编号得到分类名称
     * @param firstCategory 分类编号
     */
    String getCategoryNameById(String firstCategory);
}
