package top.mnsx.course.service;

import top.mnsx.course.dto.SaveCatalogueDto;
import top.mnsx.course.dto.CatalogueDto;
import top.mnsx.course.po.CourseCatalogue;

import java.util.List;

/**
 * 教学接话service
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public interface CatalogueService {
    /**
     * 查找教学课程树
     * @param courseId 课程id
     * @return 集合
     */
    public List<CatalogueDto> findCatalogueTree(Integer courseId);

    /**
     * 保存课程计划
     * @param dto dto
     */
    public CourseCatalogue saveCatalogue(SaveCatalogueDto dto);
}
