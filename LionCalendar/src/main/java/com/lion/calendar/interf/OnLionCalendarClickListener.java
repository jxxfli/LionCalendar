package com.lion.calendar.interf;

/**
 * 日记点击事件
 * @Author Lino-082
 * @Date 2020/6/2 19:25
 */
public interface OnLionCalendarClickListener {
    void onMonthCalendarClick(String dateFormat);

    void onWeekCalendarClick(String dateFormat, int weekForMonth);
}
