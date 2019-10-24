package cc.mrbird.febs.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 时间工具类
 *
 * @author MrBird
 */
public class DateUtil {

    public static final String FULL_TIME_PATTERN_NO_DETAIL = "yyyy-MM-dd";

    public static final String FULL_TIME_PATTERN = "yyyyMMddHHmmss";

    public static final String FULL_TIME_SPLIT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String CST_TIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";

    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, FULL_TIME_PATTERN);
    }

    public static String formatFullTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    public static String getDateFormat(Date date, String dateFormatType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatType, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static Date getNowDateTime(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FULL_TIME_SPLIT_PATTERN, Locale.CHINA);
        try {
            return simpleDateFormat.parse(simpleDateFormat.format(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
        }
        return date;
    }

    public static String formatCSTTime(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CST_TIME_PATTERN, Locale.US);
        Date usDate = simpleDateFormat.parse(date);
        return DateUtil.getDateFormat(usDate, format);
    }

    public static String formatInstant(Instant instant, String format) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static Integer getLatestYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static Integer getLatestMonth(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static Integer getLatestDay(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取最近12个月数据
     */
    public static String[] getLast12Months(){

        String[] months = new String[12];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        // 加一行代码,否则3月重复
        cal.set(Calendar.DATE,1);
        for (int i = 0; i < 12; i++) {
            String localMonth = (cal.get(Calendar.MONTH) + 1) + "";
            if(localMonth.length() <= 1){
                localMonth = "0" + localMonth;
            }
            months[11 - i] = cal.get(Calendar.YEAR) + "-" + localMonth;
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        }
        return months;
    }

    public static void main(String[] args) {
        String[] months = new String[12];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        // 加一行代码,否则3月重复
        cal.set(Calendar.DATE,1);
        for (int i = 0; i < 12; i++) {
            String localMonth = (cal.get(Calendar.MONTH) + 1) + "";
            if(localMonth.length() <= 1){
                localMonth = "0" + localMonth;
            }
            months[11 - i] = cal.get(Calendar.YEAR) + "-" + localMonth;
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        }
        for (int i = 0; i < months.length; i++) {
            System.out.println(months[i]);
        }

        System.out.println("2019-10-11".substring(0,7));

        Map<String,Integer> map = new TreeMap<String,Integer>();
        map.put("2019-10",20);
        map.put("2018-02",22);
        map.put("2019-11",22);
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + map.get(key));
        }
    }
}
