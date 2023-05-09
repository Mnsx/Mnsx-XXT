package top.mnsx.course.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CoursePublishConsumer {
    @RabbitListener(queues = "course.publish")
    public void execute(String courseId) {

        // 向es添加索引数据

        // 向redis写入缓存

    }
}
