package cc.mrbird.febs.basicInfo.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *  Entity
 *
 * @author psy
 * @date 2019-08-16 08:37:08
 */
@Data
public class OnlineClass {

    private Integer schoolId;

    private String deviceName;

    private String schoolName;

    private String grade;

    private String playUrl;

    private String subject;

    private String firmName;

    private String classroomName;

}