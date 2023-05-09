package top.mnsx.media.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.mnsx.media.dto.QueryMediaParamsDto;
import top.mnsx.media.dto.UploadFileParamsDto;
import top.mnsx.media.dto.UploadFileResultDto;
import top.mnsx.media.po.MediaFile;
import top.mnsx.media.service.MediaFileService;
import top.mnsx.model.PageParams;
import top.mnsx.model.PageResult;
import top.mnsx.model.RestResponse;

import java.io.File;
import java.io.IOException;

/**
 * @Author Mnsx_x
 */
@RestController
@CrossOrigin
@RequestMapping("/media-file")
public class MediaFileController {


    @Autowired
    MediaFileService mediaFileService;

    @PostMapping
    public RestResponse<PageResult<MediaFile>> list(PageParams pageParams){
        return RestResponse.success(mediaFileService.queryMediaFile(pageParams));

    }

    @RequestMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResultDto upload(@RequestPart("fileData")MultipartFile fileData) throws IOException {

        // 准备上传数据信息
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        // 原始文件名称
        uploadFileParamsDto.setFileName(fileData.getOriginalFilename());
        // 文件大小
        uploadFileParamsDto.setFileSize(fileData.getSize());
        // 文件类型
        uploadFileParamsDto.setFileType(3);

        File tempFile = File.createTempFile("minio", ".temp");
        fileData.transferTo(tempFile);

        // 文件路径
        String localFilePath = tempFile.getAbsolutePath();

        return mediaFileService.uploadFile(uploadFileParamsDto, localFilePath);
    }
}
