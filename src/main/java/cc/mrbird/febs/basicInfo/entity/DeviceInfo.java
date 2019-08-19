package cc.mrbird.febs.basicInfo.entity;

import java.util.Date;

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
public class DeviceInfo {

    /**
     * 
     */
    @TableId(value = "device_id", type = IdType.AUTO)
    private Integer deviceId;

    /**
     * 
     */
    @TableField("device_name")
    private String deviceName;

    /**
     * 
     */
    @TableField("school_id")
    private Integer schoolId;

    /**
     * 
     */
    @TableField("username")
    private String username;

    /**
     * 
     */
    @TableField("buyt_time")
    private Date buytTime;

    /**
     * 
     */
    @TableField("device_type")
    private String deviceType;
    /**
     * 
     */
    @TableField("classroom_id")
    private Integer classroomId;

    @TableField( "厂商名称")
    private String firmName;

    /**
     * 
     */
    @TableField("state")
    private Integer state;

    @TableField("num")
    private Integer num;

    @ExcelField(value = "学校名称")
    @TableField(exist = false)
    private String schoolName;

    @ExcelField(value = "教室名称")
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