package cc.mrbird.febs.search.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 搜索热词统计表 Entity
 *
 * @author lb
 * @date 2019-09-22 23:15:01
 */
@Data
@TableName("r_keyword_count")
public class KeywordCount {

    /**
     * id
     */
    @TableId(value = "keyword_count_id", type = IdType.AUTO)
    private Long keywordCountId;

    /**
     * 热词
     */
    @TableField("keyword")
    private String keyword;

    /**
     * 数量
     */
    @TableField("count")
    private Integer count;

    /**
     * 搜索日期
     */
    @TableField("search_date")
    private Date searchDate;

}