package cc.mrbird.febs.common.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {
	public static String uploadBasePath;
	public static String ip2regionPath;
	
	@Value("${myConfig.uploadBasePath}")
	public void setUploadBasePath(String uploadBasePath){
		GlobalConfig.uploadBasePath = uploadBasePath;
	}
}
