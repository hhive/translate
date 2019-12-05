package org.example.translate.commom.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import lombok.Getter;

/**
 * 时间工具类
 *
 * @author jinyanan
 * @version 1.0
 * @date 2017年1月17日
 * @since v1.0
 */
public final class DateUtil {

    /**
     * logger
     */
    private static Logger log = Logger.getLogger(DateUtil.class);

    /**
     * DATE_FORMAT_1
     */
    public static final String DATE_FORMAT_1 = "yyyy-MM-dd";

    /**
     * DATE_FORMAT_2
     */
    public static final String DATE_FORMAT_2 = "yyyyMMdd";

    /**
     * DATE_FORMAT_4
     */
    public static final String DATE_FORMAT_4 = "yyyyMM";

    /**
     * DATE_FORMAT_5
     */
    public static final String DATE_FORMAT_5 = "yyyy";

    /**
     * DATETIME_FORMAT_1
     */
    public static final String DATETIME_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    /**
     * DATETIME_FORMAT_3
     */
    public static final String DATETIME_FORMAT_3 = "yyyy-MM-dd HH-mm-ss";

    /**
     * DATETIME_FORMAT_2
     */
    public static final String DATETIME_FORMAT_2 = "yyyyMMddHHmmss";

    /**
     * DATETIME_FORMAT_4
     */
    public static final String DATETIME_FORMAT_4 = "yyyy/MM/dd HH:mm:ss";

    /**
     * DATETIME_FORMAT_5
     */
    public static final String DATETIME_FORMAT_5 = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * DATETIME_FORMAT_6
     */
    public static final String DATETIME_FORMAT_6 = "yyyy-MM-dd HH:mm:ss";

    /**
     * DATE_FORMAT_3
     */
    public static final String DATE_FORMAT_3 = "yyyy年MM月dd日";

    /**
     * TIME_FORMAT_1
     */
    public static final String TIME_FORMAT_1 = "HHmmss";

    /**
     * DEFAULT_DATE_FORMAT
     */
    public static final String DEFAULT_DATE_FORMAT = DATE_FORMAT_1;

    /**
     * DEFAULT_TIME_FORMAT
     */
    public static final String DEFAULT_TIME_FORMAT = DATETIME_FORMAT_1;

    /**
     * NAME_FILE_DATE_FORMAT
     */
    public static final String NAME_FILE_DATE_FORMAT = "yyyyMMdd_HHmmss";

    /** 年月日时分秒总计长度 11 */
    private static final Integer DATE_TIME_LENGTH_11 = 11;

    /** 年月日总计长度 8 */
    private static final Integer DATE_LENGTH_8 = 8;

    /**
     * 私有构造函数
     */
    private DateUtil() {}

