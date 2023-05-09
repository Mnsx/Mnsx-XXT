package top.mnsx.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.mnsx.message.JobResponse;

/**
 * JobResponse处理器
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
public class JobResponseHandler extends SimpleChannelInboundHandler<JobResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JobResponse msg) throws Exception {
        log.info("{}", msg);
    }
}
