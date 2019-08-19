package cc.mrbird.febs.basicInfo.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 学校表 Entity
 *
 * @author MrBird
 * @date 2019-08-18 01:39:43
 */
@Data
@TableName("jcc_school_info")
public class School {

    /**
     * 学校ID
     */
    @TableId(value = "school_id", type = IdType.AUTO)
    private Long schoolId;

    /**
     * 学校名
     */
    @TableField("school_name")
    private String schoolName;

    /**
     * 介绍
     */
    @TableField("introduce")
    private String introduce;

    /**
     * 学校类型
     */
    @TableField("school_type")
    private String schoolType;

    /**
     * 联系人
     */
    @TableField("link_man")
    private String linkMan;

    /**
     * 联系电话
     */
    @TableField("link_phone")
    private String linkPhone;

    /**
     * 邮编
     */
    @TableField("post_code")
    private String postCode;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

    /**
     * 经度
     */
    @TableField("lng")
    private String lng;

    /**
     * 纬度
     */
    @TableField("lat")
    private String lat;

    /**
     * 省
     */
    @TableField("province")
    private String province;

    /**
     * 市
     */
    @TableField("city")
    private String city;

    /**
     * 县
     */
    @TableField("conunty")
    private String conunty;

    /**
     * 资质证明
     */
    @TableField("certificate_book")
    private String certificateBook;

    /**
     * 市领导
     */
    @TableField("city_leader_name")
    private String cityLeaderName;

    /**
     * 省领导
     */
    @TableField("province_leader_name")
    private String provinceLeaderName;

    /**
     * 市审核时间
     */
    @TableField("city_date")
    private Date cityDate;

    /**
     * 省审核时间
     */
    @TableField("province_date")
    private Date provinceDate;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

}