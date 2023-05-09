package top.mnsx.course.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.mnsx.course.dto.BindCatalogueMediaDto;
import top.mnsx.course.dto.SaveCatalogueDto;
import top.mnsx.course.dto.CatalogueDto;
import top.mnsx.course.po.CatalogueMedia;
import top.mnsx.course.po.CourseCatalogue;
import top.mnsx.course.service.AssociationMediaService;
import top.mnsx.course.service.CatalogueService;
import top.mnsx.model.RestResponse;

import java.util.List;

/**
 * 教学计划controller
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
@RestController
@RequestMapping("/catalogue")
public class CatalogueController {
    @Autowired
    private CatalogueService catalogueService;
    @Autowired
    private AssociationMediaService associationMediaService;

    /**
     * 树结构查询
     * @param courseId 课程id
     * @return 返回集合
     */
    @GetMapping("/{courseId}/tree-nodes")
    public RestResponse<List<CatalogueDto>> getTreeNodes(@PathVariable Integer courseId) {
        return RestResponse.success(catalogueService.findCatalogueTree(courseId));
    }

    /**
     * 保存课程计划
     * @param dto 添加dto
     */
    @PostMapping
    public RestResponse<CourseCatalogue> saveCatalogue(@RequestBody SaveCatalogueDto dto) {
        return RestResponse.success(catalogueService.saveCatalogue(dto));
    }

    /**
     * 绑定媒体和目录
     * @param bindCatalogueMediaDto 绑定dto
     */
    @PostMapping("/association-media")
    public RestResponse<CatalogueMedia> associationMedia(@RequestBody BindCatalogueMediaDto bindCatalogueMediaDto) {
        return RestResponse.success(associationMediaService.associationMedia(bindCatalogueMediaDto));
    }
}
