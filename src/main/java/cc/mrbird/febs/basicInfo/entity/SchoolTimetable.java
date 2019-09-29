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
 * @date 2019-08-21 10:38:49
 */
@Data
@TableName("jcc_school_timetable")
public class SchoolTimetable {

    /**
     *
     */
    @TableId(value = "course_id", type = IdType.AUTO)
    private Integer courseId;
    /**
     * 
     */
    @TableField("begin_date")
    private Date beginDate;

    /**
     * 
     */
    @TableField("class_id")
    private String classId;

    @ExcelField(value = "班级名称")
    @TableField(exist = false)
    private String className;

    /**
     * 教室编号
     */
    @TableField("classroom_id")
    private Integer classroomId;

    @ExcelField(value = "教室位置")
    @TableField(exist = false)
    private String location;

    @ExcelField(value = "巡课路径")
    @TableField(exist = false)
    private String url;

    @ExcelField(value = "巡课状态")
    @TableField(exist = false)
    private String state;
    /**
     * 
     */
    @TableField("course_name")
    private String courseName;

    /**
     * 
     */
    @TableField("duration")
    private Integer duration;

    /**
     * 
     */
    @ExcelField(value = "年级名称")
    @TableField("grade")
    private String grade;
    /**
     * 
     */
    @TableField("school_id")
    private Integer schoolId;

    @ExcelField(value = "学校名称")
    @TableField(exist = false)
    private String schoolName;

    /**
     * 节次
     */
    @TableField("section")
    private String section;

    /**
     * 学科
     */
    @TableField("subject")
    private String subject;

    /**
     * 学期
     */
    @TableField("term")
    private String term;

    /**
     * 
     */
    @TableField("user_id")
    private Long userId;


    @ExcelField(value = "授课教师")
    @TableField("user_name")
    private String username;
    /**
     * 
     */
    @TableField("week")
    private String week;

}