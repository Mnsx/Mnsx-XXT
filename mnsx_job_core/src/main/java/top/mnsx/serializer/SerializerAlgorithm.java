package top.mnsx.serializer;

import java.io.*;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public enum SerializerAlgorithm implements Serializer {

    // jdk实现
    jdk {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return (T) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("反序列化错误: " + e.getMessage());
            }
        }

        @Override
        public <T> byte[] serialize(T object) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                return baos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("序列化错误: " + e.getMessage());
            }
        }
    };

    /**
     * 通过序列化算法编号获取序列化算法
     * @param type 序列化编号
     * @return 返回序列化算法
     */
    public static SerializerAlgorithm getAlgorithm(int type) {
        SerializerAlgorithm[] types = SerializerAlgorithm.values();
        if (type < 0 || type >= types.length) {
            throw new RuntimeException("不存在该序列化算法");
        }
        return types[type];
    }
}
