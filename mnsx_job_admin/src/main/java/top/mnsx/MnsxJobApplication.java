package top.mnsx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@SpringBootApplication
@MapperScan("top.mnsx.mapper")
public class MnsxJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(MnsxJobApplication.class);
    }
}
