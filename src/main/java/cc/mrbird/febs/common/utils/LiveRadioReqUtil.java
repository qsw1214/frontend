package cc.mrbird.febs.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视频推流请求工具类
 * @author  psy
 */
public class LiveRadioReqUtil {
    /**
     * 视频来源地址
     */
    public static final String LIVE_RADIO_URL = "http://192.168.0.109:81/cmd.vc?";

    /**
     * 列举指定日期的所有日志记录
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static List<RadioLog> getPointLogRecord(String year,String month,String day){
        StringBuffer buff = new StringBuffer();
        buff.append("cmdid=1&year=" + year + "&month=" + month + "&day=" + day);
        String url = LIVE_RADIO_URL + buff.toString();
        String response = HttpUtils.doGet(url);
        List<RadioLog> list = new ArrayList<RadioLog>();
        if(StringUtils.isNotEmpty(response)) {
            String[] logArrs = response.split("\r\n");
            for (int i = 0; i < logArrs.length; i++) {
                String log = logArrs[i];
                String[] details = log.split("`");
                RadioLog radioLog = new RadioLog();
                radioLog.setLogId(details[0]);
                radioLog.setLogTime(details[1]);
                radioLog.setContent(details[2]);
                list.add(radioLog);
            }
        }
        return list;
    }

    /**
     * 注册一个新的推流地址
     */
    public static String[] getNewLiveRadioUrl(){
        StringBuffer buff = new StringBuffer();
        buff.append("cmdid=2");
        String url = LIVE_RADIO_URL + buff.toString();
        String response = HttpUtils.doGet(url);
        String[] urlStrs = new  String[0];
        if(StringUtils.isNotEmpty(response)) {
            if (StringUtils.isNotEmpty(response)) {
                urlStrs = response.split("\r\n");
//                System.out.println("推流地址：" + urlStrs[0] + "  播放地址：" + urlStrs[1]);  //推流地址：live/65  播放地址：65/65.m3u8
            }
        }
        return urlStrs;
    }

    /**
     * 删除指定的推流地址
     * @param targetUrl
     * @return
     */
    public static String deleteLiveRadioUrl(String targetUrl){
        StringBuffer buff = new StringBuffer();
        buff.append("cmdid=3&addr=" + targetUrl);
        String url = LIVE_RADIO_URL + buff.toString();
        String response = HttpUtils.doGet(url);
        return response;
    }

    /**
     * 查询当前在线（正在推流）的推流源摘要
     * @param targetUrl
     * @return
     */
    public static List<RadioStatus> getRadioStatus(String targetUrl){
        StringBuffer buff = new StringBuffer();
        buff.append("cmdid=4&addr=");
        if(StringUtils.isNotEmpty(targetUrl)){
            buff.append(targetUrl);
        }
        String url = LIVE_RADIO_URL + buff.toString();
        String response = HttpUtils.doGet(url);
        List<RadioStatus> statusList = new ArrayList<RadioStatus>();
        if(StringUtils.isNotEmpty(response)) {
            String[] statusArr = response.split("\r\n");
            for (int i = 0; i < statusArr.length; i++) {
                String statusInfo = statusArr[i];
                String[] detail = statusInfo.split("`");
                if (detail.length > 0 && !"".equals(detail[0])) {
                    RadioStatus status = new RadioStatus();
                    status.setUrl(detail[0]);
                    status.setStatus(detail[1]);
                    if (detail.length > 2) {
                        status.setStartDate(detail[2]);
                    }
                    statusList.add(status);
                }
            }
        }
        return statusList;
    }

