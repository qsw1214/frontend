package cc.mrbird.febs.dingding.vo;

import lombok.Data;

import java.util.Date;

@Data
public class Tasks {
    private String task_status;
    private Date create_time;
    private String task_result;
    private String userid;
    private Date finish_time;
    private String taskid;
    private String url;
}
