package cc.mrbird.febs.basicInfo.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author wsq
 * @date 2019-10-23 15:34:24
 */
@Data
@TableName("timetable")
public class Timetable {

    /**
     * 课程id
     */
    @TableId(value = "TID", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tid;

    /**
     * 课程名称
     */
    @TableField("TIMETABLE_NAME")
    private String timetableName;

    /**
     * 课程时长
     */
    @TableField("TIMETABLE_TIMES")
    private String timetableTimes;

    /**
     * 主讲人
     */
    @TableField("TIMETABLE_SPEAKER")
    private String timetableSpeaker;

    /**
     * 日期
     */
    @TableField("TIMETABLE_DATE")
    private String timetableDate;

    /**
     * 日期
     */
    @TableField("TIMETABLE_WEEK")
    private String timetableWeek;
}