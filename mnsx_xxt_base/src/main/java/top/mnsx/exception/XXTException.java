package top.mnsx.exception;

/**
 * XXT专属异常
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public class XXTException extends RuntimeException {
    // 异常信息
    private String errMessage;

    public XXTException() {
        super();
    }

    public XXTException(String message) {
        super(message);
        this.errMessage = message;
    }

    public String getErrMessage() {
        return errMessage;
    }

    /**
     * 返回XXTException异常
     * @param errMessage 异常消息
     */
    public static void cast(String errMessage) {
        throw new XXTException(errMessage);
    }

    /**
     * 返回XXTException
     * @param commonError 常见异常枚举类
     */
    public static void cast(CommonError commonError) {
        throw new XXTException(commonError.getErrMessage());
    }
}
