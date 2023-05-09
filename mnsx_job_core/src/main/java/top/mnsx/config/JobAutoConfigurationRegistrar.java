package top.mnsx.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import top.mnsx.annotation.EnableJobAutoConfiguration;
import top.mnsx.executor.JobExecutor;
import top.mnsx.utils.NetUtil;

import java.util.Objects;

/**
 * 读取注解上的配置信息，并且手动注册JobExecutor和JobInvokeNettyRegistrar、JobInvokeServletRegistrar到Spring中
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
public class JobAutoConfigurationRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    @Setter
    // 配置环境
    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取注解参数
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(EnableJobAutoConfiguration.class.getName()));

        if (Objects.isNull(annotationAttributes)) {
            log.error("[Mnsx-Job] EnableJobAutoConfiguration注解属性为空");
            throw new RuntimeException("EnableJobAutoConfiguration注解属性为空");
        }

        // 准备配置信息
        JobProperties jobProperties = getJobProperties(annotationAttributes);

        // 注册JobExecutor
        registerJobExecutor(jobProperties, registry);

        // 注册JobInvokeServletRegistrar
        registerJobInvokeServletRegistrar(registry);

        // 注册JobInvokeServletRegistrar
        registerJobInvokeNettyRegistrar(jobProperties, registry);
    }

    /**
     * 将注解属性创建成JobProperties
     * @param annotationAttributes 注解属性类
     * @return JobProperties
     */
    private JobProperties getJobProperties(AnnotationAttributes annotationAttributes) {
        // 创建配置实例
        JobProperties jobProperties = new JobProperties();
        jobProperties.setAdminIp(annotationAttributes.getString("adminIp"));
        jobProperties.setAdminPort(annotationAttributes.getNumber("adminPort"));
        jobProperties.setAppName(annotationAttributes.getString("appName"));
        jobProperties.setAppDesc(annotationAttributes.getString("appDesc"));
        jobProperties.setIp(NetUtil.getIp());
        jobProperties.setPort(environment.getProperty("server.port", Integer.class, 8080));
        return jobProperties;
    }

    /**
     * 注册JobInvokeServletRegistrar
     * @param registry BeanDefinition注册中心
     */
    private void registerJobInvokeServletRegistrar(BeanDefinitionRegistry registry) {

        log.info("[Mnsx-Job] JobInvokeServletRegistrar开始注册...");

        // 创建JobInvokeServletRegistrar类的BeanDefinition
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(JobInvokeServletRegistrar.class)
                .setFactoryMethod("newInstance") // 指定工厂方法
                .addPropertyReference("jobExecutor", "jobExecutor") // 指定参数引用
                .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO) // 不能通过AutoWired注入
                .getBeanDefinition();

        // 注册BeanDefinition
        registry.registerBeanDefinition("JobInvokeServlet", beanDefinition);

        log.info("[Mnsx-Job] JobInvokeServletRegistrar注册成功");
    }

    /**
     * 注册JobExecutor
     * @param jobProperties 注解参数类
     * @param registry BeanDefinition注册中心
     */
    private void registerJobExecutor(JobProperties jobProperties, BeanDefinitionRegistry registry) {

        log.info("[Mnsx-Job] JobExecutor开始注册...");

        // 创建JobExecutor类BeanDefinition
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(JobExecutor.class)
                .setInitMethodName("init") // 初始化方法
                .setDestroyMethodName("destroy") // 销毁方法
                .addPropertyValue("jobProperties", jobProperties) // 类中的参数的值
                .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE) // 通过Type进行AutoWired注入
                .getBeanDefinition();

        // 注册bean到spring容器
        registry.registerBeanDefinition("jobExecutor", beanDefinition);

        log.info("[Mnsx-Job] JobExecutor注册成功");
    }

    /**
     * 注册JobInvokeNettyRegistrar
     * @param jobProperties 注解参数类
     * @param registry BeanDefinition注册中心
     */
    private void registerJobInvokeNettyRegistrar(JobProperties jobProperties, BeanDefinitionRegistry registry) {

        log.info("[Mnsx-Job] JobInvokeNettyRegistrar开始注册...");

        // 创建JobInvokeNettyRegistrar类的BeanDefinition
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(JobInvokeNettyRegistrar.class)
                .setInitMethodName("init") // 初始化方法
                .addPropertyValue("jobProperties", jobProperties) // 参数值
                .addPropertyReference("jobExecutor", "jobExecutor") // 参数引用
                .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO) // 不允许直接注入
                .getBeanDefinition();

        // 注册bean到spring容器
        registry.registerBeanDefinition("JobInvokeNetty", beanDefinition);

        log.info("[Mnsx-Job] JobInvokeNettyRegistrar注册成功");
    }
}