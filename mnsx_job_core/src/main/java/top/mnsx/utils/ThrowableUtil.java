package top.mnsx.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
public class ThrowableUtil {

    private ThrowableUtil() {
    }

    public static String getThrowableStackTrace(Throwable throwable) {
        if (Objects.isNull(throwable)) {
            return null;
        }

        StringWriter stringWriter = null;
        PrintWriter printWriter = null;

        try {
            stringWriter = new StringWriter();
            printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);
            printWriter.flush();
            stringWriter.flush();
            return stringWriter.toString();
        } finally {
            try {
                if (Objects.nonNull(stringWriter)) {
                    stringWriter.close();
                }
                if (Objects.nonNull(printWriter)) {
                    printWriter.close();
                }
            } catch (IOException e) {
                log.error("流对象关闭时出现异常", e);
            }
        }
    }
}