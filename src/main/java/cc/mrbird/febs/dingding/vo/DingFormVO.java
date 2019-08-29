package cc.mrbird.febs.dingding.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class DingFormVO implements Serializable{

    private List<Form_component_values> form_component_values;
    private Date create_time;
    private List<String> attached_process_instance_ids;
    private String originator_dept_name;
    private String originator_userid;
    private String title;
    private Date finish_time;
    private String result;
    private String originator_dept_id;
    private String business_id;
    private List<Tasks> tasks;
    private String biz_action;
    private List<Operation_records> operation_records;
    private String status;
}
