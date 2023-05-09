package top.mnsx.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 任务执行请求消息
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Data
public class JobInvokeRequest extends AbstractMessage implements Serializable {
    // 序列码
    public static final long serialVersionUID = 42L;

    // handler名称
    private String name;

    // 参数
    private String params;

    @Override
    public int getMessageType() {
        return JOB_INVOKE_REQUEST;
    }
}
