package org.hj.chain.platform;

import cn.hutool.core.util.StrUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.time.temporal.WeekFields;
import java.util.Date;

/**
 * @Project : chem-erp
 * @Description : TODO
 * @Author : lijinku
 * @Iteration : 1.0
 * @Date : 2019/7/27  1:25 AM
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2019/07/27    create
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";


    public static LocalDateTime getDefaultDateTime() {
        return LocalDateTime.parse("1970-01-01 01:01:01", DateTimeFormatter.ofPattern(DEFAULT_FORMAT_TIME));
    }

    /**
     * @Description: TODO 转换年月字符串
     * @Param: [localDate]
     * @return: java.lang.String
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2020/5/15 11:53 上午
     */
    public static String getYearMonth(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        String format = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return format;
    }

    public static String getDateStr(LocalDate localDate, String format) {
        if (format == null) {
            format = DEFAULT_FORMAT;
        }
        if (localDate == null) {
            return null;
        }
        return localDate.format(DateTimeFormatter.ofPattern(format));
    }


    public static String getDefaultFormatDateStr(LocalDateTime localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DEFAULT_FORMAT));
    }



    public static String getDefaultTimeDateYYYYmmddHHmm(LocalDateTime localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static String getDefaultTimeDateStr(LocalDateTime localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("HH:mm"));
    }


    public static LocalDateTime parseStringToDateTime(String time) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DEFAULT_FORMAT_TIME);
        return LocalDateTime.parse(time, df);
    }

    public static LocalDateTime parseDateStr(String dateStr, String format) {
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取时间实例
        Instant instant = date.toInstant();
        //获取时间地区ID
        ZoneId zoneId = ZoneId.systemDefault();
        //转换为LocalDate
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        //获得LocalDateTime时间戳(东八区)
        localDateTime.toEpochSecond(ZoneOffset.of("+8"));

        return localDateTime;
    }

    public static LocalDate parseDate(String dateStr, String format) {
        if (StrUtil.isBlank(dateStr)) {
            return null;
        }
        if (StrUtil.isBlank(format)) {
            format = DEFAULT_FORMAT;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(dateStr, formatter);
    }

    /**
     * @Description: TODO 获取改日期是在该年第几周
     * @Param: [localDate]
     * @return: int
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2020/4/9 8:50 PM
     */
    public static int getWeekOfYear(LocalDate localDate) {
        WeekFields weekFields = WeekFields.ISO;
        return localDate.get(weekFields.weekOfYear());
    }

    /**
     * @Description: TODO 自定义获取当前日期是第几周（周六到周五是一周，闭区间）
     * @Param: [localDate]
     * @return: int
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2020/4/12 11:19 AM
     */
    public static int selfFstWeekOfYear(LocalDate localDate) {
        //1.自定义周几是一周的第一天，且一周有几天
        WeekFields weekFields = WeekFields.of(DayOfWeek.SATURDAY, 7);
        //2.通过日期获取当前是第几周
        int fstWeek = localDate.get(weekFields.weekOfYear());

        //3.默认第一周日期为0，则需要加1
        return fstWeek + 1;

    }


    /**
     * @Description: TODO 根据自定义的周计算，获取该周的日期范围
     * @Param: [localDate]
     * @return: java.lang.String
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2020/4/12 11:53 AM
     */
    public static String getSelfWeekDateRangeByDate(LocalDate localDate) {
        String startDateStr = null;
        String endDateStr = null;
        //1.自定义周几是一周的第一天，且一周有几天
        WeekFields weekFields = WeekFields.of(DayOfWeek.SATURDAY, 7);
        //2.该日期是这周的第几天
        int dayOfWeek = localDate.get(weekFields.dayOfWeek());
        //3.往前推 dayOfWeek-1天，是该周第一天的日期
        LocalDate startDate = localDate.minusDays(dayOfWeek - 1);
        //4.判断startDate是否同localDate是同年，若不是同年，则设定为该年的第一天
        startDateStr = startDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        if (startDate.getYear() != localDate.getYear()) {
            startDateStr = localDate.getYear() + "/01/01";
        }

        //5.往后推7-dayOfWeek是该周的最后一天
        LocalDate endDate = localDate.plusDays(7 - dayOfWeek);
        endDateStr = endDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        //6.判断该年是否是与localDate同年，且不是未来的一天
        if (localDate.getYear() != endDate.getYear()) {
            endDateStr = localDate.getYear() + "/12/31";
        } else {
            LocalDate now = LocalDate.now();
            if (endDate.isAfter(now)) {
                endDateStr = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            }
        }


        return startDateStr + "-" + endDateStr;
    }


    public static int getFstMonthByYYYYmm(String month) {
        month = month + "-01";
        LocalDate localDate = LocalDate.parse(month, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int monthValue = localDate.getMonthValue();
        return monthValue;
    }

    public static void main(String[] args) throws ParseException {
//        LocalDate localDateTime = DateUtils.parseDate("2019-07-22", "yyyy-MM-dd");
//        System.out.println(localDateTime);

        LocalDate now = LocalDate.now();
        LocalDate plus = now.plus(8, ChronoUnit.MONTHS);
        String dateStr = getDateStr(plus, "yyyy年M月dd日");
        System.out.println(dateStr);

        String rangeByDate = getSelfWeekDateRangeByDate(LocalDate.parse("2020-01-06", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        System.out.println(rangeByDate);


        LocalDateTime defaultDateTime = getDefaultDateTime();
        System.out.println(defaultDateTime);

        System.out.println(LocalDateTime.now());

        String defaultTimeDateStr = getDefaultTimeDateStr(LocalDateTime.now());
        System.out.println(defaultTimeDateStr);
    }
}