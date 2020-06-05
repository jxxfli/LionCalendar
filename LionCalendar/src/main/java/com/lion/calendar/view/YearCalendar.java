package com.lion.calendar.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lion.calendar.LionCalendarPopup;
import com.lion.calendar.R;
import com.lion.calendar.util.DisplayUtil;
import com.lion.calendar.util.ToastUtil;


import java.util.HashMap;
import java.util.Map;

import static com.lion.calendar.util.DateUtil.getLocalYear;

/**
 * 年历控件
 *
 * @author Lion-082
 */
@SuppressWarnings("deprecation")
public class YearCalendar extends ViewFlipper implements GestureDetector.OnGestureListener {
    public static final int COLOR_TX_NORMAL_YEAR = Color.parseColor("#fa564b4b"); // 当前年历数字颜色
    public static final int COLOR_BG_CALENDAR = Color.parseColor("#ffffffff"); // 年历背景色
    public static final int COLOR_TX_CANT_SELECT_YEAR = Color.parseColor("#ffcccccc"); // 不可选数字颜色

    private GestureDetector gd; // 手势监听器
    private Animation push_left_in; // 动画-左进
    private Animation push_left_out; // 动画-左出
    private Animation push_right_in; // 动画-右进
    private Animation push_right_out; // 动画-右出


    private int MONTH_ROWS_TOTAL = 4; // 年历的行数
    private int MONTH_COLS_TOTAL = 4; // 年历的列数
    private String[][] yearDates = new String[4][4]; // 当前年历年份


    private OnCalendarClickListener onCalendarClickListener; //年历点击回调
    private OnCalendarDateChangedListener onCalendarDateChangedListener; // 年历翻页回调

    private int calendarYear; // 选中的年份
    private int startYear; // 年历开始的年份

    private LinearLayout firstCalendar; // 第一个年历
    private LinearLayout secondCalendar; // 第二个年历
    private LinearLayout currentCalendar; // 当前显示的年历

    private Map<String, Integer> dayBgColorMap = new HashMap<String, Integer>(); // 储存某个日子的背景色

    private static boolean mShowYearSelect;//是否显示已选年份标记

    private static Context context;

    public YearCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public YearCalendar(Context context) {
        this(context, null);
    }

