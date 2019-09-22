package cc.mrbird.febs;

import cc.mrbird.febs.common.interceptor.TokenInterceptor;
import com.taobao.api.ApiException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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
@Configuration
public class FebsApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) throws ApiException {
        new SpringApplicationBuilder(FebsApplication.class)
                .run(args);
    }

    /**
     * 配置拦截器
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**/**");
    }

}
