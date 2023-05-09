package top.mnsx.media.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.mnsx.media.dto.UploadFileParamsDto;
import top.mnsx.media.service.MediaFileService;
import top.mnsx.model.RestResponse;

import java.io.File;
import java.io.IOException;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@CrossOrigin
@RestController
@RequestMapping("/big-file")
public class BigFileController {

    @Autowired
    MediaFileService mediaFileService;

    @PostMapping("/checkFile")
    public RestResponse<Boolean> checkFile(@RequestParam("fileMd5") String fileMd5) {
        return mediaFileService.checkFile(fileMd5);
    }

    @PostMapping("/checkChunk")
    public RestResponse<Boolean> checkChunk(@RequestParam("fileMd5") String fileMd5,
                                            @RequestParam("chunk") int chunk) {
        return mediaFileService.checkChunk(fileMd5, chunk);
    }

    // todo
    @PostMapping("/open/uploadChunk")
    public RestResponse uploadChunk(@RequestParam("file")MultipartFile file,
                                    @RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("chunk") int chunk) throws IOException {
        // 创建一个临时文件
        File tempFile = File.createTempFile("minio", ".temp");
        file.transferTo(tempFile);
        // 文件路径
        String localFilePath = tempFile.getAbsolutePath();

        return mediaFileService.uploadChunk(fileMd5, chunk, localFilePath);
    }

    @PostMapping("/mergeChunks")
    public RestResponse mergeChunks(@RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("fileName") String fileName,
                                    @RequestParam("chunkTotal") int chunkTotal) {
        // 构造文件信息
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFileName(fileName);
        uploadFileParamsDto.setFileType(1);
        return mediaFileService.mergeChunks(fileMd5, chunkTotal, uploadFileParamsDto);
    }
}
