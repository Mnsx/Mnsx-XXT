package top.mnsx.message;

import io.netty.util.concurrent.Promise;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务请求响应消息
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@ToString
public class JobResponse extends AbstractMessage implements Serializable {
    public static final long serializableID = 77L;

    public static final Integer SUCCESS_CODE = 1;

    public static final Integer ERROR_CODE = 0;

    @Getter
    private Integer code;

    @Getter
    private String msg;

    public JobResponse() {
        this.code = SUCCESS_CODE;
    }

    public JobResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static JobResponse success(String msg) {
        return new JobResponse(SUCCESS_CODE, msg);
    }

    public static JobResponse error(String msg) {
        return new JobResponse(ERROR_CODE, msg);
    }

    public boolean isOk() {
        return Objects.equals(this.code, SUCCESS_CODE);
    }

    @Override
    public int getMessageType() {
        return JOB_RESPONSE;
    }

}
