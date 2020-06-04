package com.example.calendarmaven;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.lion.calendar.util.DateUtil;
import com.lion.calendar.view.KCalendar;
import com.lion.calendar.view.MonthCalendar;
import com.lion.calendar.view.YearCalendar;

import java.util.Date;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 日历Popupwindow
 *
 * @Author Lino-082
 * @Date 2020/6/2 17:23
 */
public class CalendarPopup extends PopupWindow {
    public static final int MODE_MONTH = 0;//整个月都可以选择
    public static final int MODE_WEEK = 1;//只能选择周日


    private int mYear = new Date().getYear() + 1900;

    private static Context mContext;
    private static View mParent;

    public CalendarPopup(Context mContext, View parent, int selectMode) {
        this(mContext, parent, selectMode, null);
    }

    public CalendarPopup(Context mContext, View parent, final int selectMode, String selectDate) {


        View view = View.inflate(mContext, com.lion.calendar.R.layout.popupwindow_calendar, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, com.lion.calendar.R.anim.fade_in));
        //            LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        //            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
        //                    R.anim.push_bottom_in_1));

        //            LinearLayout calendarParentLayout =view.findViewById(R.id.popupwindow_calendar_parent_layout);
        //            setWeekView(calendarParentLayout);


        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        //showAtLocation(parent, Gravity.TOP, 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            showAsDropDown(parent, 0, 40, Gravity.CENTER_HORIZONTAL);
        } else {
            showAsDropDown(parent);
        }
        update();

        final Button popupwindow_calendar_year = (Button) view.findViewById(com.lion.calendar.R.id.popupwindow_calendar_year);
        final Button popupwindow_calendar_month = (Button) view.findViewById(com.lion.calendar.R.id.popupwindow_calendar_month);
        //日历
        final KCalendar calendar = (KCalendar) view.findViewById(com.lion.calendar.R.id.popupwindow_calendar);
        //月历
        final MonthCalendar monthCalendar = (MonthCalendar) view.findViewById(com.lion.calendar.R.id.popupwindow_month_calendar);
        //年历
        final YearCalendar yearCalendar = (YearCalendar) view.findViewById(com.lion.calendar.R.id.popupwindow_year_calendar);


        popupwindow_calendar_year.setText(calendar.getCalendarYear() + "");
        //popupwindow_calendar_month.setText(getCNMothon(calendar.getCalendarMonth()));//一月
        popupwindow_calendar_month.setText(calendar.getCalendarMonth() + "月");//1月

        if (!TextUtils.isEmpty(selectDate)) {

            int years = Integer.parseInt(selectDate.substring(0, selectDate.indexOf("-")));

            if (selectMode == MODE_WEEK) {
                int month = Integer.parseInt(selectDate.substring(selectDate.indexOf("-") + 1, selectDate.lastIndexOf("-")));

                //popupwindow_calendar_month.setText(getCNMothon(month));//一月
                popupwindow_calendar_month.setText(month + "月");//1月

                calendar.showCalendar(years, month);
                calendar.setCalendarDayBgColor(selectDate, com.lion.calendar.R.drawable.calendar_date_focused);
            }
            popupwindow_calendar_year.setText(years + "");
        }

        //设置选择模式
