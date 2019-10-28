package cc.mrbird.febs.basicInfo.entity;

import java.io.Serializable;
import java.util.Date;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author psy
 * @date 2019-08-16 16:50:43
 */
@Data
@TableName("jcc_device_info")
@Excel("设备管理")
public class DeviceInfo implements Serializable {

    /**
     * 
     */
    @TableId(value = "device_id", type = IdType.AUTO)
    private Integer deviceId;

    /**
     * 学校名称
     */
    @ExcelField(value = "设备名称",maxLength = 255)
    @TableField("device_name")
    private String deviceName;

    /**
     * 
     */
    @TableField("school_id")
    private Integer schoolId;

    /**
     * 负责人
     */
    @ExcelField(value = "负责人",maxLength = 255)
    @TableField("username")
    private String username;

    /**
     * 购买时间
     */
    @ExcelField(value = "购买时间",maxLength = 255,dateFormat = "yyyy-MM-dd HH:mm:SS")
    @TableField("buyt_time")
    private Date buytTime;

    /**
     * 状态
     */
    @ExcelField(value = "设备类型",maxLength = 255)
    @TableField("device_type")
    private String deviceType;
    /**
     * 
     */
    @TableField("classroom_id")
    private Integer classroomId;

    /**
     *
     */
    @ExcelField(value = "厂商名称",maxLength = 255)
    @TableField( "firm_name")
    private String firmName;

    /**
     * 
     */
    @ExcelField(value = "状态(1表示启用,0表示禁用)",maxLength = 255)
    @TableField("state")
    private Integer state;

    @TableField("num")
    private Integer num;

    @TableField(exist = false)
    private String schoolName;

    @TableField(exist = false)
    private String classroomName;

    @TableField(exist = false)
    private String buytTimeFrom;

    @TableField(exist = false)
    private String buytTimeTo;


    public Integer getDeviceId() {
        return deviceId;
    }


}