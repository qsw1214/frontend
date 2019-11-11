package cc.mrbird.febs.basicInfo.entity;

import java.sql.Time;
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
 * @date 2019-08-21 10:38:49
 */
@Data
@TableName("jcc_school_timetable")
@Excel("课程管理")
public class SchoolTimetable {

    /**
     *
     */
    @TableId(value = "course_id", type = IdType.AUTO)
    private Integer courseId;
    /**
     * 开课时间
     */
    @ExcelField(value = "开课时间",maxLength = 255)
    @TableField("begin_date")
    private Time beginDate;

    /**
     * 
     */
    @TableField("class_id")
    private String classId;

    @ExcelField(value = "班级名称",maxLength = 255)
    @TableField(exist = false)
    private String className;

    /**
     * 教室编号
     */
    @TableField("classroom_id")
    private Integer classroomId;

    @ExcelField(value = "教室位置",maxLength = 255)
    @TableField(exist = false)
    private String location;

    @TableField(exist = false)
    private String url;

    @TableField(exist = false)
    private String state;
    /**
     * 
     */
    @ExcelField(value = "课程名称",maxLength = 255)
    @TableField("course_name")
    private String courseName;

    /**
     * 
     */
    @ExcelField(value = "上课时长",maxLength = 255)
    @TableField("duration")
    private Integer duration;

    /**
     * 
     */
    @ExcelField(value = "年级名称",maxLength = 255)
    @TableField("grade")
    private String grade;
    /**
     * 
     */
    @TableField("school_id")
    private Integer schoolId;

    @TableField(exist = false)
    private String schoolName;

    /**
     * 节次
     */
    @ExcelField(value = "节次",maxLength = 255)
    @TableField("section")
    private String section;

    /**
     * 学科
     */
    @ExcelField(value = "教学科目",maxLength = 255)
    @TableField("subject")
    private String subject;

    /**
     * 学期
     */
    @ExcelField(value = "学期",maxLength = 255)
    @TableField("term")
    private String term;

    /**
     * 
     */
    @TableField("user_id")
    private Long userId;


    @ExcelField(value = "授课教师",maxLength = 255)
    @TableField("user_name")
    private String username;
    /**
     * 星期
     */
    @ExcelField(value = "授课教师",maxLength = 255)
    @TableField("week")
    private String week;

    /**
     * 学校ids
     */
    @TableField(exist = false)
    @ExcelField(value = "学校id(请复制)",maxLength = 255)
    private String schoolIds;

    /**
     * 学校名称
     */
    @TableField(exist = false)
    private String schoolNames;

    /**
     * 班级ids
     */
    /**
     * 学校ids
     */
    @TableField(exist = false)
    @ExcelField(value = "班级id(请复制)",maxLength = 255)
    private String classIds ;
}