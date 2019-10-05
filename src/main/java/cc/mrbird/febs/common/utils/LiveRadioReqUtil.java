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
    public static final String LIVE_RADIO_URL = "http://127.0.0.1:81/cmd.vc?";

    /**
     * 列举指定日期的所有日志记录
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static List<RadioLog> getPointLogRecord(String year,String month,String day){
        StringBuffer buff = new StringBuffer();
        buff.append("cmid=1&year=" + year + "&month=" + month + "&day=" + day);
        String url = LIVE_RADIO_URL + buff.toString();
        String response = HttpUtils.doGet(url);
        String[] logArrs = response.split("\r\n");
        List<RadioLog> list = new ArrayList<RadioLog>();
        for (int i = 0 ; i < logArrs.length; i++){
            String log = logArrs[i];
            String[] details = log.split("'");
            RadioLog radioLog = new RadioLog();
            radioLog.setLogId(details[0]);
            radioLog.setLogTime(details[1]);
            radioLog.setContent(details[2]);
            list.add(radioLog);
        }
        return list;
    }

    /**
     * 注册一个新的推流地址
     */
    public static String getNewLiveRadioUrl(){
        StringBuffer buff = new StringBuffer();
        buff.append("cmid=2");
        String url = LIVE_RADIO_URL + buff.toString();
        String response = HttpUtils.doGet(url);
        return response;
    }

    /**
     * 删除指定的推流地址
     * @param targetUrl
     * @return
     */
    public static String deleteLiveRadioUrl(String targetUrl){
        StringBuffer buff = new StringBuffer();
        buff.append("cmid=3&addr=" + targetUrl);
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
        buff.append("cmid=4&addr=");
        if(StringUtils.isNotEmpty(targetUrl)){
            buff.append(targetUrl);
        }
        String url = LIVE_RADIO_URL + buff.toString();
        String response = HttpUtils.doGet(url);
        String[] statusArr = response.split("\r\n");
        List<RadioStatus> statusList = new ArrayList<RadioStatus>();
        for (int i = 0 ; i < statusArr.length; i++){
            String statusInfo = statusArr[i];
            String[] detail = statusInfo.split("'");
            RadioStatus status = new RadioStatus();
            status.setUrl(detail[0]);
            status.setStatus(detail[1]);
            status.setStartDate(detail[3]);
            statusList.add(status);
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
        buff.append("cmid=5&byear=" + byear + "&bmonth=" + bmonth + "&bday=" + bday
            + "&eyear=" + eyear + "&emonth=" + emonth + "&eday=" + eday + "&addr=");
        if(StringUtils.isNotEmpty(targetUrl)){
            buff.append(targetUrl);
        }
        String url = LIVE_RADIO_URL + buff.toString();
        String response = HttpUtils.doGet(url);
        String[] countArr = response.split("\r\n");
        for (int i = 0 ; i < countArr.length ; i++){
            String countStr = countArr[i];
            String[] addCount = countStr.split("'");
            map.put(addCount[0],Integer.valueOf(addCount[1]));
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
        buff.append("cmid=6&byear=" + byear + "&bmonth=" + bmonth + "&bday=" + bday
                + "&eyear=" + eyear + "&emonth=" + emonth + "&eday=" + eday + "&addr=");
        if(StringUtils.isNotEmpty(targetUrl)){
            buff.append(targetUrl);
        }
        String url = LIVE_RADIO_URL + buff.toString();
        String response = HttpUtils.doGet(url);
        List<RadioHistoryRecord> historyList = new ArrayList<RadioHistoryRecord>();
        String[] historyArr = response.split("\r\n");
        for (int i = 0 ; i < historyArr.length ; i++){
            String historyStr = historyArr[i];
            String[] history = historyStr.split("'");
            RadioHistoryRecord record = new RadioHistoryRecord();
            record.setUrl(history[0]);
            record.setBeginDate(history[1]);
            record.setEndDate(history[2]);
            historyList.add(record);
        }
        return historyList;
    }
}
