package top.mnsx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.mnsx.utils.Result;
import top.mnsx.domain.po.JobApp;
import top.mnsx.enums.CreateWayEnum;
import top.mnsx.enums.EnabledEnum;
import top.mnsx.enums.IsDeletedEnum;
import top.mnsx.service.JobAppService;
import top.mnsx.mapper.JobAppMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Mnsx_x
* @description 针对表【mnsx_job_app】的数据库操作Service实现
* @createDate 2023-03-28 11:55:30
*/
@Service
public class JobAppServiceImpl extends ServiceImpl<JobAppMapper, JobApp>
    implements JobAppService{

    @Resource
    private JobAppMapper jobAppMapper;

    @Override
    public Result<Page<JobApp>> queryByPage(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<JobApp> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(JobApp::getId);

        Page<JobApp> page = new Page<>();
        Page<JobApp> result = page(page, wrapper);
        return Result.ok(result);
    }

    @Override
    @Transactional
    public Result<JobApp> insert(JobApp jobApp) {
        // 判断是否存在该名称数据
        LambdaQueryWrapper<JobApp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(jobApp.getAppName() != null, JobApp::getAppName, jobApp.getAppName());
        JobApp dbJobApp = jobAppMapper.selectOne(wrapper);

        // 如果不存在，直接保存
        if (Objects.isNull(dbJobApp)) {
            jobApp.setCreateWay(jobApp.getCreateWay() == null ? CreateWayEnum.AUTO.getCode() : jobApp.getCreateWay());
            jobApp.setEnabled(jobApp.getEnabled() == null ? EnabledEnum.YES.getCode() : jobApp.getEnabled());
            jobAppMapper.insert(jobApp);
            return Result.ok(jobApp);
        }

        // 如果手动添加，提示重复
        if (Objects.equals(dbJobApp.getCreateWay(), CreateWayEnum.MANUAL.getCode())) {
            return Result.err("应用名称已经存在");
        }

        // 自动添加，比较地址
        List<String> addressList = Arrays.stream(dbJobApp.getAddressList().split(","))
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());

        // 地址包含，忽略
        if (addressList.contains(jobApp.getAddressList())) {
            return Result.ok(jobApp);
        } else {
            addressList.add(jobApp.getAddressList());
            String newAddressList = addressList.stream().reduce((s1, s2) -> s1 + "," + s2).orElse("");
            dbJobApp.setAddressList(newAddressList);
            jobAppMapper.insert(dbJobApp);
            return Result.ok(dbJobApp);
        }
    }

    @Override
    public Result<JobApp> update(JobApp jobApp) {
        updateById(jobApp);
        return Result.ok(jobApp);
    }

    @Override
    public Result<List<Map<String, String>>> queryAllName() {
        LambdaQueryWrapper<JobApp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(JobApp::getEnabled, EnabledEnum.YES.getCode());
        wrapper.eq(JobApp::getIsDeleted, IsDeletedEnum.NO.getCode());

        List<Map<String, String>> mapList = jobAppMapper.selectList(wrapper).stream()
                .map(app -> {
                    HashMap<String, String> map = new HashMap<>(2);
                    map.put("appId", String.valueOf(app.getId()));
                    map.put("appName", app.getAppName());
                    return map;
                }).collect(Collectors.toList());

        return Result.ok(mapList);
    }
}




