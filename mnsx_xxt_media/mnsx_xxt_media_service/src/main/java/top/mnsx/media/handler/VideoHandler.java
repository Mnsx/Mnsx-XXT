package top.mnsx.media.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.mnsx.handler.IJobHandler;
import top.mnsx.media.po.MediaProcess;
import top.mnsx.media.service.MediaFileProcessService;
import top.mnsx.media.service.MediaFileService;
import top.mnsx.message.JobResponse;
import top.mnsx.model.RestResponse;
import top.mnsx.utils.Mp4VideoUtil;

import java.io.File;
import java.io.IOException;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Component
public class VideoHandler implements IJobHandler {
    private String ffmpegPath = "D:\\Application\\ffmpeg\\ffmpeg-master-latest-win64-gpl\\bin\\ffmpeg.exe";

    @Autowired
    private MediaFileProcessService mediaFileProcessService;
    @Autowired
    private MediaFileService mediaFileService;

    @Override
    public JobResponse execute(String param) throws Exception {
        // 查询待处理的任务
        MediaProcess mediaProcess = mediaFileProcessService.getMediaProcess();

        if (mediaProcess == null) {
            return JobResponse.error("没有任务正在等待");
        }

        Integer processId = mediaProcess.getId();
        // 下载minio视频到本地
        String bucket = mediaProcess.getBucket();
        String objectName = mediaProcess.getFilePath();
        File file = mediaFileService.downloadFileFromMinIo(bucket, objectName);
        String fileId = mediaProcess.getFileId();
        if (file == null) {
            // 保存任务处理失败的结果
            mediaFileProcessService.saveProcessFinishStatus(processId, 3, fileId, null, "下载文件视频失败");
            return JobResponse.error("下载文件视频出错, 任务id=" + processId);
        }

        // 获取文件Md5值
        String md5 = mediaProcess.getFileId();

        // 执行视频转码
        // 获取视频原始路径
        String videoPath = file.getAbsolutePath();
        // 转换成MP4后的名称
        String mp4Name = md5 + ".mp4";
        // 创建一个临时文件转换MP4文件的路径
        File tempFile = null;
        try {
            tempFile = File.createTempFile("minio", ".mp4");
        } catch (IOException e) {
            e.printStackTrace();
            // 保存任务处理失败的结果
            mediaFileProcessService.saveProcessFinishStatus(processId, 3, fileId, null, "创建临时文件失败");
            return JobResponse.error("创建临时文件异常, 任务id=" + processId + ", 异常=" + e.getMessage());
        }
        String mp4Path = tempFile.getAbsolutePath();

        // 创建工具类对象
        Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpegPath, videoPath, mp4Name, mp4Path);

        // 开始视频转换
        String result = videoUtil.generateMp4();
        if (!result.equals("success")) {
            // 保存任务处理失败的结果
            mediaFileProcessService.saveProcessFinishStatus(processId, 3, fileId, null, "视频转码失败, 失败原因=" + result);
            return JobResponse.error("视频转码失败, 任务id=" + processId + ", 失败愿意=" + result);
        }

        // 上传文件到MinIo
        boolean flag = mediaFileService.addMediaFileToMinIO(tempFile.getAbsolutePath(), bucket, "video/mp4", getFilePathByMd5(md5, ".mp4"));
        if (!flag) {
            mediaFileProcessService.saveProcessFinishStatus(processId, 3, fileId, null, result);
            return JobResponse.error("上传MP4到MinIO失败，原因=" + result + ", 桶=" + bucket + ", 对象名" + objectName);
        }

        // 拼接
        String url = getFilePathByMd5(md5, ".mp4");

        // 任务状态成功
        mediaFileProcessService.saveProcessFinishStatus(processId, 2, fileId, url, "创建临时文件失败");

        return JobResponse.success("任务调度成功");
    }

    /**
     * 根据md5获取文件路径
     * @param fileMd5 md5
     * @param fileExt 文件后缀
     * @return String
     */
    private String getFilePathByMd5(String fileMd5, String fileExt) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + fileMd5 + fileExt;
    }
}
