package top.mnsx.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.mnsx.media.mapper.MediaFileMapper;
import top.mnsx.media.mapper.MediaProcessHistoryMapper;
import top.mnsx.media.mapper.MediaProcessMapper;
import top.mnsx.media.po.MediaFile;
import top.mnsx.media.po.MediaProcess;
import top.mnsx.media.po.MediaProcessHistory;
import top.mnsx.media.service.MediaFileProcessService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Service
public class MediaFileProcessServiceImpl implements MediaFileProcessService {
    @Resource
    private MediaProcessMapper mediaProcessMapper;
    @Resource
    private MediaProcessHistoryMapper mediaProcessHistoryMapper;
    @Resource
    private MediaFileMapper mediaFileMapper;

    @Override
    public void saveProcessFinishStatus(Integer processId, Integer status, String fileId, String url, String errorMsg) {
        // 查找需要更新的任务
        MediaProcess mediaProcess = mediaProcessMapper.selectById(processId);
        if (mediaProcess == null) {
            return ;
        }
        // 任务执行失败
        if (status == 3) {
            // 更新process
            mediaProcess.setStatus(3);
            mediaProcess.setFailMsg(errorMsg);
            mediaProcessMapper.updateById(mediaProcess);
            return ;
        }
        // 任务执行成功
        // 根据id查询课程信息
        MediaFile mediaFile = mediaFileMapper.selectById(fileId);

        // 更新media_file中的url
        mediaFile.setUrl(url);
        mediaFileMapper.updateById(mediaFile);

        // 更新MediaProcess中的信息
        mediaProcess.setStatus(2);
        mediaProcess.setFinishTime(new Date());
        mediaProcess.setUrl(url);
        mediaProcessMapper.updateById(mediaProcess);

        // 将MediaProcess表记录插入到MediaProcessHistory中
        MediaProcessHistory mediaProcessHistory = new MediaProcessHistory();
        BeanUtils.copyProperties(mediaProcess, mediaProcessHistory);
        mediaProcessHistoryMapper.insert(mediaProcessHistory);

        // 从MediaProcess中删除任务
        mediaProcessMapper.deleteById(processId);
    }

    @Override
    public MediaProcess getMediaProcess() {
        LambdaQueryWrapper<MediaProcess> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(MediaProcess::getCreateTime);
        List<MediaProcess> mediaProcesses = mediaProcessMapper.selectList(wrapper);
        if (!mediaProcesses.isEmpty()) {
            return mediaProcesses.get(0);
        }
        return null;
    }
}
