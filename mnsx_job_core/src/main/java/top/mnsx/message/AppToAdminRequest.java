package top.mnsx.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 将应用上传到调度器的请求
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Data
public class AppToAdminRequest extends AbstractMessage implements Serializable {

    // 应用名称
    private String appName;
    // 应用简介
    private String appDesc;
    // 应用地址
    private String address;

    @Override
    public int getMessageType() {
        return APP_TO_ADMIN_REQUEST;
    }
}
