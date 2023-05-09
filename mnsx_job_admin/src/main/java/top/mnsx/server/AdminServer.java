package top.mnsx.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.mnsx.config.JobProperties;
import top.mnsx.handler.AppToAdminRequestHandler;
import top.mnsx.protocol.MessageCodecSharable;
import top.mnsx.protocol.ProtocolFrameDecoder;
import top.mnsx.utils.ThrowableUtil;

/**
 * netty 服务端
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
@Component
public class AdminServer implements CommandLineRunner {

    @Autowired
    private AppToAdminRequestHandler appToAdminRequestHandler;

    @Override
    public void run(String... args) throws Exception {
       bootstrap();
    }

    private void bootstrap() {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        LoggingHandler loggingHandler = new LoggingHandler();

        MessageCodecSharable messageCodecSharable = new MessageCodecSharable();

        try {
            log.info("[Mnsx-Job]启动Netty服务端...");
            Channel channel = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            // 添加协议解析处理器
                            ch.pipeline().addLast(new ProtocolFrameDecoder());
                            // 添加日志处理器
                            ch.pipeline().addLast(loggingHandler);
                            // 添加消息编解码处理器
                            ch.pipeline().addLast(messageCodecSharable);
                            // 添加注册请求
                            ch.pipeline().addLast(appToAdminRequestHandler);
                            // 添加心跳功能，五秒没有收到客户端的消息，将会关闭该通道
                            ch.pipeline().addLast(new IdleStateHandler(5, 0, 0));
                            ch.pipeline().addLast(new ChannelDuplexHandler() {
                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                    IdleStateEvent event = (IdleStateEvent) evt;
                                    if (event.state() == IdleState.READER_IDLE) {
                                        ctx.channel().close();
                                        log.info("[Mnsx-Job]通道={}已经关闭", ctx.channel());
                                    }
                                }
                            });
                        }
                    })
                    .bind(7777)
                    .sync()
                    .channel();

            log.info("[Mnsx-Job]Netty服务端创建成功");
            channel.closeFuture().sync();
            log.info("[Mnsx-Job]服务端关闭");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("[Mnsx-Job]Netty服务端创建存在异常={}", ThrowableUtil.getThrowableStackTrace(e));
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
