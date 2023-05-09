package top.mnsx.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.simpleframework.xml.Version;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.mnsx.exception.XXTException;
import top.mnsx.media.dto.QueryMediaParamsDto;
import top.mnsx.media.dto.UploadFileParamsDto;
import top.mnsx.media.dto.UploadFileResultDto;
import top.mnsx.media.mapper.MediaFileMapper;
import top.mnsx.media.mapper.MediaProcessMapper;
import top.mnsx.media.po.MediaFile;
import top.mnsx.media.po.MediaProcess;
import top.mnsx.media.service.MediaFileService;
import top.mnsx.model.PageParams;
import top.mnsx.model.PageResult;
import top.mnsx.model.RestResponse;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author Mnsx_x
 */
@Service
@Slf4j
public class MediaFileServiceImpl implements MediaFileService {

    @Resource
    MediaFileMapper mediaFileMapper;
    @Autowired
    MinioClient minioClient;
    @Autowired
    MediaFileService mediaFileService;
    @Resource
    MediaProcessMapper mediaProcessMapper;

    @Value("${minio.bucket.files}")
    private String bucketMediaFile;
    @Value("${minio.bucket.videoFiles}")
    private String bucketVideo;

    @Override
    public PageResult<MediaFile> queryMediaFile(PageParams pageParams) {
        //构建查询条件对象
        LambdaQueryWrapper<MediaFile> queryWrapper = new LambdaQueryWrapper<>();

        //分页对象
        Page<MediaFile> page = new Page<>(pageParams.getPageNum(), pageParams.getPageSize());

        // 查询数据内容获得结果
        Page<MediaFile> pageResult = mediaFileMapper.selectPage(page, queryWrapper);

        // 获取数据列表
        List<MediaFile> list = pageResult.getRecords();
        // 获取数据总数
        long total = pageResult.getTotal();
        // 构建结果集
        return new PageResult<>(list, total, pageParams.getPageNum(), pageParams.getPageSize());

    }

    @Override
    public UploadFileResultDto uploadFile(UploadFileParamsDto uploadFileParamsDto, String localFilePath) {

        // 获取文件名
        String filename = uploadFileParamsDto.getFileName();

        // 得到扩展名
        String extension = filename.substring(filename.lastIndexOf("."));

        // 获取mimeType
        String mimeType = getMimeType(extension);

        // 获取子目录
        String defaultFolderPath = getDefaultFolderPath();

        // 获取文件md5值
        String fileMd5 = getFileMd5(new File(localFilePath));
        String objectName = defaultFolderPath + fileMd5 + extension;

        // 将文件上传到minio
        boolean ifSuccess = addMediaFileToMinIO(localFilePath, bucketMediaFile, mimeType, objectName);
        if (!ifSuccess) {
            XXTException.cast("上传文件失败");
        }

        // 将文件信息保存到数据库
        MediaFile mediaFiles = mediaFileService.addMediaFilesToDb(uploadFileParamsDto, fileMd5, bucketMediaFile, objectName);
        if (mediaFiles == null) {
            XXTException.cast("文件上传后保存数据失败");
        }

        // 准备返回对象
        UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
        BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);

