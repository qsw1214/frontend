package cc.mrbird.febs.basicInfo.entity;


import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author psy
 * @date 2019-08-16 08:37:08
 */
@Data
@TableName("jcc_area")
public class Area {

    /**
     * 
     */
    @TableId(value = "area_code")
    private String areaCode;

    /**
     * 
     */
    @TableField("province")
    private String province;

    /**
     * 
     */
    @TableField("city")
    private String city;

    /**
     * 
     */
    @TableField("country")
    private String country;

}