package top.mnsx.domain.vo;

import lombok.Data;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Data
public class JobInfoPageVo {

    private Integer id;
    private String appName;
    private String jobName;
    private String jobDesc;
    private String creator;
    private String createTime;
    private String runCron;
    private Integer runStrategy;
    private String runParam;
    private String triggerNextTime;
    private Integer enabled;
}
