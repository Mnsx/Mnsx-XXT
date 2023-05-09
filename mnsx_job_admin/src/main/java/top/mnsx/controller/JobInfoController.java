package top.mnsx.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.mnsx.utils.Result;
import top.mnsx.domain.dto.JobInfoModifyDto;
import top.mnsx.domain.po.JobInfo;
import top.mnsx.domain.dto.JobInfoAddDto;
import top.mnsx.domain.vo.JobInfoPageVo;
import top.mnsx.enums.CreateWayEnum;
import top.mnsx.service.JobInfoService;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
@RestController
@RequestMapping("/info")
public class JobInfoController {
    @Autowired
    private JobInfoService jobInfoService;

    @GetMapping("/page_query")
    public Result<Page<JobInfoPageVo>> pageQuery(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return jobInfoService.queryByPage(pageNum, pageSize);
    }

    @PostMapping("/add")
    public Result<JobInfo> add(@RequestBody JobInfoAddDto addVo) {
        JobInfo jobInfo = new JobInfo();
        BeanUtils.copyProperties(addVo, jobInfo);
        jobInfo.setCreateWay(CreateWayEnum.MANUAL.getCode());

        return jobInfoService.insert(jobInfo);
    }

    @PutMapping("/modify")
    public Result<JobInfo> modify(@RequestBody JobInfoModifyDto jobInfoModifyDto) {
        JobInfo jobInfo = new JobInfo();
        BeanUtils.copyProperties(jobInfoModifyDto, jobInfo);
        return jobInfoService.update(jobInfo);
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable("id") Integer id) {
        return jobInfoService.delete(id);
    }

    @PutMapping("/enable/{id}")
    public Result<?> enable(@PathVariable("id") Integer id) {
        return jobInfoService.enable(id);
    }

    @PutMapping("/disable/{id}")
    public Result<?> disable(@PathVariable("id") Integer id) {
        return jobInfoService.disable(id);
    }

    @PostMapping("/run/{id}")
    public Result<?> run(@PathVariable("id") Integer id) {
        return jobInfoService.run(id);
    }
}
