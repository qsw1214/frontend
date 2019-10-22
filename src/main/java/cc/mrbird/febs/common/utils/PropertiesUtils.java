//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cc.mrbird.febs.common.utils;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {
    private static Properties props;
    static {
        String fileName = "spy.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class
                    .getClassLoader().getResourceAsStream(fileName), "UTF-8"));
        } catch (IOException e) {
            System.out.println("配置文件读取异常");
        }
    }

    /***
     *
     * @param key
     *            键值
     * @return 返回获取结果
     */
    public static String getProperty(String key) {
        String value = props.getProperty(key.trim());
        // 判断value是否为空，对于isBlank而言""， " "， "      "， null 都返回为空
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    /**
     *
     * @param key
     *            键值
     * @param defaultValue
     *            如果未找到相应的value值，则以defaultValue代替
     * @return 返回获取结果
     */
    public static String getProperty(String key, String defaultValue) {

        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value.trim();
    }

    public static void main(String[] args){
        System.out.println(PropertiesUtils.getProperty("filter"));
    }
}
