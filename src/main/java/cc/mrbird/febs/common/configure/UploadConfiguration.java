package cc.mrbird.febs.common.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadConfiguration implements WebMvcConfigurer{
	@Value("${myConfig.uploadBasePath}")
	private String uploadBasePath;
	public void addResourceHandlers(ResourceHandlerRegistry registry){
//		registry.addResourceHandler( "/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler( "/files/**").addResourceLocations("file:"+this.uploadBasePath);
	}
	
}
