package cc.mrbird.febs.basicInfo.area.entity;


import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author psy
 * @date 2019-08-15 21:34:16
 */
@Data
@TableName("jcc_area")
public class Area {

    /**
     * 
     */
    @TableId(value = "area_code", type = IdType.AUTO)
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