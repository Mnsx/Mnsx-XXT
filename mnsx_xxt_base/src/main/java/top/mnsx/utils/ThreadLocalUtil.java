package top.mnsx.utils;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 * ThreadLocal工具类
 */
public class ThreadLocalUtil {
    private static final ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    public static void add(Object temp) {
        threadLocal.set(temp);
    }

    public static Object get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
