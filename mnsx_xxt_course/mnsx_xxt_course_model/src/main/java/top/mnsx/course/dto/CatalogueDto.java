package top.mnsx.course.dto;

import lombok.Data;
import top.mnsx.course.po.CatalogueMedia;
import top.mnsx.course.po.CourseCatalogue;

import java.util.List;

/**
 * 课程计划树dto
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Data
public class CatalogueDto extends CourseCatalogue {
    // 子目录
    List<CatalogueDto> catalogueTreeNodes;

    // 关联的媒资信息
    CatalogueMedia catalogueMedia;
}
