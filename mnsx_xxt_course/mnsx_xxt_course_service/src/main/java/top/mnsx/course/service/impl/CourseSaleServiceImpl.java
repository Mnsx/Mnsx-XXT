package top.mnsx.course.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.mnsx.course.mapper.CourseSaleMapper;
import top.mnsx.course.po.CourseSale;
import top.mnsx.course.service.CourseSaleService;

/**
 * <p>
 * 课程营销信息 服务实现类
 * </p>
 *
 * @author mnsx
 * @since 2023-03-03
 */
@Service
public class CourseSaleServiceImpl extends ServiceImpl<CourseSaleMapper, CourseSale> implements IService<CourseSale>, CourseSaleService {

}
