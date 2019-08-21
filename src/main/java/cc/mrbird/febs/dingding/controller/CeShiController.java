package cc.mrbird.febs.dingding.controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static cc.mrbird.febs.dingding.controller.CeShi.$params;

public class CeShiController {
    public Map ceshiMap(HttpServletRequest request){
        Map params = $params(request);
        return params;
    }
}
