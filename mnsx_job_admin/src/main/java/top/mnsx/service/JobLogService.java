package top.mnsx.service;

import top.mnsx.utils.Result;
import top.mnsx.domain.po.JobLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Mnsx_x
* @description 针对表【mnsx_job_log】的数据库操作Service
* @createDate 2023-03-28 11:55:52
*/
public interface JobLogService extends IService<JobLog> {

    Result<?> queryByJobId(Integer jobId);
}
