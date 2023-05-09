package top.mnsx.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.mnsx.enums.CreateWayEnum;
import top.mnsx.enums.EnabledEnum;
import top.mnsx.utils.Result;
import top.mnsx.domain.po.JobApp;
import top.mnsx.message.AppToAdminRequest;
import top.mnsx.message.JobResponse;
import top.mnsx.service.JobAppService;

/**
 * 应用注册信息处理器
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@ChannelHandler.Sharable
@Component
@Slf4j
public class AppToAdminRequestHandler extends SimpleChannelInboundHandler<AppToAdminRequest> {

    @Autowired
    private JobAppService jobAppService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AppToAdminRequest msg) throws Exception {
        JobApp jobApp = new JobApp();
        BeanUtils.copyProperties(msg, jobApp);
        jobApp.setAddressList(msg.getAddress());
        Result<JobApp> result = jobAppService.insert(jobApp);
        log.info("result" + result.isErr());
        if (result.isErr()) {
            log.warn("[Mnsx-Job] 客户端={}注册失败", jobApp.getAddressList());
        } else {
            log.info("[Mnsx-Job] 客户端={}注册成功", jobApp.getAddressList());
            ctx.writeAndFlush(JobResponse.success(JSON.toJSONString(jobApp)));
        }
    }
}
