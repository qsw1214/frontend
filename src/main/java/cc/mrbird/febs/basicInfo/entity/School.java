package cc.mrbird.febs.basicInfo.entity;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.wuwenze.poi.annotation.ExcelField;
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
    private Integer schoolId;

    /**
     * 学校名
     */
    @TableField("school_name")
    @NotBlank(message = "{required}")
    private String schoolName;
    
    /**
     * 部门id
     */
    @TableField("dept_id")
    private Long deptId;

    /**
     * 介绍
     */
    @TableField("introduce")
    private String introduce;

    /**
     * 学校类型
     */
    @TableField("school_type")
    @NotBlank(message = "{required}")
    private String schoolType;

    /**
     * 学校类别
     */
    @TableField("school_category")
    private String schoolCategory;
    
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
     * 状态
     */
    @TableField("state")
    private Integer state;
    
    /**
     * 开课次数
     */
    @TableField("class_num")
    private Integer classNum;
    
    /**
     * 省
     */
    @TableField("province_dept_id")
    //@NotBlank(message = "{required}")
    private Long provinceDeptId;

    /**
     * 市
     */
    @TableField("city_dept_id")
    //@NotBlank(message = "{required}")
    private Long cityDeptId;

    /**
     * 县
     */
    @TableField("country_dept_id")
    //@NotBlank(message = "{required}")
    private Long countryDeptId;

    /**
     * 学校图片
     */
    @TableField("picture")
    private String picture;

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

    /**
     * 所属学校
     */
    @TableField( "belongId")
    private Integer belongId;

    @ExcelField(value = "教师总数")
    @TableField(exist = false)
    private Integer teacherCount;

    @ExcelField(value = "学生总数")
    @TableField(exist = false)
    private Integer studentCount;

    @ExcelField(value = "教室总数")
    @TableField(exist = false)
    private Integer classroomCount;

    @ExcelField(value = "所属主校名称")
    @TableField(exist = false)
    private String belongSchool;

    @ExcelField(value = "省级")
    @TableField(exist = false)
    private String provinceName;

    @ExcelField(value = "市级")
    @TableField(exist = false)
    private String cityName;

    @ExcelField(value = "区县级")
    @TableField(exist = false)
    private String countryName;
}