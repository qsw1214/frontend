package cc.mrbird.febs.basicInfo.entity;

import lombok.Data;

import java.util.Map;

@Data
public class ImgResult {
    private Integer code;
    private Map<String,String> data;
    private String msg;
}