//        calendar.show(selectMode);
        if (selectMode == MODE_MONTH) {
            calendar.setVisibility(GONE);
            monthCalendar.setVisibility(VISIBLE);
            yearCalendar.setVisibility(GONE);

            popupwindow_calendar_year.setVisibility(VISIBLE);
            popupwindow_calendar_month.setVisibility(GONE);

            monthCalendar.setCalendarDayBgColor(selectDate, com.lion.calendar.R.drawable.calendar_date_focused);
        } else {
            calendar.setVisibility(VISIBLE);
            monthCalendar.setVisibility(GONE);
            yearCalendar.setVisibility(GONE);

            popupwindow_calendar_year.setVisibility(VISIBLE);
            popupwindow_calendar_month.setVisibility(VISIBLE);
        }


            /*List<String> list = new ArrayList<String>(); //设置标记列表
            list.add("2014-04-01");
            list.add("2014-04-02");
            calendar.addMarks(list, 0);*/

        //监听所选中的日期
        calendar.setOnCalendarClickListener(new KCalendar.OnCalendarClickListener() {

            public void onCalendarClick(int row, int col, String dateFormat, int weekForMonth) {
                int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-")));

                if (calendar.getCalendarMonth() - month == 1//跨年跳转
                        || calendar.getCalendarMonth() - month == -11) {
                    calendar.lastMonth();

                } else if (month - calendar.getCalendarMonth() == 1 //跨年跳转
                        || month - calendar.getCalendarMonth() == -11) {
                    calendar.nextMonth();

                } else {
                    calendar.removeAllBgColor();
                    calendar.setCalendarDayBgColor(dateFormat, com.lion.calendar.R.drawable.calendar_date_focused);

                    //                    if (calendar.getSelectMode() == MODE_MONTH) {
                    //                        date = dateFormat;//最后返回给全局 date
                    //                       // mMonthBt.setText("（月）日期：" + dateFormat);
                    //                    } else {
                    //                        dateWeek = dateFormat;//最后返回给全局 date
                    //                        //mWeekBt.setText("（周）日期：" + dateFormat + "\t第" + weekForMonth + "周");
                    //                        ToastUtil.showShortToast("（周）日期：" + dateFormat + "\t第" + weekForMonth + "周");
                    //                    }

                    mYear = DateUtil.getYearForDate(dateFormat);

                    if (getOnCalendarClickListener() != null) {
                        getOnCalendarClickListener().onWeekCalendarClick(dateFormat, weekForMonth);
                    }

                    dismiss();
                }
            }
        });

        //监听当前月份
        calendar.setOnCalendarDateChangedListener(new KCalendar.OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                mYear = year;
                monthCalendar.setCandarYear(year);
                popupwindow_calendar_year.setText(year + "");
                //popupwindow_calendar_month.setText(getCNMothon(month));//一月
                popupwindow_calendar_month.setText(month + "月");//1月
            }
        });

        //月历点击事件监听
        monthCalendar.setOnCalendarClickListener(new MonthCalendar.OnCalendarClickListener() {
            @Override
            public void onCalendarClick(int row, int col, int year, int month) {

                if (selectMode == MODE_MONTH) {
                    //mMonthBt.setText("（月）日期：" + year + "-" + month);
                    String date = year + "-" + month;//最后返回给全局 date
                    monthCalendar.removeAllBgColor();
                    monthCalendar.setCalendarDayBgColor(date, com.lion.calendar.R.drawable.calendar_date_focused);
                    dismiss();

                    if (getOnCalendarClickListener() != null) {
                        getOnCalendarClickListener().onMonthCalendarClick(date);
                    }
                } else {
                    monthCalendar.setVisibility(GONE);
                    calendar.setVisibility(VISIBLE);
                    calendar.showCalendar(year, month);
                    popupwindow_calendar_year.setVisibility(View.VISIBLE);
                    popupwindow_calendar_month.setVisibility(View.VISIBLE);
                    //popupwindow_calendar_month.setText(getCNMothon(month));//一月
                    popupwindow_calendar_month.setText(month + "月");//1月
                    popupwindow_calendar_year.setText(year + "");
                }
            }
        });

        //监听月历改变（翻页）事件
        monthCalendar.setOnCalendarDateChangedListener(new MonthCalendar.OnCalendarDateChangedListener() {
            @Override
            public void onCalendarDateChanged(int year) {
                popupwindow_calendar_year.setText(year + "");
            }
        });


        //年历点击事件监听
        yearCalendar.setOnCalendarClickListener(new YearCalendar.OnCalendarClickListener() {
            @Override
            public void onCalendarClick(int row, int col, int year) {
                monthCalendar.setVisibility(VISIBLE);
                yearCalendar.setVisibility(GONE);
                calendar.setVisibility(GONE);
                popupwindow_calendar_year.setVisibility(View.VISIBLE);
                popupwindow_calendar_month.setVisibility(GONE);
                popupwindow_calendar_year.setText(year + "");
                monthCalendar.show(year);
            }
        });

        //监听年历改变（翻页）事件
        yearCalendar.setOnCalendarDateChangedListener(new YearCalendar.OnCalendarDateChangedListener() {
            @Override
            public void onCalendarDateChanged(int year) {
                popupwindow_calendar_year.setText(yearCalendar.getYearAange());
            }
        });


        //上月监听按钮
        ImageButton popupwindow_calendar_last_month = (ImageButton) view.findViewById(com.lion.calendar.R.id.popupwindow_calendar_last_month);
        popupwindow_calendar_last_month.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (calendar.getVisibility() == VISIBLE)
                    //日历
                    calendar.lastMonth();
                else if (monthCalendar.getVisibility() == VISIBLE)
                    //月历
                    monthCalendar.lastMonthCalendar();
                else
                    //年历
                    yearCalendar.lastYearCalendar();
            }

        });

        //下月监听按钮
        ImageButton popupwindow_calendar_next_month = (ImageButton) view.findViewById(com.lion.calendar.R.id.popupwindow_calendar_next_month);
        popupwindow_calendar_next_month.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (calendar.getVisibility() == VISIBLE)
                    //日历
                    calendar.nextMonth();
                else if (monthCalendar.getVisibility() == VISIBLE)
                    //月历
                    monthCalendar.nextMonthCalendar();
                else
                    //年历
                    yearCalendar.nextYearCalendar();
            }
        });

        //年按钮监听
        popupwindow_calendar_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (yearCalendar.getVisibility() == GONE) {
                    yearCalendar.showCalendar(mYear);
                    yearCalendar.setVisibility(View.VISIBLE);
                    calendar.setVisibility(GONE);
                    monthCalendar.setVisibility(GONE);

                    popupwindow_calendar_year.setVisibility(View.VISIBLE);
                    popupwindow_calendar_month.setVisibility(GONE);
                    popupwindow_calendar_year.setText(yearCalendar.getYearAange());
                }
            }
        });

        //月按钮监听
        popupwindow_calendar_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthCalendar.setVisibility(View.VISIBLE);
                calendar.setVisibility(GONE);
                yearCalendar.setVisibility(GONE);

                popupwindow_calendar_year.setVisibility(View.VISIBLE);
                popupwindow_calendar_month.setVisibility(GONE);
                monthCalendar.show(mYear);
            }
        });

    }


    private OnCalendarClickListener onCalendarClickListener; //日历点击回调
    //    private OnCalendarDateChangedListener onCalendarDateChangedListener; // 日历翻页回调

    /**
     * onClick接口回调
     */
    public interface OnCalendarClickListener {
        void onMonthCalendarClick(String dateFormat);

        void onWeekCalendarClick(String dateFormat, int weekForMonth);
    }

    //    /**
    //     * ondateChange接口回调
    //     */
    //    public interface OnCalendarDateChangedListener {
    //        void onCalendarDateChanged(int year);
    //    }

    /***********************************************
     *
     * get/set methods
     *
     **********************************************/


    private OnCalendarClickListener getOnCalendarClickListener() {
        return onCalendarClickListener;
    }

    public void setOnCalendarClickListener(OnCalendarClickListener onCalendarClickListener) {
        this.onCalendarClickListener = onCalendarClickListener;
    }

    //    public OnCalendarDateChangedListener getOnCalendarDateChangedListener() {
    //        return onCalendarDateChangedListener;
    //    }
    //
    //    public void setOnCalendarDateChangedListener(OnCalendarDateChangedListener onCalendarDateChangedListener) {
    //        this.onCalendarDateChangedListener = onCalendarDateChangedListener;
    //    }
}
