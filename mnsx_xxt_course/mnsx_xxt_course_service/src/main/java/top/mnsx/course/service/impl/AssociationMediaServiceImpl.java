package top.mnsx.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import top.mnsx.course.dto.BindCatalogueMediaDto;
import top.mnsx.course.mapper.CatalogueMediaMapper;
import top.mnsx.course.mapper.CourseCatalogueMapper;
import top.mnsx.course.po.CatalogueMedia;
import top.mnsx.course.po.CourseCatalogue;
import top.mnsx.course.service.AssociationMediaService;
import top.mnsx.exception.XXTException;

import javax.annotation.Resource;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Service
public class AssociationMediaServiceImpl implements AssociationMediaService {
    @Resource
    CourseCatalogueMapper courseCatalogueMapper;
    @Resource
    CatalogueMediaMapper catalogueMediaMapper;


    @Override
    public CatalogueMedia associationMedia(BindCatalogueMediaDto bindCatalogueMediaDto) {
        // 计划id
        Integer catalogueId = bindCatalogueMediaDto.getCatalogueId();
        CourseCatalogue courseCatalogue = courseCatalogueMapper.selectById(catalogueId);
        if (courseCatalogue == null) {
            XXTException.cast("课程计划不存在");
        }

        // 根据catalogue删除原有的媒体
        LambdaQueryWrapper<CatalogueMedia> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CatalogueMedia::getCatalogueId, bindCatalogueMediaDto.getCatalogueId());
        int delete = catalogueMediaMapper.delete(wrapper);

        // 添加新记录
        CatalogueMedia catalogueMedia = new CatalogueMedia();
        BeanUtils.copyProperties(bindCatalogueMediaDto, catalogueMedia);
        catalogueMedia.setCourseId(courseCatalogue.getCourseId());
        catalogueMediaMapper.insert(catalogueMedia);

        return catalogueMedia;
    }
}
