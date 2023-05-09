package top.mnsx.exception;

import java.io.Serializable;

/**
 * 错误响应类
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public class RestErrorResponse implements Serializable {
    // 错误信息
    private String errMessage;

    public RestErrorResponse(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
