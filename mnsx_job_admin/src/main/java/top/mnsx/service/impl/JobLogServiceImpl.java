package top.mnsx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.mnsx.utils.Result;
import top.mnsx.domain.po.JobLog;
import top.mnsx.service.JobLogService;
import top.mnsx.mapper.JobLogMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author Mnsx_x
* @description 针对表【mnsx_job_log】的数据库操作Service实现
* @createDate 2023-03-28 11:55:52
*/
@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog>
    implements JobLogService{

    @Override
    public Result<?> queryByJobId(Integer jobId) {
        LambdaQueryWrapper<JobLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(JobLog::getJobId, jobId);
        wrapper.orderByDesc(JobLog::getTriggerEndTime);
        Page<JobLog> jogLogPage = new Page<>(1, 10);
        Page<JobLog> page = page(jogLogPage, wrapper);
        List<Map<String, String>> result = page.getRecords().stream().map(l -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> logMap = new HashMap<>(6);
            logMap.put("triggerStartTime", dateFormat.format(l.getTriggerStartTime()));
            logMap.put("triggerEndTime", dateFormat.format(l.getTriggerEndTime()));
            Integer triggerResult = l.getTriggerResult();
            logMap.put("triggerResult", Objects.equals(triggerResult, 1) ? "成功" : "失败");
            logMap.put("triggerMsf", l.getTriggerMsg());
            Integer jobRunResult = l.getRunResult();
            logMap.put("jobRunResult", Objects.equals(jobRunResult, 1) ? "成功" : "失败");
            logMap.put("jobRunMsg", l.getRunMsg());
            logMap.put("runFailRetryCount", String.valueOf(l.getFailRetryCount()));
            logMap.put("runAddressList", l.getAddressList());
            return logMap;
        }).collect(Collectors.toList());
        return Result.ok(result);
    }
}




