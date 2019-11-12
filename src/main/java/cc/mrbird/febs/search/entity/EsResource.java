package cc.mrbird.febs.search.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.Data;
/**
 * 搜索中的资源信息
 * Created by lb on 2019/8/31.
 */
@Data
@Document(indexName = "rms", type = "resource",shards = 1,replicas = 0)
public class EsResource {
	/**
     * 资源ID
     */
	@Id
    private Long resourceId;

    /**
     * 资源名称
     */
	@Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String resourceName;
	
	/**
     * 关键字
     */
	@Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String keywords;

    /**
     * 创建人
     */
	@Field(type = FieldType.Keyword)
    private String creator;
    
    /**
     * 创建人头像
     */
	@Field(index = false, type = FieldType.Keyword)
    private String avatar;

    /**
     * 部门
     */
	@Field(type = FieldType.Integer)
    private String deptId;

    /**
     * 年级
     */
	@Field(type = FieldType.Integer)
    private Integer gradeId;

    /**
     * 科目
     */
	@Field(type = FieldType.Integer)
    private Integer subjectId;

    /**
     * 类别ID
     */
	@Field(type = FieldType.Long)
    private Long categoryId;
    
    /**
     * 文件类型
     */
	@Field(type = FieldType.Keyword)
    private String fileType;

    /**
     * 资源图片
     */
	@Field(index = false, type = FieldType.Keyword)
    private String pic;

    /**
     * 资源地址
     */
	@Field(index = false, type = FieldType.Keyword)
    private String url;
    
    /**
     * 评分：0->5
     */
	@Field(type = FieldType.Integer)
    private Integer star;

    /**
     * 资源介绍
     */
	@Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String description;

    /**
     * 阅读数
     */
	@Field(type = FieldType.Integer)
    private Long readCount;

    /**
     * 评论数
     */
	@Field(type = FieldType.Integer)
    private Integer commentCount;

    /**
     * 排序
     */
	@Field(type = FieldType.Long)
    private Long orderNum;

    /**
     * 审核状态：0->未审核；1->审核通过；2->审核不通过
     */
	@Field(type = FieldType.Integer)
    private Integer status;

    /**
     * 创建时间
     */
	@Field(type = FieldType.Date)
    private Date createTime;

    /**
     * 修改时间
     */
	@Field(type = FieldType.Date)
    private Date modifyTime;
    
    /**
     * 类别名称
     */
	@Field(type = FieldType.Keyword)
    private String categoryName;
    
    /**
     * 部门名称
     */
	@Field(type = FieldType.Keyword)
    private String deptName;
    
    /**
     * 年级名称
     */
	@Field(type = FieldType.Keyword)
    private String gradeName;
    
    /**
     * 科目名称
     */
	@Field(type = FieldType.Keyword)
    private String subjectName;

}