    /**
     * 统计推流次数。统计时间范围：[开始时间，结束时间]
     * @param byear
     * @param bmonth
     * @param bday
     * @param eyear
     * @param emonth
     * @param eday
     * @param targetUrl
     * @return Map<String,String> Key为推流地址，Value为地址对应的统计次数
     */
    public static Map<String,Integer> getRadioPlayCount(String byear,String bmonth,String bday,
                                                String eyear,String emonth,String eday,
                                                String targetUrl){
        Map<String,Integer> map = new HashMap<String,Integer>();
        StringBuffer buff = new StringBuffer();
        buff.append("cmdid=5&byear=" + byear + "&bmonth=" + bmonth + "&bday=" + bday
            + "&eyear=" + eyear + "&emonth=" + emonth + "&eday=" + eday + "&addr=");
        if(StringUtils.isNotEmpty(targetUrl)){
            buff.append(targetUrl);
        }
        String url = LIVE_RADIO_URL + buff.toString();
        String response = HttpUtils.doGet(url);
        if(StringUtils.isNotEmpty(response)){
            String[] countArr = response.split("\r\n");
            for (int i = 0 ; i < countArr.length ; i++){
                String countStr = countArr[i];
                String[] addCount = countStr.split("`");
                map.put(addCount[0],Integer.valueOf(addCount[1]));
            }
        }
        return map;
    }

    /**
     * 查询历史推流播放记录
     * @param byear
     * @param bmonth
     * @param bday
     * @param eyear
     * @param emonth
     * @param eday
     * @param targetUrl
     * @return
     */
    public static List<RadioHistoryRecord> getHistoryRadioPlayRecord(String byear,String bmonth,String bday,
                                                String eyear,String emonth,String eday,
                                                String targetUrl){
        Map<String,String> map = new HashMap<String,String>();
        StringBuffer buff = new StringBuffer();
        buff.append("cmdid=6&byear=" + byear + "&bmonth=" + bmonth + "&bday=" + bday
                + "&eyear=" + eyear + "&emonth=" + emonth + "&eday=" + eday + "&addr=");
        if(StringUtils.isNotEmpty(targetUrl)){
            buff.append(targetUrl);
        }
        String url = LIVE_RADIO_URL + buff.toString();
        String response = HttpUtils.doGet(url);
        List<RadioHistoryRecord> historyList = new ArrayList<RadioHistoryRecord>();
        if(StringUtils.isNotEmpty(response)) {
            String[] historyArr = response.split("\r\n");
            for (int i = 0; i < historyArr.length; i++) {
                String historyStr = historyArr[i];
                String[] history = historyStr.split("`");
                RadioHistoryRecord record = new RadioHistoryRecord();
                record.setUrl(history[0]);
                record.setBeginDate(history[1]);
                record.setEndDate(history[2]);
                historyList.add(record);
            }
        }
        return historyList;
    }

    public static void main(String[] args){

        String[] test = "".split("\r\n");
        System.out.println("22:"  + test.length);
        String[] urlStrs = getNewLiveRadioUrl();
        System.out.println("推流地址：" + urlStrs[0] + "  播放地址：" + urlStrs[1]);  //推流地址：live/65  播放地址：65/65.m3u8
        //统计推流次数
        System.out.println(getRadioPlayCount("2019","10","20",
                "2019","10","21","live/7"));  //{live/7=6}

        //获取推流状态
        List<RadioStatus> list = getRadioStatus("live/7");
        for (int i = 0 ; i < list.size(); i++){
            System.out.println("指定地址：" + list.get(i).getUrl() + " " + list.get(i).getStatus() + " " + list.get(i).getStartDate());
        } //live/7 1 2019-10-21 18:00:16   无推流时:live/7 0 null

        List<RadioStatus> list2 = getRadioStatus(null);
        for (int i = 0 ; i < list2.size(); i++){
            System.out.println(list2.get(i).getUrl() + " " + list2.get(i).getStatus() + " " + list2.get(i).getStartDate());
        } //live/7 1 2019-10-21 18:21:26   无推流时，没有返回内容

        //查询历史推流播放记录
        List<RadioHistoryRecord> list3 = getHistoryRadioPlayRecord("2019","10","20",
                "2019","10","21","live/7");
        for (int i = 0 ; i < list3.size(); i++){
            System.out.println(list3.get(i).getUrl() + " " + list3.get(i).getBeginDate() + " " + list3.get(i).getEndDate());
        }
/*        live/7 1571645854 1571646301
        live/7 1571647585 1571648000
        live/7 1571648054 1571648158
        live/7 1571648190 1571648438
        live/7 1571648439 1571648453
        live/7 1571648454 1571648843
        live/7 1571652016 1571652575*/
    }
}
