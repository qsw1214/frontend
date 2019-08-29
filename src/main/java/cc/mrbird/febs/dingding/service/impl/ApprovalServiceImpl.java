package cc.mrbird.febs.dingding.service.impl;

import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.service.ISchoolService;
import cc.mrbird.febs.dingding.service.IApprovalService;
import cc.mrbird.febs.dingding.vo.DingFormVO;
import cc.mrbird.febs.dingding.vo.Form_component_values;
import cc.mrbird.febs.dingding.vo.Tasks;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IUserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ApprovalServiceImpl implements IApprovalService{

    @Autowired
    private IUserService userService;

    @Autowired
    private ISchoolService schoolService;

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
            this.schoolService.createSchool(school);
        }
    }
}
