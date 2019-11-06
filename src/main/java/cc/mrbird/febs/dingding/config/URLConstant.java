package cc.mrbird.febs.dingding.config;

public class URLConstant {
    /**
     * 钉钉网关gettoken地址
     */
    public static final String URL_GET_TOKKEN = "https://oapi.dingtalk.com/gettoken";

    /**
     *获取用户在企业内userId的接口URL
     */
    public static final String URL_GET_USER_INFO = "https://oapi.dingtalk.com/user/getuserinfo";

    /**
     * 通过code获取用户信息接口
     */
    public static final String URL_GET_USER_INFO_BYCODE = "https://oapi.dingtalk.com/sns/getuserinfo_bycode";
    /**
     * 通过access_token和unionId获取userId的接口
     */
    public static final String URL_GET_USERID_BY_UNIONID = "https://oapi.dingtalk.com/user/getUseridByUnionid";
    /**
     * 通过
     */
    /**
     *获取用户姓名的接口url
     */
    public static final String URL_USER_GET = "https://oapi.dingtalk.com/user/get";

    /**
     * 发起审批实例的接口url
     */
    public static final String URL_PROCESSINSTANCE_START = "https://oapi.dingtalk.com/topapi/processinstance/create";

    /**
     * 获取审批实例的接口url
     */
    public static final String URL_PROCESSINSTANCE_GET = "https://oapi.dingtalk.com/topapi/processinstance/get";

    /**
     * 发送企业通知消息的接口url
     */
    public static final String MESSAGE_ASYNCSEND = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";

    /**
     * 删除企业回调接口url
     */
    public static final String DELETE_CALLBACK = "https://oapi.dingtalk.com/call_back/delete_call_back";

    /**
     * 注册企业回调接口url
     */
    public static final String REGISTER_CALLBACK = "https://oapi.dingtalk.com/call_back/register_call_back";
    /**
     * 创建ding日程的接口url
     */
    public static final String URL_CALENDAR_CREATE = "https://oapi.dingtalk.com/topapi/calendar/create";

    /**
     * 创建ding群组的接口url
     */
    public static final String URL_CHAT_CREATE = "https://oapi.dingtalk.com/chat/create";

}
