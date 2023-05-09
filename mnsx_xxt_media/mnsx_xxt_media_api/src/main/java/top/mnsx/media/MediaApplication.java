package top.mnsx.media;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.mnsx.annotation.EnableJobAutoConfiguration;

@SpringBootApplication
@EnableJobAutoConfiguration(adminIp = "127.0.0.1", adminPort = 7777, appName = "MediaApplication", appDesc = "媒资管理服务")
public class MediaApplication {
	public static void main(String[] args) {
		SpringApplication.run(MediaApplication.class, args);
	}
}