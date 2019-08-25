package cc.mrbird.febs;

import com.taobao.api.ApiException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author MrBird
 */
@EnableScheduling
@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@MapperScan("cc.mrbird.febs.*.mapper")
public class FebsApplication {

    public static void main(String[] args) throws ApiException {
        new SpringApplicationBuilder(FebsApplication.class)
                .run(args);
    }

}
