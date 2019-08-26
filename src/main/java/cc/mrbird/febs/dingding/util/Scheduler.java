package cc.mrbird.febs.dingding.util;

import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.mapper.UserMapper;
import cc.mrbird.febs.system.service.IUserService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Scheduler{
    @Autowired
    private UserMapper userMapper;

    AccessTokenUtil accessToken = new AccessTokenUtil();

    //@Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void testTasks() {
            List<User> list=userMapper.findAllUser();

            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
            OapiDepartmentListRequest request = new OapiDepartmentListRequest();
            request.setId("1");
            request.setHttpMethod("GET");
            try {
                OapiDepartmentListResponse response = client.execute(request, accessToken.getToken());
                JSONObject json = JSONObject.parseObject(response.getBody());
                JSONArray jsa = (JSONArray) json.get("department");
                for (int i = 0; i < jsa.size(); i++) {
                    DingTalkClient client1 = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/simplelist");
                    OapiUserSimplelistRequest request1 = new OapiUserSimplelistRequest();
                    request1.setDepartmentId(((JSONObject) jsa.get(i)).getLong("id"));
                    request1.setOffset(0L);
                    request1.setSize(10L);
                    request1.setHttpMethod("GET");

                    OapiUserSimplelistResponse response1 = client1.execute(request1, accessToken.getToken());

                    JSONObject userBody = JSONObject.parseObject(response1.getBody());
                    JSONArray nameList = (JSONArray) userBody.get("userlist");
                    for (int j = 0; j < nameList.size(); j++) {
                        String name = ((JSONObject) nameList.get(j)).getString("name");
                        //User user = userMapper.findByName(name);
                        System.out.println(list);
                    }
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
    }

    //@Scheduled(fixedRate = 2000)
    public void testTasks1() throws ApiException {
                DingTalkClient client1 = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/list");
                OapiRoleListRequest request1 = new OapiRoleListRequest();
                request1.setOffset(0L);
                request1.setSize(10L);
                OapiRoleListResponse response1 = null;
                try {
                    response1 = client1.execute(request1, accessToken.getToken());
                } catch (ApiException e) {
                    e.printStackTrace();
                }
                JSONObject jo = JSONObject.parseObject(response1.getBody());
                JSONArray jsonArray=((JSONArray)((JSONObject)jo.get("result")).get("list"));
                for(int i=0;i<jsonArray.size();i++) {
                    JSONArray roleArray=((JSONArray)(((JSONObject)(jsonArray.get(i))).get("roles")));
                    for(int j=0;j<roleArray.size();j++) {
                        Object name=((JSONObject)roleArray.get(j)).get("name");
                        Long id=((JSONObject)roleArray.get(j)).getLong("id");
                        if(name.equals("省领导")){
                            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/simplelist");
                            OapiRoleSimplelistRequest request = new OapiRoleSimplelistRequest();
                            request.setRoleId(id);
                            request.setOffset(0L);
                            request.setSize(10L);

                            OapiRoleSimplelistResponse response = client.execute(request, accessToken.getToken());
                            //System.out.println(response.getBody());
                        }

                        if(name.equals("市领导")){
                            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/simplelist");
                            OapiRoleSimplelistRequest request = new OapiRoleSimplelistRequest();
                            request.setRoleId(id);
                            request.setOffset(0L);
                            request.setSize(10L);

                            OapiRoleSimplelistResponse response = client.execute(request, accessToken.getToken());
                            //System.out.println(response.getBody());
                        }

                        if(name.equals("县领导")){
                            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/simplelist");
                            OapiRoleSimplelistRequest request = new OapiRoleSimplelistRequest();
                            request.setRoleId(id);
                            request.setOffset(0L);
                            request.setSize(10L);

                            OapiRoleSimplelistResponse response = client.execute(request, accessToken.getToken());
                            //System.out.println(response.getBody());
                        }

                        if(name.equals("校长")){
                            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/simplelist");
                            OapiRoleSimplelistRequest request = new OapiRoleSimplelistRequest();
                            request.setRoleId(id);
                            request.setOffset(0L);
                            request.setSize(10L);

                            OapiRoleSimplelistResponse response = client.execute(request, accessToken.getToken());
                            JSONObject headmasterBody = JSONObject.parseObject(response.getBody());
                            JSONArray headmaster=((JSONArray)((JSONObject)headmasterBody.get("result")).get("list"));
                            for(int a=0;a<headmaster.size();a++){
                                Object headmasterName=((JSONObject)headmaster.get(a)).get("name");
                                System.out.println((String) headmasterName+name.toString());
                                userMapper.updateUser(headmasterName.toString(),name.toString(),77);
                            }
                        }
                    }
                }

    }
}
