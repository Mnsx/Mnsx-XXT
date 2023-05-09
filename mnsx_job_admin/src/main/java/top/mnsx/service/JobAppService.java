package top.mnsx.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.mnsx.utils.Result;
import top.mnsx.domain.po.JobApp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author Mnsx_x
* @description 针对表【mnsx_job_app】的数据库操作Service
* @createDate 2023-03-28 11:55:30
*/
public interface JobAppService extends IService<JobApp> {

    Result<Page<JobApp>> queryByPage(Integer pageNum, Integer pageSize);

    Result<JobApp> insert(JobApp jobApp);

    Result<JobApp> update(JobApp jobApp);

    Result<List<Map<String, String>>> queryAllName();
}
