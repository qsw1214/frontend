package cc.mrbird.febs.dingding.controller;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class CeShi {
    public static Map<String,Object> $params(HttpServletRequest request){
        return $params(false,request);
    }
    public static Map<String,Object> $params(boolean putUserInfo, HttpServletRequest request){
        Map<String,Object> params = WebUtils.getParametersStartingWith(request,"");
        return params;
    }
}
