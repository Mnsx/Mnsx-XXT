package top.mnsx.media.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @Author Mnsx_x
 */
@Data
@ToString
public class QueryMediaParamsDto {

    private String fileName;
    private Integer fileType;
    private Integer auditStatus;
}
