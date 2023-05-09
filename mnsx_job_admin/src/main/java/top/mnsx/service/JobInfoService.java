package top.mnsx.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.mnsx.utils.Result;
import top.mnsx.domain.po.JobInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import top.mnsx.domain.vo.JobInfoPageVo;

/**
* @author Mnsx_x
* @description 针对表【mnsx_job_info】的数据库操作Service
* @createDate 2023-03-28 11:55:47
*/
public interface JobInfoService extends IService<JobInfo> {

    public Result register(JobInfo jobINfo);

    Result<JobInfo> insert(JobInfo jobInfo);

    Result<Page<JobInfoPageVo>> queryByPage(Integer pageNum, Integer pageSize);

    Result<JobInfo> update(JobInfo jobInfo);

    Result<Boolean> delete(Integer id);

    Result<Boolean> enable(Integer id);

    Result<Boolean> disable(Integer id);

    Result<Boolean> run(Integer id);
}
