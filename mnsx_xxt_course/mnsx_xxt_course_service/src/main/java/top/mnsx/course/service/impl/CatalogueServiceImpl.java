package top.mnsx.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import top.mnsx.course.dto.SaveCatalogueDto;
import top.mnsx.course.dto.CatalogueDto;
import top.mnsx.course.mapper.CourseCatalogueMapper;
import top.mnsx.course.po.CourseCatalogue;
import top.mnsx.course.service.CatalogueService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
@Service
public class CatalogueServiceImpl implements CatalogueService {
    @Resource
    private CourseCatalogueMapper catalogueMapper;

    @Override
    public List<CatalogueDto> findCatalogueTree(Integer courseId) {
        return catalogueMapper.selectTreeNodes(courseId);
    }

    @Override
    public CourseCatalogue saveCatalogue(SaveCatalogueDto dto) {
        Integer id = dto.getId();

        CourseCatalogue courseCatalogue = catalogueMapper.selectById(id);

        if (courseCatalogue == null) {
            CourseCatalogue cc = new CourseCatalogue();
            BeanUtils.copyProperties(dto, cc);
            int count = getCatalogueCount(dto.getCourseId(), dto.getParentId());
            cc.setOrderBy(count + 1);
            // 计算默认顺序
            catalogueMapper.insert(cc);
        } else {
            BeanUtils.copyProperties(dto, courseCatalogue);
            catalogueMapper.insert(courseCatalogue);
        }

        return courseCatalogue;
    }

    /**
     * 获取目录数量
     * @param courseId 课程编号
     * @param parentId 父类id
     * @return int
     */
    private int getCatalogueCount(Integer courseId,Integer parentId){
        LambdaQueryWrapper<CourseCatalogue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseCatalogue::getCourseId,courseId);
        queryWrapper.eq(CourseCatalogue::getParentId,parentId);
        return catalogueMapper.selectCount(queryWrapper);
    }
}
