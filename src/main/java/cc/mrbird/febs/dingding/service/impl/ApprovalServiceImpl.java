package cc.mrbird.febs.dingding.service.impl;

import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.service.ISchoolService;
import cc.mrbird.febs.dingding.service.AppAbutmentService;
import cc.mrbird.febs.dingding.service.IApprovalService;
import cc.mrbird.febs.dingding.vo.DingFormVO;
import cc.mrbird.febs.dingding.vo.Form_component_values;
import cc.mrbird.febs.dingding.vo.Tasks;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IUserService;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApprovalServiceImpl implements IApprovalService{

    @Autowired
    private IUserService userService;

    @Autowired
    private ISchoolService schoolService;

    @Autowired
    private AppAbutmentService appAbutmentService;




    public void dealSchoolApprovalData(String processInstance){
         Gson gson = new Gson();
        DingFormVO dingFormVO = gson.fromJson(processInstance,DingFormVO.class);
        List<Tasks> taskLists = dingFormVO.getTasks();
        List<Form_component_values> formValuesList = dingFormVO.getForm_component_values();
        Date finishTime = dingFormVO.getFinish_time();

        School school = new School();
        for (Form_component_values formObjValue:formValuesList){
            String name = formObjValue.getName();
            switch (name){
                case "学校名称":
                    school.setSchoolName(formObjValue.getValue());
                    break;
                case "负责人":
                    school.setLinkMan(formObjValue.getValue());
                    break;
                case "联系方式":
                    school.setLinkPhone(formObjValue.getValue());
                    break;
                case"学校类别":
                    school.setSchoolType(formObjValue.getValue());
                    break;
            }
        }
        List<School> schoolRecords = this.schoolService.findSchoolsByName(school.getSchoolName());
        if(schoolRecords.size() > 0){
            //记录已存在
        }else{
            if(taskLists.size() >= 2){
                Tasks firstTask = taskLists.get(0);
                if(firstTask.getTask_status().equals("COMPLETED")){
                    User user = this.userService.getById(firstTask.getUserid());
                    if(user != null){
                        school.setCityLeaderName(user.getUsername());
                        school.setCityDate(firstTask.getFinish_time());
                    }
                }
                Tasks secondTask = taskLists.get(1);
                if(secondTask.getTask_status().equals("COMPLETED")){
                    User user = this.userService.getById(secondTask.getUserid());
                    if(user != null){
                        school.setProvinceLeaderName(user.getUsername());
                        school.setProvinceDate(secondTask.getFinish_time());
                    }
                }
            }
            School s=this.schoolService.createSchool(school);
            if(s.getBelongId()==null&&s.getSchoolType().equals("主校")){
                s.setBelongId(s.getSchoolId());
                this.schoolService.updateSchool(s);
            }
        }
    }

    @Override
    public void insertAppAbutmentApply(String processInstance) {
        Gson gson = new Gson();
        DingFormVO dingFormVO = gson.fromJson(processInstance, DingFormVO.class);
        //得到审批任务
        List<Tasks> taskLists = dingFormVO.getTasks();
        List<Form_component_values> formValuesList = dingFormVO.getForm_component_values();
        Date finishTime = dingFormVO.getFinish_time();

        Map paramsMap = new HashMap<>();
        //只有最终结果审核通过的才进行操作
        if ("agree".equals(dingFormVO.getResult())) {
        paramsMap.put("originator_userid", dingFormVO.getOriginator_userid());
        paramsMap.put("title", dingFormVO.getTitle());
        paramsMap.put("result", 1);
        paramsMap.put("create_time", dingFormVO.getCreate_time());
        paramsMap.put("finish_time", dingFormVO.getFinish_time());
        for (Form_component_values formObjValue : formValuesList) {
            String name = formObjValue.getName();
            switch (name) {
                case "申请学校":
                    paramsMap.put("apply_school", formObjValue.getValue());
                    break;
                case "联系电话":
                    paramsMap.put("phone", formObjValue.getValue());
                    break;
                case "应用名称":
                    paramsMap.put("app_name", formObjValue.getValue());
                    break;
                case "应用logo":
                    String logo = formObjValue.getValue().substring(2,formObjValue.getValue().length()-2);
                    paramsMap.put("app_logo", logo);
                    break;
                case "应用简介":
                    paramsMap.put("app_inf", formObjValue.getValue());
                    break;
                case "应用首页链接":
                    paramsMap.put("app_main_link", formObjValue.getValue());
                    break;
                case "服务器出口IP":
                    paramsMap.put("export_ip", formObjValue.getValue());
                    break;
                case "PC端首页地址":
                    paramsMap.put("pc_main_address", formObjValue.getValue());
                    break;
                case "管理后台地址":
                    paramsMap.put("manage_address", formObjValue.getValue());
                    break;
            }
        }
        List schoolRecords = appAbutmentService.selectAppAbutmentByAppName(paramsMap);

        if (schoolRecords.size() > 0) {
            //记录已存在
        } else {
            if(taskLists.size() >= 2){
                Tasks firstTask = taskLists.get(0);
                if(firstTask.getTask_status().equals("COMPLETED")){
                    //市级审核人的用户id和审核时间
                    paramsMap.put("city_leader_id", firstTask.getUserid());
                    paramsMap.put("city_approval_time", firstTask.getFinish_time());
                }
                Tasks secondTask = taskLists.get(1);
                if(secondTask.getTask_status().equals("COMPLETED")){
                    //省级审核人的用户id和审核时间
                    paramsMap.put("province_leader_id", firstTask.getUserid());
                    paramsMap.put("province_approval_time", firstTask.getFinish_time());
                }
            }
            this.appAbutmentService.insertAppAbutmentApply(paramsMap);
        }
    }
    }
}
