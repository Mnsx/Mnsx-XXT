package top.mnsx.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络工具类
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
public class NetUtil {

    private NetUtil() {}

    public static String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("[Mnsx-Job] get ip address has exception={}", e.getMessage());
            throw new RuntimeException("get ip address has exception!");
        }
    }
}
