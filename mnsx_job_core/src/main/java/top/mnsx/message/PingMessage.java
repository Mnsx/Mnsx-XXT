package top.mnsx.message;

import java.io.Serializable;

/**
 * 心跳包
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public class PingMessage extends AbstractMessage implements Serializable {
    public static final long serializableID = 77L;

    public PingMessage(int sequenceId) {
        setSequenceId(sequenceId);
    }

    @Override
    public int getMessageType() {
        return PING_MESSAGE_REQUEST;
    }
}
