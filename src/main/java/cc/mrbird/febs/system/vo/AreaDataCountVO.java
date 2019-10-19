package cc.mrbird.febs.system.vo;

import lombok.Data;

import java.util.List;

/**
 * 区域开课、资源、学生统计VO对象
 */
@Data
public class AreaDataCountVO {
    private List<String> cityNameList;
    private List<Integer>  netClassCountList;
    private List<Integer> resourceCountList;
    private List<Integer> studentCountList;
}
