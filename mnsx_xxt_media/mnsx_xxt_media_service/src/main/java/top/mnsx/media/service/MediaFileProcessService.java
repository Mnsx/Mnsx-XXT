package top.mnsx.media.service;

import top.mnsx.media.po.MediaProcess;
import top.mnsx.model.PageParams;

import java.util.List;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public interface MediaFileProcessService {
    /**
     * 更新任务执行流程状态
     * @param processId 流程编号
     * @param status 状态
     * @param fileId 文件编号
     * @param url url
     * @param errorMsg 错误信息
     */
    void saveProcessFinishStatus(Integer processId, Integer status, String fileId, String url, String errorMsg);

    /**
     * 获取任务
     * @return MediaProcess
     */
    MediaProcess getMediaProcess();
}
