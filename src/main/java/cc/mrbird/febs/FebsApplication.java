package cc.mrbird.febs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author MrBird
 */
@EnableScheduling

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@MapperScan("cc.mrbird.febs.*.mapper")
@Configuration
public class FebsApplication {

    public static void main(String[] args){
        new SpringApplicationBuilder(FebsApplication.class)
                .run(args);
    }

}