    /**
     * 根据传入format格式化日期
     *
     * @param format format
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getDateFormat(String format) {
        return new SimpleDateFormat(format);
    }

    /**
     * 按照format把date转为string
     *
     * @param date date
     * @param format format
     * @return String
     */
    public static String date2String(Date date, String format) {
        if (date == null) {
            return StringUtil.EMPTY;
        }
        SimpleDateFormat sdf = getDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取当前时间，使用默认format
     *
     * @param date date
     * @return String
     */
    public static String date2String(Date date) {
        return date2String(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * 获取当前时间
     *
     * @return String
     */
    public static String getCurrentDate() {
        Date date = new Date();
        return date2String(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * 文件命名用时间String
     *
     * @return String
     */
    public static String getNameFileCurrentDate() {
        Date date = new Date();
        return date2String(date, NAME_FILE_DATE_FORMAT);
    }

    /**
     * 把string转date，会尝试各种format格式
     *
     * @param date date
     * @return Date
     */
    public static Date string2Date(String date) {
        Date ret;
        if (StringUtil.isEmpty(date)) {
            return null;
        }
        if (date.length() > DATE_TIME_LENGTH_11) {
            if (date.contains(SymbolConstants.MINUS)) {
                if (date.contains(SymbolConstants.COLON)) {
                    ret = string2Date(date, DATETIME_FORMAT_1);
                } else {
                    ret = string2Date(date, DATETIME_FORMAT_3);
                }
            } else if (date.contains(SymbolConstants.BACKSLASH)) {
                ret = string2Date(date, DATETIME_FORMAT_4);
            } else {
                ret = string2Date(date, DATETIME_FORMAT_2);
            }
        } else {
            if (date.contains(SymbolConstants.MINUS)) {
                ret = string2Date(date, DATE_FORMAT_1);
            } else if (date.length() == DATE_LENGTH_8) {
                ret = string2Date(date, DATE_FORMAT_2);
            } else {
                ret = string2Date(date, DATE_FORMAT_3);
            }
        }
        return ret;
    }

    /**
     * 按照format把string转date
     *
     * @param date 时间
     * @param format 格式
     * @return Date
     */
    public static Date string2Date(String date, String format) {
        if (StringUtil.isEmpty(format)) {
            throw new IllegalArgumentException("the date format string is null!");
        }
        SimpleDateFormat sdf = getDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("the date string " + date + " is not matching format: " + format, e);
        }
    }

    /**
     * 获取系统当前时间 yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    public static String getStandardNowTime() {
        SimpleDateFormat sdf = getDateFormat(DEFAULT_TIME_FORMAT);
        return sdf.format(new Date());
    }


    /**
     * 获取当前日期和时间
     *
     * @param format 日期格式 例：yyyy-MM-dd hh:mm
     * @return String
     */
    public static String getNowDate(String format) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 根据date计算seconds后的时间
     *
     * @param date 当前时间
     * @param seconds 秒数
     * @return Date 计算后的时间
     */
    public static Date offsetSecond(Date date, long seconds) {
        if (date == null) {
            return null;
        }
        long time = date.getTime();
        long time2 = time + (seconds * 1000);
        long time3 = time + (seconds * 1000) - 60 * 60 * 1000;
        Date date2 = new Date(time2);
        Date date3 = new Date(time3);

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);
        Calendar calendarDate2 = Calendar.getInstance();
        calendarDate2.setTime(date2);
        Calendar calendarDate3 = Calendar.getInstance();
        calendarDate3.setTime(date3);

        long dstDate = calendarDate.get(Calendar.DST_OFFSET);
        long dstDate2 = calendarDate2.get(Calendar.DST_OFFSET);
        long dstDate3 = calendarDate3.get(Calendar.DST_OFFSET);

        long dstOffset = dstDate - dstDate2;
        // 前后两个日期偏移相同（含不偏移）或者夏令日开始的那个小时不用补偿，否则要补偿偏移量。
        boolean isNeedReset = dstOffset == 0 || (dstDate2 - dstDate3 != 0 && dstDate2 != 0);
        if (!isNeedReset) {
            return date2;
        } else {
            return new Date(time2 + dstOffset);
        }
    }


    /**
     * 根据date计算minutes后的时间
     *
     * @param date 当前时间
     * @param minutes 要计算的分钟数
     * @return Date 计算后的时间
     */
    public static Date offsetMinute(Date date, long minutes) {
        return offsetSecond(date, 60 * minutes);
    }

    /**
     * 日期添加多少分钟后的时间
     *
     * @param day 当前时间
     * @param x 要加的分钟
     * @return 处理后的时间
     */
    public static Date addDateMinut(Date day, int x) {
        return offsetMinute(day, x);
    }

    /**
     * 日期减少多少分钟后的时间
     *
     * @param day 当前时间
     * @param x 要减的分钟
     * @return 处理后的时间
     */
    public static String reduceDateMinut(Date day, int x) {
        Date date = addDateMinut(day, -x);
        return getDate(date, DATETIME_FORMAT_1);
    }

    /**
     * 根据date计算hours后的时间
     *
     * @param date 当前时间
     * @param hours 要计算的小时数
     * @return Date 计算后的时间
     */
    public static Date offsetHour(Date date, long hours) {
        return offsetMinute(date, 60 * hours);
    }

    /**
     * 根据date计算days后的时间
     *
     * @param date 当前时间
     * @param days 要计算的天数
     * @return Date 计算后的时间
     */
    public static Date offsetDay(Date date, int days) {
        return offsetHour(date, 24L * days);
    }

    /**
     * 日期加上多少天后的日期
     *
     * @param now 当前时间
     * @param day 要加的天数
     * @return 处理后的时间
     */
    public static Date dateAddDay(Date now, int day) {
        return offsetDay(now, day);
    }

    /**
     * 日期加指定天数
     *
     * @param day 天数
     * @return 返回相加后的日期
     */
    public static Date addDate(Date date, long day) {
        return dateAddDay(date, (int) day);
    }

    /**
     * 日期减指定天数
     *
     * @param day 天数
     * @return 返回相减后的日期
     */
    public static Date deleteDate(Date date, long day) {
        return dateAddDay(date, -(int) day);
    }

    /**
     * 根据date计算weeks后的时间
     *
     * @param date 当前时间
     * @param weeks 要计算的周数
     * @return Date 计算后的时间
     */
    public static Date offsetWeek(Date date, int weeks) {
        return offsetDay(date, 7 * weeks);
    }

    /**
     * 根据date计算months后的时间
     *
     * @param date 当前时间
     * @param months 要计算的月数
     * @return 计算后的时间
     */
    public static Date offsetMonth(Date date, int months) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, months);
        int newMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (curDay == maxDay) {
            calendar.set(Calendar.DAY_OF_MONTH, newMaxDay);
        } else {
            if (curDay > newMaxDay) {
                calendar.set(Calendar.DAY_OF_MONTH, newMaxDay);
            } else {
                calendar.set(Calendar.DAY_OF_MONTH, curDay);
            }
        }
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 日期加上多少月后的日期
     *
     * @param now 时间
     * @param month 要加的月数
     * @return 加上月数后的日期
     */
    public static Date dateAddMonth(Date now, int month) {
        return offsetMonth(now, month);
    }

    /**
     * 日期减上多少月前的日期
     *
     * @param now 当前时间
     * @param month 要减的月
     * @return 处理后的时间
     */
    public static Date dateReduceMonth(Date now, int month) {
        return offsetMonth(now, -month);
    }

    /**
     * 日期减上多少天前的日期
     *
     * @param now 当前时间
     * @param day 要减的时间
     * @return 处理后的时间
     */
    public static Date dateReduceDay(Date now, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, -day);
        return calendar.getTime();
    }

    /**
     * 根据date计算years后的时间
     *
     * @param date 当前时间
     * @param years 要计算的年数
     * @return Date 计算后的时间
     */
    public static Date offsetYear(Date date, int years) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 日期加上多少年后的日期
     *
     * @param now 当前时间
     * @param year 要加的年份
     * @return 处理后的时间
     */
    public static Date dateAddYear(Date now, int year) {
        return offsetYear(now, year);
    }

    /**
     * 计算两个日期之间的差距
     *
     * @param beginDate beginDate
     * @param endDate endDate
     * @param timeType 0:秒级；1:分级:；2:小时级；3:天级；
     * @return 差距
     */
    public static long differDateInDays(Date beginDate, Date endDate, TimeType timeType) {
        long begin = beginDate.getTime();
        long end = endDate.getTime();
        long surplus = end - begin;

        Calendar calendarBeginDate = Calendar.getInstance();
        calendarBeginDate.setTime(beginDate);

        Calendar calendarEndDate = Calendar.getInstance();
        calendarEndDate.setTime(endDate);
        int dstOffset = calendarBeginDate.get(Calendar.DST_OFFSET) - calendarEndDate.get(Calendar.DST_OFFSET);
        surplus += dstOffset;

        long ret = 0;
        switch (timeType) {
            case SECOND:
                // 秒
                ret = surplus / 1000;
                break;
            case MINUTE:
                // 分
                ret = surplus / 1000 / 60;
                break;
            case HOUR:
                // 时
                ret = surplus / 1000 / 60 / 60;
                break;
            case DAY:
                // 天
                ret = surplus / 1000 / 60 / 60 / 24;
                break;
            default:
                break;
        }
        return ret;
    }

    /**
     * 计算两个日期之间的差距
     *
     * @param before beginDate
     * @param after endDate
     * @return 差距
     */
    public static int getBetweenDays(Date before, Date after) {
        return (int) differDateInDays(before, after, TimeType.DAY);
    }

    /**
     * 判断date是否在beginDate和endDate之间（入参都是String类型）
     *
     * @param date date
     * @param beginDate beginDate
     * @param endDate endDate
     * @return true-在范围之内；false-不在范围内
     */
    public static boolean isInRange(String date, String beginDate, String endDate) {

        int dateLen = date.length();
        int beginDateLen = beginDate.length();
        int endDateLen = endDate.length();
        if (!(beginDateLen == dateLen)) {
            log.error("长度不相符");
        }
        boolean asc = isAsc(beginDate, endDate);
        if (asc) {
            return date.compareTo(beginDate) >= 0 && date.compareTo(endDate) <= 0;
        } else {
            return date.compareTo(beginDate) >= 0 || date.compareTo(endDate) <= 0;
        }
    }

    /**
     * 判断date是否在beginDate和endDate之间（入参都是Date类型）
     *
     * @param date date
     * @param beginDate beginDate
     * @param endDate endDate
     * @return true-在范围之内；false-不在范围内
     */
    public static boolean isInRange(Date date, Date beginDate, Date endDate) {
        long time = date.getTime();
        long beginTime = beginDate.getTime();
        long endTime = endDate.getTime();
        return time >= beginTime && time <= endTime;
    }

    /**
     * 比较两个时间
     *
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return 如果时间相同，返回0；如果beginDate早于endDate，返回-1；如果beginDate晚于endDate，返回1；
     */
    public static int compare(Date beginDate, Date endDate) {
        long beginTime = beginDate.getTime();
        long endTime = endDate.getTime();
        return Long.compare(beginTime, endTime);
    }

    /**
     * @param beginDateStr String
     * @param endDateStr String
     * @return boolean true-beginDateStr早于endDateStr, false-beginDateStr晚于endDateStr
     */
    private static boolean isAsc(String beginDateStr, String endDateStr) {
        return (beginDateStr.compareTo(endDateStr) < 0);
    }

    /**
     * 计算时间偏移
     *
     * @param offset 偏移的值
     * @param dateTimeStr 具体的时间日期字符串
     * @param intOut 是输入还是输出，如果是0表示输入，则在对应时间上加上偏移量返回，如果是1表示输出，则在对应时间上减去偏移量
     * @return Date
     */
    public static Date dateOffsetCalc(int offset, String dateTimeStr, int intOut) {
        Date ret = string2Date(dateTimeStr);
        boolean hasTimeStr = dateTimeStr.length() > 11;
        if (ret != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(ret);
            // 表示是输入，加上偏移量
            if (intOut == 0) {
                if (hasTimeStr) {
                    cal.add(Calendar.SECOND, offset);
                } else {
                    cal.add(Calendar.DATE, offset);
                }
            } else {
                if (hasTimeStr) {
                    cal.add(Calendar.SECOND, (-1 * offset));
                } else {
                    cal.add(Calendar.DATE, (-1 * offset));
                }
            }
            ret = cal.getTime();
        }
        return ret;
    }


    /**
     * 获取系统当前时间
     *
     * @return Date
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取某一天的0时0分0秒时间
     *
     * @param dt 某个时间
     * @return 某一天的0时0分0秒时间
     */
    public static Date getStartTimeOfDay(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取某一天的0时0分0秒时间，以{@link #DATETIME_FORMAT_1}格式返回
     *
     * @param date 某个时间
     * @return 字符串类型的某一天的0时0分0秒时间
     */
    public static String getDayBeginStr(Date date) {
        Date startTimeOfDay = getStartTimeOfDay(date);
        return getDate(startTimeOfDay, DATETIME_FORMAT_1);
    }

    /**
     * 跟{@link #getStartTimeOfDay(Date)}方法重复
     *
     * @param date 某个时间
     * @return 某一天的0时0分0秒时间
     */
    public static Date getDayBegin(Date date) {
        return getStartTimeOfDay(date);
    }

    /**
     * 获取某一天的23时59分59秒999毫秒时间
     *
     * @param dt 某个时间
     * @return 某一天的23时59分59秒999毫秒时间
     */
    public static Date getEndTimeOfDay(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取某一天的23时59分59秒时间，以{@link #DATETIME_FORMAT_1}格式返回
     *
     * @param date 某个时间
     * @return 字符串类型的某一天的23时59分59秒时间
     */
    public static String getDayEndStr(Date date) {
        Date endTimeOfDay = getEndTimeOfDay(date);
        return getDate(endTimeOfDay, DATETIME_FORMAT_1);
    }

    /**
     * 跟{@link #getEndTimeOfDay(Date)}方法重复
     *
     * @param date 某个时间
     * @return 某一天的23时59分59秒时间
     */
    public static Date getDayEnd(Date date) {
        return getEndTimeOfDay(date);
    }

    /**
     * 将指定格式的日期字符串转换为日期对象。
     *
     * @param source 日期字符串。
     * @param pattern 模式。
     * @return Date 日期对象。
     */
    public static Date parseDate(String source, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.parse(source, new ParsePosition(0));
    }

    /**
     * 快速获取指定日期
     *
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 对应时间
     */
    public static Date getDate(Integer year, Integer month, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    /**
     * 获取当前时间字符串。
     *
     * @return 字符串类型时间
     */
    public static String getDate() {
        return getDate(new Date());
    }

    /**
     * 获取日期字符串。
     *
     * @param date 需要转化的日期。
     * @return 字符串类型时间
     */
    public static String getDate(Date date, String format) {
        if (null == date) {
            return StringUtil.EMPTY;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 使用{@link #DATE_FORMAT_1}来格式化时间
     *
     * @param date 时间
     * @return 字符串类型时间
     */
    public static String getDate(Date date) {
        return getDate(date, DATE_FORMAT_1);
    }

    /**
     * 获取日期字符串。
     *
     * <pre>
     *  日期字符串格式： yyyyMMdd
     *  其中：
     *      yyyy   表示4位年。
     *      MM     表示2位月。
     *      dd     表示2位日。
     * </pre>
     *
     * @param date
     *                需要转化的日期。
     * @return String "yyyyMMdd"格式的日期字符串。
     */
    /**
     * 将指定的日期字符串转化为日期对象
     *
     * @param dateStr 日期字符串
     * @return java.util.Date
     */
    public static Date getDate(String dateStr) {
        if (StringUtil.isEmpty(dateStr)) {
            return null;
        }
        if (DateUtil.isDate(dateStr)) {
            // 日期型
            return getDate(dateStr, DATE_FORMAT_2);
        } else if (DateUtil.isDateTime(dateStr)) {
            // 日期时间型
            return getDate(dateStr, DATETIME_FORMAT_5);
        }
        return null;
    }

    /**
     * 判断字符串是否日期格式
     *
     * @param str 字符串
     * @return true-是日期格式，false-不是日期格式
     */
    public static boolean isDate(String str) {
        return isDateTime(str, DATE_FORMAT_2);
    }

    /**
     * String类型时间按照一定格式，格式化为Date类型
     *
     * @param dateStr String类型时间
     * @param pattern 格式
     * @return Date类型
     */
    public static Date getDate(String dateStr, String pattern) {
        if (DateUtil.isDateTime(dateStr, pattern)) {
            // 日期型
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            try {
                return df.parse(dateStr);
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    /**
     * 判断字符串是否日期时间格式
     *
     * @param str 时间字符串
     * @return true-是日期格式，false-不是日期格式
     */
    public static boolean isDateTime(String str) {
        return isDateTime(str, DATETIME_FORMAT_5);
    }

    /**
     * 判断字符串是否日期时间格式
     *
     * @param str 时间字符串
     * @param pattern 格式
     * @return true-是日期格式，false-不是日期格式
     */
    public static boolean isDateTime(String str, String pattern) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (Exception e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 以{@link #TIME_FORMAT_1}格式获取时间的时分秒
     *
     * @param date 时间
     * @return 时分秒
     */
    public static String getTimeStr6(Date date) {
        return getDate(date, TIME_FORMAT_1);
    }

    /**
     * 以{@link #TIME_FORMAT_1}格式获取时间的时分秒
     *
     * @return 时分秒
     */
    public static String getTimeStr6() {
        return getDate(new Date(), TIME_FORMAT_1);
    }

    /**
     * 获取入参时间的当月1号零点零分零秒
     *
     * @param date 要计算的时间
     * @return 入参时间的当月1号零点零分零秒
     */
    public static Date getBeginDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 获取入参时间的当年1号零点零分零秒
     *
     * @param date 要计算的时间
     * @return 入参时间的当年1号零点零分零秒
     */
    public static Date getBeginDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 获取入参时间的当天23:59:59
     *
     * @param date 要计算时间
     * @return 返回当天23:59:59
     */
    public static Date getEndDateTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得这个月的第一天
     *
     * @return 这个月的第一天
     */
    public static Date getThisMonthFirstDay() {
        return getBeginDayOfMonth(new Date());
    }

    /**
     * 获取入参时间的当月最后一天
     *
     * @param date 要计算的时间
     * @return 入参时间的当月最后一天
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, maxDay);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        date.setTime(calendar.getTimeInMillis());
        return date;
    }

    /**
     * 获得这个月的最后一天
     *
     * @return 当天时间的当月最后一天
     */
    public static Date getThisMonthLastDay() {
        return getLastDayOfMonth(new Date());
    }

    /**
     * 获得这周的第一天
     *
     * @return 这周的第一天
     */
    public static Date getThisWeekFirstDay() {
        Calendar calendar = Calendar.getInstance();
        // 得到当天是这周的第几天
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // 减去dayOfWeek,得到第一天的日期，因为Calendar用０－６代表一周七天，所以要减一
        calendar.add(Calendar.DAY_OF_WEEK, -(dayOfWeek - 1));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 判断日期是否相同
     *
     * @param d1 第一个日期
     * @param d2 第二个日期
     * @return true-相同，false-不相同
     */
    public static boolean isSame(Date d1, Date d2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);
        return dateFormat.format(d1).equals(dateFormat.format(d2));
    }

    /**
     * 获取当前月度字符串。
     * <p>
     * <pre>
     *  日期字符串格式： dd
     *  其中：
     *      dd   表示4位年。
     * </pre>
     *
     * @return String "dd"格式的当前天字符串。
     */
    public static String getNowDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        return formatter.format(new Date());
    }

    /**
     * 获取当前日期和时间
     * 格式 yyyy-MM-dd HH:mm:ss.SSS
     *
     * @return String
     */
    public static String getCurrentDateStr() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_FORMAT_5);
        return formatter.format(new Date());
    }

    /**
     * 获取某一天的的下一天的0时0分0秒时间
     *
     * @param dt 时间
     * @return 某一天的下一天的0时0分0秒时间
     */
    public static Date getStartTimeOfNextDay(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 获取日期字符串。
     * <p>
     * <pre>
     *  日期字符串格式： yyyy-MM-dd
     *  其中：
     *      yyyy   表示4位年。
     *      MM     表示2位月。
     *      dd     表示2位日。
     * </pre>
     *
     * @return String "yyyy-MM-dd"格式的日期字符串。
     */
    public static String getHyphenDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_1);
        return formatter.format(new Date());
    }

    /**
     * 获取日期字符串。
     * <p>
     * <pre>
     *  日期字符串格式： yyyy-MM-dd
     *  其中：
     *      yyyy   表示4位年。
     *      MM     表示2位月。
     *      dd     表示2位日。
     * </pre>
     *
     * @param date 需要转化的日期。
     * @return String "yyyy-MM-dd"格式的日期字符串。
     */
    public static String getHyphenDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_1);

        return formatter.format(date);
    }

    @Getter
    public enum TimeType {
        /** 秒 */
        SECOND(0),
        /** 分钟 */
        MINUTE(1),
        /** 小时 */
        HOUR(2),
        /** 天 */
        DAY(3),
        ;

        private int code;

        TimeType(int code) {
            this.code = code;
        }
    }

    /**
     * 计算日期所在年份的第几周
     *
     * @param date 指定日期
     * @return int 指定日期所在年份的周数
     */
    public static int calWeekNumber(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取指定日期对应月份的最后一天
     *
     * @param date 日期
     * @return 指定日期对应月份的最后一天
     */
    public int getLastDayOfMonthByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DATE);
    }
}
