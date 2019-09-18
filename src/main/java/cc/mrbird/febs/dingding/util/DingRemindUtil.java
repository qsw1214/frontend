package cc.mrbird.febs.dingding.util;

import cc.mrbird.febs.dingding.config.Constant;
import cc.mrbird.febs.dingding.config.URLConstant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiCalendarCreateRequest;
import com.dingtalk.api.response.OapiCalendarCreateResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DingRemindUtil {
    private static final Logger bizLogger = LoggerFactory.getLogger(DingRemindUtil.class);

    public static void doDingRemind(Map params) throws RuntimeException {
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_CALENDAR_CREATE);
            OapiCalendarCreateRequest request = new OapiCalendarCreateRequest();
            OapiCalendarCreateRequest.OpenCalendarCreateVo creatVo = new OapiCalendarCreateRequest.OpenCalendarCreateVo();
            //请求的唯一标识, 保证请求唯一性
            creatVo.setUuid("tang123456");
            //业务方自己的主键
            creatVo.setBizId("tang123");
            //创建者userid
            creatVo.setCreatorUserid("176415581621823364");
            //日程主题
            creatVo.setSummary("还有10分钟就上课啦！！！");
            //接收者userid
            creatVo.setReceiverUserids(Arrays.asList(String.valueOf(params.get("userId"))));
            OapiCalendarCreateRequest.DatetimeVo start = new OapiCalendarCreateRequest.DatetimeVo();
            //开始的unix时间戳(单位:秒)
            long startTime = (long) params.get("beginTime");
            start.setUnixTimestamp(startTime*1000);
            //1567042326
            System.out.println(System.currentTimeMillis());
            //开始时间,从数据库获取
            creatVo.setStartTime(start);
            OapiCalendarCreateRequest.DatetimeVo end = new OapiCalendarCreateRequest.DatetimeVo();
            //结束的unix时间戳(单位:秒)
            long endTime = (long) params.get("endTime");
            end.setUnixTimestamp(endTime*1000);
            //1567128726
            //结束时间
            creatVo.setEndTime(end);
            //日程类型:notification-提醒
            creatVo.setCalendarType("notification");
            OapiCalendarCreateRequest.OpenCalendarSourceVo source = new OapiCalendarCreateRequest.OpenCalendarSourceVo();
            //日程来源
            source.setTitle("官方");
            //点击日程跳转目标地址
            source.setUrl("/");
            //显示日程来源
            creatVo.setSource(source);
            //事项开始前提醒
            OapiCalendarCreateRequest.OpenCalendarReminderVo reminder = new OapiCalendarCreateRequest.OpenCalendarReminderVo();
            //距开始时间多久开始提醒
            reminder.setMinutes((long)10);
            //提醒类型
            reminder.setRemindType("app");
            //距开始时间多久开始提醒
            creatVo.setReminder(reminder);
            //创建日程实体
            request.setCreateVo(creatVo);
            //这里调用的ACCESS_TOKEN是企业内部应用的ACCESS_TOKEN
            OapiCalendarCreateResponse response = client.execute(request, AccessTokenUtil.getToken(Constant.APPKEY,Constant.APPSECRET));
            if(response.isSuccess()){
                System.out.println("日程创建成功");
            }
        } catch (ApiException e) {
            bizLogger.error("create calendar failed", e);
            throw new RuntimeException();
        }

    }

    /**
     * 将开始时间先精确到分钟，在转成unix时间戳(单位:毫秒)
     * @param args
     */
    public static void main(String[] args) {
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_CALENDAR_CREATE);
            OapiCalendarCreateRequest request = new OapiCalendarCreateRequest();
            OapiCalendarCreateRequest.OpenCalendarCreateVo creatVo = new OapiCalendarCreateRequest.OpenCalendarCreateVo();
            //请求的唯一标识, 保证请求唯一性
            creatVo.setUuid("123456");
            //业务方自己的主键
            creatVo.setBizId("test123");
            //创建者userid
            creatVo.setCreatorUserid("176415581621823364");
            //日程主题
            creatVo.setSummary("还有5分钟就上课啦！！！");
            //接收者userid
            creatVo.setReceiverUserids(Arrays.asList("176415581621823364"));
            OapiCalendarCreateRequest.DatetimeVo start = new OapiCalendarCreateRequest.DatetimeVo();
            //开始的unix时间戳(单位:秒)
            start.setUnixTimestamp((long)1567059000*1000);
            //1567042326
            System.out.println(System.currentTimeMillis());
            //开始时间,从数据库获取
            creatVo.setStartTime(start);
            OapiCalendarCreateRequest.DatetimeVo end = new OapiCalendarCreateRequest.DatetimeVo();
            //结束的unix时间戳(单位:秒)
            end.setUnixTimestamp((long)1567059600*1000);
            //1567128726
            //结束时间
            creatVo.setEndTime(end);
            //日程类型:notification-提醒
            creatVo.setCalendarType("notification");
            OapiCalendarCreateRequest.OpenCalendarSourceVo source = new OapiCalendarCreateRequest.OpenCalendarSourceVo();
            //日程来源
            source.setTitle("招聘");
            //点击日程跳转目标地址
            source.setUrl("/");
            //显示日程来源
            creatVo.setSource(source);
            //事项开始前提醒
            OapiCalendarCreateRequest.OpenCalendarReminderVo reminder = new OapiCalendarCreateRequest.OpenCalendarReminderVo();
            //距开始时间多久开始提醒
            reminder.setMinutes((long)1);
            //提醒类型
            reminder.setRemindType("app");
            //距开始时间多久开始提醒
            creatVo.setReminder(reminder);
            //创建日程实体
            request.setCreateVo(creatVo);
            //这里调用的ACCESS_TOKEN是企业内部应用的ACCESS_TOKEN
            OapiCalendarCreateResponse response = client.execute(request, AccessTokenUtil.getToken(Constant.APPKEY,Constant.APPSECRET));
            if(response.isSuccess()){
                System.out.println("日程创建成功");
            }
        } catch (ApiException e) {
            bizLogger.error("create calendar failed", e);
            throw new RuntimeException();
        }
    }
}
