package top.mnsx.handler;

import top.mnsx.message.JobResponse;

/**
 * Handler规范
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public interface IJobHandler {
    public JobResponse execute(String params) throws Exception;
}