    private void init() {
        setBackgroundColor(COLOR_BG_CALENDAR);
        // 实例化手势监听器
        gd = new GestureDetector(this);
        // 初始化日历翻动动画
        push_left_in = AnimationUtils.loadAnimation(getContext(), R.anim.push_left_in);
        push_left_out = AnimationUtils.loadAnimation(getContext(), R.anim.push_left_out);
        push_right_in = AnimationUtils.loadAnimation(getContext(), R.anim.push_right_in);
        push_right_out = AnimationUtils.loadAnimation(getContext(), R.anim.push_right_out);
        push_left_in.setDuration(400);
        push_left_out.setDuration(400);
        push_right_in.setDuration(400);
        push_right_out.setDuration(400);
        // 初始化第一个年历
        firstCalendar = new LinearLayout(getContext());
        firstCalendar.setOrientation(LinearLayout.VERTICAL);
        firstCalendar.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        // 初始化第二个年历
        secondCalendar = new LinearLayout(getContext());
        secondCalendar.setOrientation(LinearLayout.VERTICAL);
        secondCalendar.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));

        // 设置默认年历为第一个年历
        currentCalendar = firstCalendar;
        // 加入ViewFlipper
        addView(firstCalendar);
        addView(secondCalendar);
        // 绘制线条框架
        drawYearFrame(firstCalendar);
        drawYearFrame(secondCalendar);

        initCalendarYear();

    }

    /**
     * 根据具体的某年，展示一个范围年历
     *
     * @param year
     */
    public void showCalendar(int year) {
        this.calendarYear = year;
        //计算规则 最小年份限制为1970年
        int s = year - 1970;
        double x = (s / 16);
        double m = 16 * x;
        double n = 1970 + m;
        startYear = (int) n;
        setCalendarDate();
    }

    /**
     * 绘制年份布局
     *
     * @param content
     */
    private void drawYearFrame(LinearLayout content) {

        // 添加年份TextView
        for (int i = 0; i < MONTH_ROWS_TOTAL; i++) {
            LinearLayout row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
            content.addView(row);
            // 绘制年历上的列
            for (int j = 0; j < MONTH_COLS_TOTAL; j++) {
                RelativeLayout col = new RelativeLayout(getContext());
                col.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
                //col.setBackgroundResource(R.drawable.calendar_day_bg);
                row.addView(col);

//                TextView view = new TextView(getContext());
//                view.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
//                //view.setBackgroundResource(R.drawable.calendar_day_bg);
//                row.addView(view);
//
//                view.setGravity(Gravity.CENTER);
//                view.setTextSize(15);
//                view.setBackgroundColor(Color.TRANSPARENT);

                // 给每一个年份加上监听
                final int finalI = i;
                final int finalJ = j;
                col.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewGroup parent = (ViewGroup) v.getParent();
                        int row = 0, col = 0;

                        // 获取列坐标
                        for (int i = 0; i < parent.getChildCount(); i++) {
                            if (v.equals(parent.getChildAt(i))) {
                                col = i;
                                break;
                            }
                        }
                        // 获取行坐标
                        ViewGroup pparent = (ViewGroup) parent.getParent();
                        for (int i = 0; i < pparent.getChildCount(); i++) {
                            if (parent.equals(pparent.getChildAt(i))) {
                                row = i;
                                break;
                            }
                        }

                        if (onCalendarClickListener != null) {
                            if (Integer.parseInt(yearDates[finalI][finalJ]) <= getLocalYear()) {
                                onCalendarClickListener.onCalendarClick(row, col, Integer.parseInt(yearDates[finalI][finalJ]));
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * 填充年历
     */
    private void setCalendarDate() {
        // 根据循环会自动++
        int year = startYear;

        // 填充年份TextView
        for (int i = 0; i < MONTH_ROWS_TOTAL; i++) {
            // 年历上的列
            for (int j = 0; j < MONTH_COLS_TOTAL; j++) {
                RelativeLayout group = getYearView(i, j);
                group.setGravity(Gravity.CENTER);
                TextView view = null;
                if (group.getChildCount() > 0) {
                    view = (TextView) group.getChildAt(0);
                } else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(getContext(), 40), DisplayUtil.dip2px(getContext(), 40));
                    view = new TextView(getContext());
                    view.setLayoutParams(params);
                    view.setGravity(Gravity.CENTER);
                    view.setTextSize(15);
                    view.setBackgroundColor(Color.TRANSPARENT);
                    group.addView(view);
                }

//                TextView view = getYearView(i, j);

                yearDates[i][j] = year + "";
                view.setText(yearDates[i][j]);
                if (year <= getLocalYear()) {
                    view.setTextColor(COLOR_TX_NORMAL_YEAR);
                } else {
                    view.setTextColor(COLOR_TX_CANT_SELECT_YEAR);
                }

                if (mShowYearSelect)
                    // 设置选中日期背景色
                    if (dayBgColorMap.get(yearDates[i][j]) != null) {
                        view.setTextColor(Color.WHITE);
                        view.setBackgroundResource(dayBgColorMap.get(yearDates[i][j]));
                    } else {
                        view.setBackgroundColor(Color.TRANSPARENT);
                    }
                year++;
            }
        }
    }


    /**
     * 设置日历具体某个日期的背景色
     *
     * @param year
     * @param color
     */
    public void showCalendarDayBgColor(int year, int color) {
        removeAllBgColor();
        dayBgColorMap.put(year + "", color);
        showCalendar(year);
    }

    /**
     * 移除日历所有日期的背景色
     */
    public void removeAllBgColor() {
        dayBgColorMap.clear();
//        if (calendarYear == 0)
//            initCalendarYear();
//        showCalendar(calendarYear);
    }


    /**
     * onClick接口回调
     */
    public interface OnCalendarClickListener {
        void onCalendarClick(int row, int col, int year);
    }

    /**
     * ondateChange接口回调
     */
    public interface OnCalendarDateChangedListener {
        void onCalendarDateChanged(String yearRange);
    }


    /**
     * 下一个年历
     */
    public synchronized void nextYearCalendar() {
        if (startYear + 16 > getLocalYear()) {
            ToastUtil.showShortToast(context, "已是最大年份");
            return;
        }
        // 改变年历上下顺序
        if (currentCalendar == firstCalendar) {
            currentCalendar = secondCalendar;
        } else {
            currentCalendar = firstCalendar;
        }
        // 设置动画
        setInAnimation(push_left_in);
        setOutAnimation(push_left_out);
        // 改变年历年份
        startYear = startYear + 16;
        //填充年历
        setCalendarDate();
        // 下翻到下一个年历
        showNext();
        // 回调
        if (onCalendarDateChangedListener != null) {
            onCalendarDateChangedListener.onCalendarDateChanged(getYearAange());
        }
    }

    /**
     * 上一个年历
     */
    public synchronized void lastYearCalendar() {
        if (startYear - 16 < 1970) {
            ToastUtil.showShortToast(context, "已是最小年份");
            return;
        }
        if (currentCalendar == firstCalendar) {
            currentCalendar = secondCalendar;
        } else {
            currentCalendar = firstCalendar;
        }
        setInAnimation(push_right_in);
        setOutAnimation(push_right_out);
        startYear = startYear - 16;
        //填充年历
        setCalendarDate();
        showPrevious();
        if (onCalendarDateChangedListener != null) {
            onCalendarDateChangedListener.onCalendarDateChanged(getYearAange());
        }
    }

    /**
     * 根据行列号获得对应的view
     *
     * @param row
     * @param col
     * @return
     */
    private RelativeLayout getYearView(int row, int col) {
        return (RelativeLayout) ((LinearLayout) currentCalendar.getChildAt(row)).getChildAt(col);
    }

    private TextView getYearTextView(int row, int col) {
        return (TextView) ((LinearLayout) currentCalendar.getChildAt(row)).getChildAt(col);
    }

    /**
     * 初始化当前年份
     */
    public void initCalendarYear() {
        calendarYear = getLocalYear();
    }

    public void setCandarYear(int calendarYear) {
        this.calendarYear = calendarYear;
    }

    public String getYearAange() {
        return startYear + " - " + (startYear + 15);
    }


    /**
     * 是否显示已选年份标记
     *
     * @param showYearSelect
     * @return
     */
    public void setShowYearSelect(boolean showYearSelect) {
        this.mShowYearSelect = showYearSelect;
    }

    /***********************************************
     *
     * Override methods
     *
     **********************************************/
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (gd != null) {
            if (gd.onTouchEvent(ev))
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.gd.onTouchEvent(event);
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // 向左/上滑动
        if (e1.getX() - e2.getX() > 20) {
            nextYearCalendar();
        }
        // 向右/下滑动
        else if (e1.getX() - e2.getX() < -20) {
            lastYearCalendar();
        }
        return false;
    }

    /***********************************************
     *
     * get/set methods
     *
     **********************************************/

    public OnCalendarClickListener getOnCalendarClickListener() {
        return onCalendarClickListener;
    }

    public void setOnCalendarClickListener(OnCalendarClickListener onCalendarClickListener) {
        this.onCalendarClickListener = onCalendarClickListener;
    }

    public OnCalendarDateChangedListener getOnCalendarDateChangedListener() {
        return onCalendarDateChangedListener;
    }

    public void setOnCalendarDateChangedListener(OnCalendarDateChangedListener onCalendarDateChangedListener) {
        this.onCalendarDateChangedListener = onCalendarDateChangedListener;
    }

}