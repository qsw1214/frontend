package cc.mrbird.febs.dingding.config;

/**
 * 项目中的常量定义类
 */
public class Constant {
    /**
     * 企业corpid, 需要修改成开发者所在企业
     */
    public static final String CORP_ID = "dingc73214fc1bf5420135c2f4657eb6378f";
    //public static final String CORP_ID = "ding827934143c10773835c2f4657eb6378f";
    /**
     * 应用的AppKey，登录开发者后台，点击应用管理，进入应用详情可见
     */
    //public static final String APPKEY = "dingrw665wtcg7e4brqi";
    public static final String APPKEY = "ding60ni18cfggf5ppss";
    /**
     * 应用的AppSecret，登录开发者后台，点击应用管理，进入应用详情可见
     */
    //public static final String APPSECRET = "kitLl5P_cxpgt04Ztm-6QpdraF9Tkie1GJ2ivwELkWjxRJVie4XVVWWyy1TNuC15";
    public static final String APPSECRET = "7XfMyuRTtkHV6hxC-Ze74pmfhwNoHtg-6lVs0WdcNiH5tA98_ae31W8_y4i4Zca6";

    public static final String ACCESS_TOKEN = "5e30e557a73d3014976b754a27f84ef0";
    /**
     * 登录的ACCESSKEY
     */
    public static final String ACCESSKEY = "dingoaqdflcmvtnqribq4w";
    //public static final String ACCESSKEY = "dingoaqdflcmvtnqribq4w";
    /**
     * 登录的ACCESSSECRET
     */
    public static final String ACCESSSECRET = "eLBsym3BoQyxR7HFJA4J9mJ9P2DrRbi03qPligElmRWhfzdESfaHxkIsyE-yJPSw";
    //public static final String ACCESSSECRET = "wzMnAn6ZVc8gcabZT3rXEsGcAMRGO6DixPoIxw4Vq4K--d9aXtMs19iMmF4O0ZRX";
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
    //public static final Long AGENTID = (long)301835083;
    public static final Long AGENTID = (long)316438650;
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
    public static final String APP_ABUTMENT_CALLBACK_URL_HOST = "PROC-26F70284-0844-4AD8-83A7-9E14FD8EBD58";

    /**
     * sso
     */
    public static final String SSO_Secret = "";

    /**
     * 审批结果
     */
    public static class APPROVE_RESULT{
        public static final String AGREE_RESULT = "AGREE";
        public static final String DEFEND_RESULT = "DEFEND";
    }

    /**
    /**
     * 通讯录回调host
     */
    public static final String CALLBACK_URL_ORG = "http://47.97.74.226:8080/org";


    public static String CITY_ALL_SELECT_DEPT_ID = "111";
    public static String COUNTRY_ALL_SELECT_DEPT_ID = "222";
}
