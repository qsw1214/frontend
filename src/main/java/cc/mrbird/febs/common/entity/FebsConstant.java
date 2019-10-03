package cc.mrbird.febs.common.entity;

/**
 * 常量
 *
 * @author MrBird
 */
public class FebsConstant {

    // 排序规则：降序
    public static final String ORDER_DESC = "desc";
    // 排序规则：升序
    public static final String ORDER_ASC = "asc";

    // 前端页面路径前缀
    public static final String VIEW_PREFIX = "febs/views/";

    // 验证码 Session Key
    public static final String CODE_PREFIX = "febs_captcha_";

    // 允许下载的文件类型，根据需求自己添加（小写）
    public static final String[] VALID_FILE_TYPE = {"xlsx", "zip"};
    
    // 缓存前缀
    public static final String USER_PREFIX = "user.";
    
    public static final String RES_READ_PREFIX = "res.read.";
    
    // 缓存 hashset key
    public static final String RES_DETAIL_API_VISIT = "res.detail.api.visit";
    
    public static final String RES_VISIT = "res.visit";
}
