package top.mnsx.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.mnsx.utils.BeanUtil;
import top.mnsx.domain.po.JobApp;
import top.mnsx.domain.po.JobInfo;
import top.mnsx.domain.po.JobLog;
import top.mnsx.message.JobResponse;
import top.mnsx.service.JobInfoService;
import top.mnsx.service.JobLogService;
import top.mnsx.utils.ThrowableUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
@Component
@DependsOn("beanUtil")
public class QuartzJob implements Job {

    private static final int SUCCESS = 1;
    private static final int ERROR = 0;

    private JobInvoker jobInvoker;
    private JobLogService jobLogService;
    private JobInfoService jobInfoService;

    public QuartzJob() {
        this.jobInvoker = BeanUtil.getBean(JobInvoker.class);
        this.jobLogService = BeanUtil.getBean(JobLogService.class);
        this.jobInfoService = BeanUtil.getBean(JobInfoService.class);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 获取数据
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        JobApp jobApp = (JobApp) jobDataMap.get("JobApp");
        JobInfo jobInfo = (JobInfo) jobDataMap.get("JobInfo");

        log.info("[Mnsx-Job]任务={}开始执行", jobInfo.getJobName());

        // 准备日志
        JobLog jobLog = new JobLog();
        jobLog.setJobId(jobInfo.getId());
        jobLog.setTriggerStartTime(new Date());

        try {
            jobRun(jobApp, jobInfo, jobLog);
            jobLog.setTriggerResult((int)SUCCESS);
            jobLog.setTriggerMsg("success");
        } catch (Throwable t) {
            String message = ThrowableUtil.getThrowableStackTrace(t);
            log.warn("[Mnsx-Job]任务={}执行存在异常={}", jobInfo.getJobName(), message);
            jobLog.setTriggerResult(ERROR);
            jobLog.setTriggerMsg("error: " + message);
        }
        jobLog.setTriggerEndTime(new Date());

        jobInfo.setTriggerLastTime(jobExecutionContext.getFireTime());
        jobInfo.setTriggerNextTime(jobExecutionContext.getNextFireTime());

        // 保存日志
        jobLogService.save(jobLog);

        // 保存任务信息
        jobInfoService.saveOrUpdate(jobInfo);

        log.info("[Mnsx-Job]任务执行成功");
    }

    private void jobRun(JobApp jobApp, JobInfo jobInfo, JobLog jobLog) {
        // 获取地址
        List<String> addressList = Arrays.stream(jobApp.getAddressList().split(","))
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
        // 获取迭代器
        Iterator<String> iterator = addressList.iterator();
        JobResponse response = null;
        // 已经执行的地址
        ArrayList<String> hasInvokeAddress = new ArrayList<>();
        // 失败重试此处
        int failRetryCount = jobInfo.getFailRetryMaxCount(), readyRetryCount = -1;
        // 失败重试或者使用下一个地址
        while (iterator.hasNext() && ++readyRetryCount <= failRetryCount) {
            String address = iterator.next();
            hasInvokeAddress.add(address);
            try {
                response = jobInvoker.invoke(address, jobInfo.getJobName(), jobInfo.getRunParam());
                // 有一个执行成功了直接跳出循环
                if (response.isOk()) {
                    break;
                }
                log.warn("任务={}在地址为{}的客户端执行失败", jobInfo.getJobName(), address);
            } catch (Exception e) {
                String msg = ThrowableUtil.getThrowableStackTrace(e);
                log.warn("任务={}在地址为{}的客户端执行存在异常={}", jobInfo.getJobName(), address, msg);
                response = JobResponse.error("任务执行失败: " + msg);
            }
            iterator.remove();
        }

        if (Objects.isNull(response)) {
            response = JobResponse.error("没有进行任务调用");
        }

        // 完成日志
        jobLog.setRunResult(response.getCode());
        jobLog.setRunMsg(response.getMsg());
        jobLog.setFailRetryCount(Objects.equals(readyRetryCount, -1) ? 0 : readyRetryCount);
        jobLog.setAddressList(hasInvokeAddress.stream().reduce((s1, s2) -> s1 + "," + s2).orElse(""));
    }
}
