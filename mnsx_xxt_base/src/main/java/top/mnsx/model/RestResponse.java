package top.mnsx.model;

import lombok.Data;
import lombok.ToString;

/**
 * 相应类
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Data
@ToString
public class RestResponse<T> {

    // 响应码
    private int code;

    // 消息
    private String msg;

    // 实体
    private T result;

    public RestResponse() {
        this(200, "success");
    }

    public RestResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 成功
     * @param result 实例
     * @param <T> 泛型
     * @return RestResponse
     */
    public static <T> RestResponse<T> success(T result) {
        RestResponse<T> response = new RestResponse<>();
        response.setResult(result);
        return response;
    }

    /**
     * 成功
     * @param result 实例
     * @param msg 消息
     * @param <T> 泛型
     * @return RestResponse
     */
    public static <T> RestResponse<T> success(T result, String msg) {
        RestResponse<T> response = new RestResponse<>();
        response.setResult(result);
        response.setMsg(msg);
        return response;
    }

    /**
     * 失败
     * @param result 实例
     * @param msg 消息
     * @param <T> 泛型
     * @return 返回RestResponse
     */
    public static <T> RestResponse<T> validFail(T result, String msg) {
        RestResponse<T> response = new RestResponse<>();
        response.setCode(-1);
        response.setResult(result);
        response.setMsg(msg);
        return response;
    }
}
