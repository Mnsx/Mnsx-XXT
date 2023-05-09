package top.mnsx.executor;

import io.netty.channel.Channel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import top.mnsx.config.JobProperties;
import top.mnsx.handler.IJobHandler;
import top.mnsx.message.AppToAdminRequest;
import top.mnsx.message.JobResponse;
import top.mnsx.utils.SequenceIdGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 任务执行器
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
public class JobExecutor {
    @Setter
    private JobProperties jobProperties;

    @Setter
    private Channel channel;

    @Autowired
    private ApplicationContext applicationContext;

    // 存放所有IJobHandler的Bean
    private ConcurrentHashMap<String, IJobHandler> jobHandlerRepository = new ConcurrentHashMap<>();

    /**
     * 向Map中添加JobHandler
     * @param name handler名称
     * @param jobHandler jobHandler
     * @return jobHandler
     */
    public IJobHandler registerJobHandler(String name, IJobHandler jobHandler) {
        IJobHandler result = jobHandlerRepository.put(name, jobHandler);
        log.info("[Mnsx-Job] 注解handler成功, name={}, handler={}", name, jobHandler);
        return result;
    }

    /**
     * 从Map中读取JobHandler
     * @param name handler名称
     * @return jobHandler
     */
    public IJobHandler loadJobHandler(String name) {
        return jobHandlerRepository.get(name);
    }

    public void init() {
        log.info("[Mnsx-Job] JobExecutor初始化中...");

        // 初始化所有JobHandler
        initJobHandler();

        // 将自己注册到调度中心
        new RegisterAppToAdminThread().start();
    }

    public void destroy() {
        log.info("[Mnsx-Job] JobExecutor销毁中...");
    }

    /**
     * 任务执行
     * @param name 任务名称
     * @param params 任务参数
     * @return jobInvokeResponse
     */
    public JobResponse jobInvoke(String name, String params) {
        IJobHandler jobHandler = jobHandlerRepository.get(name);

        if (Objects.isNull(jobHandler)) {
            return JobResponse.error("任务不存在!");
        }

        try {
            return jobHandler.execute(params);
        } catch (Exception e) {
            log.error("[Mnsx-Job] 任务={} 调用异常={}", name, e);
            return JobResponse.error("任务调度异常!");
        }
    }

    /**
     * 将所有IJobHandler类型的Bean初始
     */
    public void initJobHandler() {
        // 获取所有Bean实现了IJobHandler
        String[] beanNames = applicationContext.getBeanNamesForType(IJobHandler.class);
        if (beanNames == null || beanNames.length == 0) {
            return;
        }

        // 注册JobHandler到Map
        Arrays.stream(beanNames).forEach(beanName -> {
            registerJobHandler(beanName, (IJobHandler)applicationContext.getBean(beanName));
        });
    }

    private class RegisterAppToAdminThread extends Thread {

        private RegisterAppToAdminThread() {
            super("Mnsx-AppToAdmin");
        }

        @Override
        public void run() {
            //todo: 取消循环
            do {
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    log.info("[Mnsx-Job] 等待Netty客户端创建创建...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.info("[Mnsx-Job] Netty客户端调用产生异常, exception={}", e.getMessage());
                    throw new RuntimeException(e);
                }
            } while (Objects.isNull(channel));

            log.info("[Mnsx-Job] 开始将应用注册到客户端...");

            // 准备参数
            AppToAdminRequest appToAdminRequest = new AppToAdminRequest();
            appToAdminRequest.setAppName(jobProperties.getAppName());
            appToAdminRequest.setAppDesc(jobProperties.getAppDesc());
            appToAdminRequest.setAddress(jobProperties.getIp() + ":" + jobProperties.getPort());
            appToAdminRequest.setSequenceId(SequenceIdGenerator.nextId());

            try {
                // 发送请求
                channel.writeAndFlush(appToAdminRequest);
                log.info("[Mnsx-Job] 应用通过通道={}注册到服务端成功", channel);
            } catch (Throwable t) {
                log.error("[Mnsx-Job] 应用注册到服务端失败, exception={}", t.getMessage());
            }
        }
    }
}
