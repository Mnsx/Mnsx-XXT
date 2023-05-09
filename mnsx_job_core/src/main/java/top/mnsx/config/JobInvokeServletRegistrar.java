package top.mnsx.config;

import com.alibaba.fastjson.JSON;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import top.mnsx.executor.JobExecutor;
import top.mnsx.message.JobInvokeRequest;
import top.mnsx.message.JobResponse;
import top.mnsx.utils.HttpPair;
import top.mnsx.utils.ThrowableUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

/**
 * 任务执行Servlet注册中心
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
public class JobInvokeServletRegistrar<JobInvokeServlet> extends ServletRegistrationBean {

    @Setter
    private JobExecutor jobExecutor;

    public JobInvokeServletRegistrar() {
        super();
    }

    // 初始化工厂方法
    public static JobInvokeServletRegistrar newInstance() {

        JobInvokeServletRegistrar jobInvokeServletRegistrar = new JobInvokeServletRegistrar();
        // 设置Servlet
        jobInvokeServletRegistrar.setServlet(jobInvokeServletRegistrar.new JobInvokeServlet());
        // 设置映射
        jobInvokeServletRegistrar.setUrlMappings(Collections.singletonList("/mnsx/job/invoke"));
        // 设置启动时间
        jobInvokeServletRegistrar.setLoadOnStartup(1);

        return jobInvokeServletRegistrar;
    }

    private class JobInvokeServlet extends HttpServlet {

        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            // 设置json响应头
            resp.setHeader("Content-Type", "application/json");

            try {
                HttpPair reqAndRsp = run(req, resp);

                log.info("[Mnsx-Job] 执行作业：req={}，rsp={}", reqAndRsp.getRequest(), reqAndRsp.getResponse());
            } catch (Throwable t) {
                String msg = ThrowableUtil.getThrowableStackTrace(t);
                log.warn("[Mnsx-Job] 任务调用异常：{}", msg);
                String rspStr = JSON.toJSONString(JobResponse.error(msg));
                resp.getOutputStream().write(rspStr.getBytes(Charset.defaultCharset()));
            }
        }

        /**
         * 执行JobInvoke请求
         * @param req Http请求
         * @param resp Http响应
         * @return HttpPair
         * @throws Throwable Throwable
         */
        private HttpPair run(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
            // 反序列化
            ServletInputStream inputStream = req.getInputStream();
            byte[] body = new byte[req.getContentLength()];
            inputStream.read(body);
            JobInvokeRequest jobInvokeReq = SerializerAlgorithmConfig.getSerializerAlgorithm().deserialize(JobInvokeRequest.class, body);

            // 调用任务
            JobResponse jobInvokeRsp = JobInvokeServletRegistrar.this.jobExecutor.jobInvoke(jobInvokeReq.getName(), jobInvokeReq.getParams());

            // 响应结果
            String response = JSON.toJSONString(jobInvokeRsp);
            resp.getOutputStream().write(response.getBytes(Charset.defaultCharset()));

            // 返回请求和响应提供日志记录
            return new HttpPair(req, resp);
        }
    }
}