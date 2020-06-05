package com.lion.calendar;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.lion.calendar.constant.SelectMoed;
import com.lion.calendar.interf.OnLionCalendarClickListener;
import com.lion.calendar.util.DateUtil;
import com.lion.calendar.view.KCalendar;
import com.lion.calendar.view.MonthCalendar;
import com.lion.calendar.view.YearCalendar;

import java.util.Date;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.lion.calendar.constant.SelectMoed.MODE_MONTH;
import static com.lion.calendar.constant.SelectMoed.MODE_WEEK;

/**
 * 日历Popupwindow
 *
 * @Author Lino-082
 * @Date 2020/6/2 17:23
 */
public class LionCalendarPopup extends PopupWindow {

    private int mYear ;

    private static Context mContext;
    private static View mAnchorView;
    private static SelectMoed mSelectMode;
    private static String mSelectDate;
    private static boolean mShowYearSelect=true;//是否显示年已选标记
    private static boolean mShowMonthSelect=true;//是否显示月已选标记
    private static boolean mShowDaySelect=true;//是否显示日已选标记

    private boolean isDwon;

    public LionCalendarPopup() {
    }

    public static LionCalendarPopup getInstance(Context context) {
        mContext = context;
        return new LionCalendarPopup();
    }

    /**
     * 设置显示在哪个控件下
     *
     * @param anchorView
     * @return
     */
    public LionCalendarPopup setShowAsDropDown(View anchorView) {
        this.mAnchorView = anchorView;
        isDwon = true;
        return this;
    }

    /**
     * 设置显示在哪个控件上
     *
     * @param anchorView
     * @return
     */
    public LionCalendarPopup setShowAsCenter(View anchorView) {
        this.mAnchorView = anchorView;
        isDwon = false;
        return this;
    }

    public LionCalendarPopup setSetelectMode(SelectMoed selectMode) {
        this.mSelectMode = selectMode;
        return this;
    }

    public LionCalendarPopup setSetelectDate(String selectDate) {
        this.mSelectDate = selectDate;
        if (TextUtils.isEmpty(selectDate))
            mYear = DateUtil.getLocalYear();
        else
            mYear =  DateUtil.getYearForDateSmart(selectDate);
        return this;
    }

    /**
     * 是否显示已选日期标记 总开关
     * @param showSelect
     * @return
     */
    public LionCalendarPopup setShowSelect(boolean showSelect) {
        setShowYearSelect(showSelect);
        setShowMonthSelect(showSelect);
        setShowDaySelect(showSelect);
        return this;
    }

    /**
     * 是否显示年已选标记
     * @param showYearSelect
     * @return
     */
    public LionCalendarPopup setShowYearSelect(boolean showYearSelect) {
        this.mShowYearSelect = showYearSelect;
        return this;
    }

    /**
     * 是否显示月已选标记
     * @param showMonthSelect
     * @return
     */
    public LionCalendarPopup setShowMonthSelect(boolean showMonthSelect) {
        this.mShowMonthSelect = showMonthSelect;
        return this;
    }

    /**
     * 是否显示日已选标记
     * @param showDaySelect
     * @return
     */
    public LionCalendarPopup setShowDaySelect(boolean showDaySelect) {
        this.mShowDaySelect = showDaySelect;
        return this;
    }

    public static View getAnchorView() {
        return mAnchorView;
    }

    public static SelectMoed getSelectMode() {
        return mSelectMode;
    }

    public static String getSelectDate() {
        return mSelectDate == null ? "" : mSelectDate;
    }

    public void show() {

        View view = View.inflate(mContext, R.layout.popupwindow_calendar, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        if (isDwon) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                showAsDropDown(getAnchorView(), 0, 40, Gravity.CENTER_HORIZONTAL);
            } else {
                showAsDropDown(getAnchorView());
            }
        } else {
            showAtLocation(mAnchorView, Gravity.CENTER, 0, 0);
        }
        update();

