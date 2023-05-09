package top.mnsx.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.mnsx.utils.Result;
import top.mnsx.domain.po.JobApp;
import top.mnsx.domain.dto.JobAppAddDto;
import top.mnsx.domain.dto.JobAppModifyDto;
import top.mnsx.enums.CreateWayEnum;
import top.mnsx.service.JobAppService;

import java.util.List;
import java.util.Map;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
@RestController
@RequestMapping("/app")
public class JobAppController {

    @Autowired
    private JobAppService jobAppService;

    @GetMapping("/page_query")
    public Result<Page<JobApp>> pageQuery(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return jobAppService.queryByPage(pageNum, pageSize);
    }

    @PostMapping("/add")
    public Result<JobApp> manualAdd(@RequestBody JobAppAddDto jobAppAddVo) {
        JobApp jobApp = new JobApp();
        BeanUtils.copyProperties(jobAppAddVo, jobApp);
        jobApp.setCreateWay(CreateWayEnum.MANUAL.getCode());

        return jobAppService.insert(jobApp);
    }

    @PutMapping("/modify")
    public Result<JobApp> modify(@RequestBody JobAppModifyDto jobAppModifyDto) {
        JobApp jobApp = new JobApp();
        BeanUtils.copyProperties(jobAppModifyDto, jobApp);
        return jobAppService.update(jobApp);
    }

    @GetMapping("/query_all")
    public Result<List<Map<String, String>>> queryAllName() {
        return jobAppService.queryAllName();
    }
}
