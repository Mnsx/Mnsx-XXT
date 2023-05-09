package top.mnsx.config;

import top.mnsx.serializer.SerializerAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * 读取配置获取序列化方式
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public class SerializerAlgorithmConfig {
    static Properties properties;

    static {
        // 读取Properties中的数据
        try (
            InputStream in = SerializerAlgorithmConfig.class.getResourceAsStream("/application.properties")
        ) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前的序列化方式（默认为Jdk）
     * @return
     */
    public static SerializerAlgorithm getSerializerAlgorithm() {
        String value = properties.getProperty("serializer.algorithm");
        if (value == null) {
            return SerializerAlgorithm.jdk;
        } else {
            return SerializerAlgorithm.valueOf(value.toLowerCase());
        }
    }
}
