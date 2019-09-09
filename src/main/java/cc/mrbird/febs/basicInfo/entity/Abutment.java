package cc.mrbird.febs.basicInfo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@TableName("app_abutment_apply")
public class Abutment {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * '发起人id
     */
    @TableId(value = "originator_userid")
    private Long originatorUserid;
    /**
     * '审批实例标题',
     */
    /**
     * '申请学校'
     * @return
     */
    @TableId(value = "apply_school")
    private String applySchool;

    /**
     * 联系电话
     * @return
     */
    @TableId(value = "phone")
    private String phone;
    /**
     * 标题
     */
    @TableId(value = "title")
    private String title;
    /**
     * //'审批结果,1代表agree，0代表refuse',
     */
    @TableId(value = "result")
    private Integer result;
    /**
     * / /'应用名称',
     */
    @TableId(value = "app_name")
    private String appName;
    /**
     * '应用logo',
     */
    @TableId(value = "app_logo")
    private String appLogo;
    /**
     * //'应用简介',
     */
    @TableId(value = "app_inf")
    private String appInf;
    /**
     * //'应用首页链接',
     */
    @TableId(value = "app_main_link")
    private String appMainLink;
    /**
     * //'服务器出口ip',
     */
    @TableId(value = "export_ip")
    private String exportIp;
    /**
     * //'PC端首页地址',
     */
    @TableId(value = "pc_main_address")
    private String pcMainAddress;
    /**
     * //'管理后台地址',
     */
    @TableId(value = "manage_address")
    private String manageAddress;
    /**
     * //'审批创建时间',
     */
    @TableId(value = "create_time")
    private Date createTime;
    /**
     * //'审批结束时间',
     */
    @TableId(value = "finish_time")
    private Date finishTime;
    /**
     * //'市级领导id',
     */
    @TableId(value = "city_leader_id")
    private Long cityLeaderId;
    /**
     * //'市级领导审核时间',
     */
    @TableId(value = "city_approval_time")
    private Date cityApprovalTime;
    /**
     * //'省级领导id',
     */
    @TableId(value = "province_leader_id")
    private Long provinceLeaderId;
    /**
     * //'省级领导审核时间',
     */
    @TableId(value = "province_approval_time")
    private Date provinceApprovalTime;
    /**
     * //'应用的agentdId',
     */
    @TableId(value = "agent_id")
    private Long agentId;
    /**
     * //'应用的AppKey',
     */
    @TableId(value = "app_key")
    private String appKey;
    /**
     * //'应用的Secret',
     */
    @TableId(value = "app_secret")
    private String appSecret;
    /**
     * //'是否已创建应用,1表示已创建,0表示未创建',
     */
    @TableId(value = "is_create")
    private Integer isCreate;
    /**
     * //'创建应用人的id'
     */
    @TableId(value = "create_app_userid")
    private Long createAppUserid;
}
