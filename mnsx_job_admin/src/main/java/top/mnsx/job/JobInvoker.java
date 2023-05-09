package top.mnsx.job;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import top.mnsx.config.SerializerAlgorithmConfig;
import top.mnsx.handler.AppToAdminRequestHandler;
import top.mnsx.message.JobInvokeRequest;
import top.mnsx.message.JobResponse;
import top.mnsx.server.AdminServer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务处理类
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Component
@Slf4j
public class JobInvoker {
    @Autowired
    private RestTemplate restTemplate;

    private static final String PREFIX = "http://";

    // todo: 动态匹配任务
    private static final String PATH = "/media/mnsx/job/invoke";

    public JobResponse invoke(String url, String jobHandler, String params) {
        JobInvokeRequest req = new JobInvokeRequest();
        req.setName(jobHandler);
        req.setParams(params);

        byte[] dataBytes = SerializerAlgorithmConfig.getSerializerAlgorithm().serialize(req);
        return restTemplate.postForObject(PREFIX + url + PATH, dataBytes, JobResponse.class);
    }
}
