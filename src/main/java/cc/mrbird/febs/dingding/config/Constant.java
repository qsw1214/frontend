package cc.mrbird.febs.dingding.config;

/**
 * 项目中的常量定义类
 */
public class Constant {
    /**
     * 企业corpid, 需要修改成开发者所在企业
     */
    public static final String CORP_ID = "ding827934143c10773835c2f4657eb6378f";
    /**
     * 应用的AppKey，登录开发者后台，点击应用管理，进入应用详情可见
     */
    public static final String APPKEY = "dingrw665wtcg7e4brqi";
    /**
     * 应用的AppSecret，登录开发者后台，点击应用管理，进入应用详情可见
     */
    public static final String APPSECRET = "kitLl5P_cxpgt04Ztm-6QpdraF9Tkie1GJ2ivwELkWjxRJVie4XVVWWyy1TNuC15";

    /**
     * 登录的ACCESSKEY
     */
    public static final String ACCESSKEY = "dingoakvxpd9icyeewvolr";
    /**
     * 登录的ACCESSSECRET
     */
    public static final String ACCESSSECRET = "wzMnAn6ZVc8gcabZT3rXEsGcAMRGO6DixPoIxw4Vq4K--d9aXtMs19iMmF4O0ZRX";
    /**
     * 数据加密密钥。用于回调数据的加密，长度固定为43个字符，从a-z, A-Z, 0-9共62个字符中选取,您可以随机生成
     */
    public static final String ENCODING_AES_KEY = "1234567890123456789012345678901234567890123";

    /**
     * 加解密需要用到的token，企业可以随机填写。如 "12345"
     */
    public static final String TOKEN = "12345";

    /**
     * 应用的agentdId，登录开发者后台可查看
     */
    public static final Long AGENTID = (long)283129946;

    /**
     * 审批模板唯一标识，可以在审批管理后台找到
     */
    public static final String PROCESS_CODE = "PROC-6405B98C-CF31-49D6-98F7-3DFF0457B759";

    /**
     * 回调host
     */
    public static final String CALLBACK_URL_HOST = "http://157fcefb.cpolar.io";
}
