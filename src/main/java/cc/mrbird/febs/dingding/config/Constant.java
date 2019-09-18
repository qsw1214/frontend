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
    public static final String PROCESS_CODE = "PROC-997ABD5D-D0D9-487E-9696-5B5DF49EFCC0";

    /**
     * 回调host
     */
    public static final String CALLBACK_URL_HOST = "http://72524b49.cpolar.io/approval";

    /**
     * 学校入驻审批标识
     */
    public static final String SCHOOL_CALLBACK_URL_HOST = "PROC-B2CA87F4-BB2B-4EF8-9672-CCF839FD344C";

    /**
     * 第三方应用申请接入审批标识
     */
    public static final String APP_ABUTMENT_CALLBACK_URL_HOST = "PROC-B23A2A20-14A0-4549-8C9F-CD5C583DC0DD";

    /**
     * 审批结果
     */
    public static class APPROVE_RESULT{
        public static final String AGREE_RESULT = "AGREE";
        public static final String DEFEND_RESULT = "DEFEND";
    }

    /**
     * 通讯录回调host
     */
    public static final String CALLBACK_URL_ORG = "http://4e62ef2b.cpolar.io/org";
}
