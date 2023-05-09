package top.mnsx.course.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.mnsx.course.dto.BindCatalogueMediaDto;
import top.mnsx.course.dto.CourseCategoryTreeDto;
import top.mnsx.course.po.CatalogueMedia;
import top.mnsx.course.service.AssociationMediaService;
import top.mnsx.course.service.CourseCategoryService;
import top.mnsx.model.RestResponse;

import java.util.List;

/**
 * 课程分类controller
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@RestController
@RequestMapping("/course-category")
@Slf4j
public class CourseCategoryController {
    @Autowired
    private CourseCategoryService courseCategoryService;
    @Autowired
    private AssociationMediaService associationMediaService;

    /**
     * 树节点查询
     * @return 返回集合
     */
    @GetMapping("/tree-nodes")
    public RestResponse<List<CourseCategoryTreeDto>> queryTreeNodes() {
        return RestResponse.success(courseCategoryService.queryTreeNodes("1"));
    }
}
