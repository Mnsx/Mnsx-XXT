package top.mnsx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.mnsx.utils.Result;
import top.mnsx.service.JobLogService;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@RestController
@RequestMapping("/log")
public class JobLogController {
    @Autowired
    private JobLogService jobLogService;

    @GetMapping("/query/{jobId}")
    public Result<?> queryById(@PathVariable("jobId") Integer jobId) {
        return jobLogService.queryByJobId(jobId);
    }
}
