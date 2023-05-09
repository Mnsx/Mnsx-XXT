package top.mnsx.media.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Data
@ToString
public class UploadFileParamsDto {
    private String fileName;

    private Integer fileType;

    private Long fileSize;
}
