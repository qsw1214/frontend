package cc.mrbird.febs.resource.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author MrBird
 */
@Data
@TableName("r_category")
@Excel("类别信息表")
public class Category implements Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final Long TOP_NODE = 0L;

    /**
     * 类别ID
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

    /**
     * 上级类别ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 类别名称
     */
    @TableField("category_name")
    @NotBlank(message = "{required}")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "名称")
    private String categoryName;

    /**
     * 图标
     */
    @TableField("icon")
    @Size(max = 255, message = "{noMoreThan}")
    @ExcelField(value = "图标")
    private String icon;

    /**
     * 排序
     */
    @TableField("order_num")
    private Long orderNum;

    /**
     * 是否显示
     */
    @TableField("show_status")
    private Long showStatus;

}

