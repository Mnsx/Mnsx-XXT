package top.mnsx.course.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.mnsx.course.dto.CourseCategoryTreeDto;
import top.mnsx.course.mapper.CourseCategoryMapper;
import top.mnsx.course.po.CourseCategory;
import top.mnsx.course.service.CourseCategoryService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Service
@Slf4j
public class CourseCategoryServiceImpl implements CourseCategoryService {
    @Resource
    private CourseCategoryMapper courseCategoryMapper;


    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        // 得到了根节点下的所有子节点
        List<CourseCategoryTreeDto> categoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);

        // 将数据封装到List中，只包括了根节点和下属
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = new ArrayList<>();
        HashMap<String, CourseCategoryTreeDto> nodeMap = new HashMap<>();

        categoryTreeDtos.forEach(item -> {
            nodeMap.put(item.getId(), item);

            if (item.getParentId().equals(id)) {
                courseCategoryTreeDtos.add(item);
            }
            // 找到子节点，放到父节点的childrenTreeNodes
            String parentId = item.getParentId();
            // 找到该节点的父节点
            CourseCategoryTreeDto parentNode = nodeMap.get(parentId);
            // 判断父节点是否存在
            if (parentNode != null) {
                List childrenTreeNodes = parentNode.getChildrenTreeNodes();
                if (childrenTreeNodes == null) {
                    // 如果集合为空创建集合
                    parentNode.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                }

                // 不为空直接加入
                parentNode.getChildrenTreeNodes().add(item);
            }
        });

        return courseCategoryTreeDtos;
    }

    @Override
    public String getCategoryNameById(String firstCategory) {
        CourseCategory courseCategory = courseCategoryMapper.selectById(firstCategory);
        return courseCategory.getCategoryName();
    }
}