        return uploadFileResultDto;
    }

    @Transactional
    @Override
    public MediaFile addMediaFilesToDb(UploadFileParamsDto uploadFileParamsDto, String fileMd5,
                                         String bucket, String objectName) {
        // 根据文件的Md5值获取问及那媒体信息
        MediaFile mediaFiles = mediaFileMapper.selectById(fileMd5);
        if (mediaFiles == null) {
            mediaFiles = new MediaFile();
            BeanUtils.copyProperties(uploadFileParamsDto, mediaFiles);
            // 文件id
            mediaFiles.setId(fileMd5);
            // 桶
            mediaFiles.setBucket(bucket);
            // file_path
            mediaFiles.setFilePath(objectName);
            // file_id
            mediaFiles.setFileId(fileMd5);
            // url
            mediaFiles.setUrl("/" + bucket + "/" + objectName);
            // 状态
            mediaFiles.setStatus(1);
            // 审核状态
            mediaFiles.setAuditStatus(0);
            // 插入数据库
            int insert = mediaFileMapper.insert(mediaFiles);
            if (insert <= 0) {
                log.debug("想数据库保存文件失败, objectName:{}, bucket:{}", objectName, bucket);
                return null;
            }

            // 记录待处理的任务
            addWaitingTask(mediaFiles);

            return mediaFiles;
        }
        return mediaFiles;
    }

    @Override
    public RestResponse<Boolean> checkFile(String fileMd5) {

        // 查询数据库
        MediaFile mediaFiles = mediaFileMapper.selectById(fileMd5);
        if (mediaFiles != null) {
            // 如果数据库存在，再查询minio
            // 获取桶信息
            String bucket = mediaFiles.getBucket();
            // objectName
            String objectName = mediaFiles.getFilePath();
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build();

            try {
                FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
                if (inputStream != null) {
                    return RestResponse.success(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 文件不存在
        return RestResponse.success(false);
    }

    @Override
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex) {

        // 根据MD5得到分块文件的路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);

        // obejctname
        GetObjectArgs getObejctArgs = GetObjectArgs.builder()
                .bucket(bucketVideo)
                .object(chunkFileFolderPath + chunkIndex)
                .build();

        try {
            FilterInputStream inputStream = minioClient.getObject(getObejctArgs);
            if (inputStream != null) {
                return RestResponse.success(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 文件不存在
        return RestResponse.success(false);
    }

    @Override
    public RestResponse uploadChunk(String fileMd5, int chunk, String localChunkFilePath) {

        // 分块文件路径
        String chunkPath = getChunkFileFolderPath(fileMd5) + chunk;

        // mimeType
        String mimeType = getMimeType(null);

        // 将分块文件发送minio
        boolean flag = addMediaFileToMinIO(localChunkFilePath, bucketVideo, mimeType, chunkPath);
        if (!flag) {
            return RestResponse.validFail(false, "上传分块文件失败");
        }

        // 上传成功
        return RestResponse.success(true);
    }

    @Override
    public RestResponse mergeChunks(String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
        // 获取分块文件的目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        // 获取原文间名称
        String filename = uploadFileParamsDto.getFileName();
        // 获取原文件扩展名
        String extension = filename.substring(filename.lastIndexOf("."));
        // 合并后文件的ObjectName
        String newObjectName = getFilePathByMd5(fileMd5, extension);
        // 找到分块文件调用minio的sdk
        List<ComposeSource> sourceList = Stream.iterate(0, i -> ++i).limit(chunkTotal)
                .map(i -> ComposeSource.builder()
                        .bucket(bucketVideo)
                        .object(chunkFileFolderPath + i)
                        .build())
                .collect(Collectors.toList());

        // 指定合并后的文件信息
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                .bucket(bucketVideo)
                .object(newObjectName)
                .sources(sourceList)
                .build();
        // 合并文件
        try {
            minioClient.composeObject(composeObjectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("合并文件出错, bucket: {}, objectName: {}, 错误信息: {}", bucketVideo, newObjectName, e.getMessage());
            return RestResponse.validFail(false, "合并文件失败");
        }

        // 检验合并后的文件是否和源文件一致
        File source = downloadFileFromMinIo(bucketVideo, newObjectName);
        // 计算合并后文件的md5
        try (FileInputStream fileInputStream = new FileInputStream(source)) {
            String mergeFileMd5 = DigestUtils.md5Hex(fileInputStream);
            // 比较原始和合并后是否相同
            if (!fileMd5.equals(mergeFileMd5)) {
                log.error("文件校验失败, 原始文件:{}, 合并文件:{}", fileMd5, mergeFileMd5);
                return RestResponse.validFail(false, "文件校验失败");
            }
            // 注入文件大小
            uploadFileParamsDto.setFileSize(source.length());
        } catch (IOException e) {
            e.printStackTrace();
            return RestResponse.validFail(false, "文件校验失败");
        }

        // 将文件信息入库
        MediaFile mediaFiles = addMediaFilesToDb(uploadFileParamsDto, fileMd5, bucketVideo, newObjectName);
        if (mediaFiles == null) {
            return RestResponse.validFail(false, "文件入库失败");
        }

        // 清理分块文件
        clearChunkFiles(chunkFileFolderPath, chunkTotal);

        return RestResponse.success(true);
    }

    @Override
    public File downloadFileFromMinIo(String bucket, String objectName) {
        // 临时文件
        File minIoFile = null;
        FileOutputStream outputStream = null;
        try {
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
            // 创建临时文件
            minIoFile = File.createTempFile("minio", ".merge");
            outputStream = new FileOutputStream(minIoFile);
            IOUtils.copy(inputStream, outputStream);
            return minIoFile;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 添加待处理的任务
     * @param mediaFiles mediaFiles
     */
    private void addWaitingTask(MediaFile mediaFiles) {
        // 获取文件名
        String filename = mediaFiles.getFileName();
        // 文件扩展名
        String extension = filename.substring(filename.lastIndexOf("."));
        // 获取文件的mimeType
        String mimeType = getMimeType(extension);

        // 判断扩展名是否为avi
        if (mimeType.equals("video/x-msvideo")) {
            MediaProcess mediaProcess = new MediaProcess();
            BeanUtils.copyProperties(mediaFiles, mediaProcess);
            // 状态 未处理
            mediaProcess.setStatus(0);
            mediaProcess.setCreateTime(new Date());

            mediaProcessMapper.insert(mediaProcess);
        }
        // todo: 其他类型转换
    }

    /**
     * 文件合并后，将分块文件清除
     * @param chunkFileFolderPath 分块文件路径
     * @param chunkTotal 分块个数
     */
    private void clearChunkFiles(String chunkFileFolderPath, int chunkTotal) {
        Iterable<DeleteObject> objects = Stream.iterate(0, i -> ++i).limit(chunkTotal)
                .map(i -> new DeleteObject(chunkFileFolderPath + i))
                .collect(Collectors.toList());

        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                .bucket(bucketVideo)
                .objects(objects)
                .build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);

        // 清除后，有bug无法成功，需要遍历
        results.forEach(f -> {
            try {
                DeleteError deleteError = f.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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

    /**
     * 根据文件md5获取分块的路径
     * @param fileMd5 文件MD5值
     * @return String
     */
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + "chunk" + "/";
    }

    /**
     * 根据文件扩展名获取文件mimeType
     * @param extension 扩展名
     * @return String
     */
    private String getMimeType(String extension) {
        if (extension == null) {
            extension = "";
        }
        // 根据扩展名得到mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }

    /**
     * 将媒体文件添加到Minio中
     * @param localFilePath 当前文件路径
     * @param bucket 桶
     * @param mimeType minmeType
     * @param objectName 对象
     * @return boolean
     */
    public boolean addMediaFileToMinIO(String localFilePath, String bucket, String mimeType, String objectName) {
        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .filename(localFilePath)
                    .object(objectName)
                    .contentType(mimeType)
                    .build();
            // 上传文件
            minioClient.uploadObject(uploadObjectArgs);
            log.info("上传文件成功, bucket:{}, objectName:{}", bucket, objectName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传文件出错, bucket:{}, objectName:{}, 错误信息:{}", bucket, objectName, e.getMessage());
        }
        return false;
    }

    @Override
    public MediaFile getFileById(String mediaId) {
        return mediaFileMapper.selectById(mediaId);
    }

    /**
     * 获取默认路径
     * @return String
     */
    private String getDefaultFolderPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date()).replace("-", "/") + "/";
    }

    /**
     * 获取文件的MD5值
     * @param file 目标文件
     * @return String
     */
    private String getFileMd5(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return DigestUtils.md5Hex(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
