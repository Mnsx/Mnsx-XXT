package top.mnsx.media.service;

import top.mnsx.media.dto.QueryMediaParamsDto;
import top.mnsx.media.dto.UploadFileParamsDto;
import top.mnsx.media.dto.UploadFileResultDto;
import top.mnsx.media.po.MediaFile;
import top.mnsx.model.PageParams;
import top.mnsx.model.PageResult;
import top.mnsx.model.RestResponse;

import java.io.File;

/**
 * @Author Mnsx_x
 */
public interface MediaFileService {


    /**
     * 查询所有的媒体文件
     * @param pageParams 分页参数
     * @return pageResult
     */
    public PageResult<MediaFile> queryMediaFile(PageParams pageParams);

    /**
     * 上传文件
     * @param uploadFileParamsDto 课程参数
     * @param localFilePath 文件本地路径
     * @return UploadFileResultDto
     */
    public UploadFileResultDto uploadFile(UploadFileParamsDto uploadFileParamsDto, String localFilePath);

    /**
     * 将媒体文件保存到数据库中
     * @param uploadFileParamsDto 上传文件参数Dto
     * @param fileMd5 文件的MD5值
     * @param bucketMediaFile 桶
     * @param objectName 对象名称
     * @return MediaFile
     */
    MediaFile addMediaFilesToDb(UploadFileParamsDto uploadFileParamsDto, String fileMd5, String bucketMediaFile, String objectName);

    /**
     * 检查文件是否存在
     * @param fileMd5 文件MD5值
     * @return RestResponse
     */
    public RestResponse<Boolean> checkFile(String fileMd5);

    /**
     * 检查分块是否存在
     * @param fileMd5 文件Md5值
     * @param chunkIndex 分块索引
     * @return RestResponse
     */
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex);

    /**
     * 上传分块
     * @param fileMd5 文件md5值
     * @param chunk 分块索引
     * @param localChunkFilePath 路径
     * @return RestResponse
     */
    public RestResponse uploadChunk(String fileMd5, int chunk, String localChunkFilePath);

    /**
     * 合并分块
     * @param fileMd5 文件md5值
     * @param chunkTotal 分块总喝
     * @param uploadFileParamsDto 文件信息
     * @return RestResponse
     */
    public RestResponse mergeChunks(String fileMd5, int chunkTotal,
                                    UploadFileParamsDto uploadFileParamsDto);

    /**
     * 从MinIo下载文件
     * @param bucket 桶
     * @param objectName 对象名
     * @return File
     */
    public File downloadFileFromMinIo(String bucket, String objectName);

    /**
     * 添加媒体文件到Minio
     * @param localFilePath 本地文件路径
     * @param mimeType mimeType
     * @param bucket 桶
     * @param objectName 对象名
     * @return boolean
     */
    public boolean addMediaFileToMinIO(String localFilePath, String bucket, String mimeType, String objectName);

    /**
     * 通过编号获取媒体文件
     * mediaId 媒体编号
     * @return MediaFile
     */
    MediaFile getFileById(String mediaId);
}
