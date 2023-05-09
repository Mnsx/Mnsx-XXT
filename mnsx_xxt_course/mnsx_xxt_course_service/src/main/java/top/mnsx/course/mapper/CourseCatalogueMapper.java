package top.mnsx.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.mnsx.course.dto.CatalogueDto;
import top.mnsx.course.po.CourseCatalogue;

import java.util.List;

/**
* @Author Mnsx_x
*/
@Mapper
public interface CourseCatalogueMapper extends BaseMapper<CourseCatalogue> {

    List<CatalogueDto> selectTreeNodes(Integer courseId);
}




