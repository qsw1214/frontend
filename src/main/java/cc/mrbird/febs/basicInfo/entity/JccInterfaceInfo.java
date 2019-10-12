package cc.mrbird.febs.basicInfo.entity;


import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author lb
 * @date 2019-10-11 09:27:33
 */
@Data
@TableName("jcc_interface_info")
public class JccInterfaceInfo {

    /**
     * 接口id
     */
    @TableId(value = "interface_id", type = IdType.AUTO)
    private Integer interfaceId;

    /**
     * 上级接口id
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 接口名称
     */
    @TableField("interface_name")
    private String interfaceName;

    /**
     * 请求方式,GET(http),POST(http)
     */
    @TableField("request_ways")
    private String requestWays;

    /**
     * 请求地址
     */
    @TableField("request_address")
    private String requestAddress;

    /**
     * 请求头
     */
    @TableField("request_head")
    private String requestHead;

    /**
     * 请求包结构体
     */
    @TableField("request_body")
    private String requestBody;

    /**
     * 请求参数说明
     */
    @TableField("request_parameter_explain")
    private String requestParameterExplain;

    /**
     * 返回结果
     */
    @TableField("request_result")
    private String requestResult;

    /**
     * 返回结果说明
     */
    @TableField("request_result_explain")
    private String requestResultExplain;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 排序
     */
    @TableField("order_num")
    private Long orderNum;

    /**
     * 显示状态：0->不显示；1->显示
     */
    @TableField("show_status")
    private Integer showStatus;

}