        final Button popupwindow_calendar_year = (Button) view.findViewById(R.id.popupwindow_calendar_year);
        final Button popupwindow_calendar_month = (Button) view.findViewById(R.id.popupwindow_calendar_month);
        //日历
        final KCalendar calendar = (KCalendar) view.findViewById(R.id.popupwindow_calendar);
        //月历
        final MonthCalendar monthCalendar = (MonthCalendar) view.findViewById(R.id.popupwindow_month_calendar);
        //年历
        final YearCalendar yearCalendar = (YearCalendar) view.findViewById(R.id.popupwindow_year_calendar);

        //是否显示已选日期标记
        calendar.setShowDaySelect(mShowDaySelect);
        monthCalendar.setShowMonthSelect(mShowMonthSelect);
        yearCalendar.setShowYearSelect(mShowYearSelect);

        popupwindow_calendar_year.setText(calendar.getCalendarYear() + "");
        //popupwindow_calendar_month.setText(getCNMothon(calendar.getCalendarMonth()));//一月
        popupwindow_calendar_month.setText(calendar.getCalendarMonth() + "月");//1月

        if (!TextUtils.isEmpty(getSelectDate())) {

            try {
                int years = Integer.parseInt(getSelectDate().substring(0, getSelectDate().indexOf("-")));

                if (getSelectMode() == MODE_WEEK) {
                    int month = Integer.parseInt(getSelectDate().substring(getSelectDate().indexOf("-") + 1, getSelectDate().lastIndexOf("-")));

                    //popupwindow_calendar_month.setText(getCNMothon(month));//一月
                    popupwindow_calendar_month.setText(month + "月");//1月

                    calendar.showCalendar(years, month);
                    calendar.setCalendarDayBgColor(getSelectDate(), R.drawable.calendar_date_focused);
                }
                popupwindow_calendar_year.setText(years + "");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        //设置选择模式
        calendar.show(getSelectMode());
        if (getSelectMode() == MODE_MONTH) {
            calendar.setVisibility(GONE);
            monthCalendar.setVisibility(VISIBLE);
            yearCalendar.setVisibility(GONE);

            popupwindow_calendar_year.setVisibility(VISIBLE);
            popupwindow_calendar_month.setVisibility(GONE);

        } else {
            calendar.setVisibility(VISIBLE);
            monthCalendar.setVisibility(GONE);
            yearCalendar.setVisibility(GONE);

            popupwindow_calendar_year.setVisibility(VISIBLE);
            popupwindow_calendar_month.setVisibility(VISIBLE);
        }
        //设置选中的月份
        monthCalendar.showCalendarDayBgColor(DateUtil.getYearMonthForDateSmart(getSelectDate()), R.drawable.calendar_date_focused);


            /*List<String> list = new ArrayList<String>(); //设置标记列表
            list.add("2020-05-01");
            list.add("2020-05-02");
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
                    calendar.setCalendarDayBgColor(dateFormat, R.drawable.calendar_date_focused);

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

                if (getSelectMode() == MODE_MONTH) {
                    String date = year + "-" + month;//最后返回给全局 date
                    monthCalendar.showCalendarDayBgColor(date, R.drawable.calendar_date_focused);

                    mYear = year;
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
            public void onCalendarDateChanged(String yearRange) {
                popupwindow_calendar_year.setText(yearRange);
            }
        });


        //上月监听按钮
        ImageButton popupwindow_calendar_last_month = (ImageButton) view.findViewById(R.id.popupwindow_calendar_last_month);
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
        ImageButton popupwindow_calendar_next_month = (ImageButton) view.findViewById(R.id.popupwindow_calendar_next_month);
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
                    yearCalendar.showCalendarDayBgColor(mYear,R.drawable.calendar_date_focused);
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


    private OnLionCalendarClickListener onCalendarClickListener; //日历点击回调

    /***********************************************
     *
     * get/set methods
     *
     **********************************************/


    private OnLionCalendarClickListener getOnCalendarClickListener() {
        return onCalendarClickListener;
    }

    public LionCalendarPopup setOnCalendarClickListener(OnLionCalendarClickListener onCalendarClickListener) {
        this.onCalendarClickListener = onCalendarClickListener;
        return this;
    }

}
