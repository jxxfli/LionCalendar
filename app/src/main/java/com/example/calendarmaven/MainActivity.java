package com.example.calendarmaven;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.lion.calendar.LionCalendarPopup;
import com.lion.calendar.constant.SelectMoed;
import com.lion.calendar.interf.OnLionCalendarClickListener;

public class MainActivity extends Activity implements OnLionCalendarClickListener {
    String monthDate = null;// 设置默认选中的日期  格式为 “2014-04” 标准DATE格式
    String weekDate = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式
    String dayDate = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式

    Button mMonthBt;
    Button mWeekBt;
    Button mAllDay;

    CheckBox mYear;
    CheckBox mMonth;
    CheckBox mDay;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mYear = findViewById(R.id.cbx_year);
        mMonth = findViewById(R.id.cbx_month);
        mDay = findViewById(R.id.cbx_day);

        mMonthBt = (Button) findViewById(R.id.month_bt);
        mMonthBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LionCalendarPopup.getInstance(MainActivity.this)//上下文
                        .setSetelectMode(SelectMoed.MODE_MONTH)//选择模式 MODE_WEEK：周 MODE_MONTH：月 MODE_ALLDAY：所有日期
                        .setSetelectDate(monthDate)//设置选中的日期
                        .setShowAsDropDown(v)//显示在指定控件下方 .setShowAsCenter(v)//显示在屏幕中间
                        //非必须参数
                        .setShowYearSelect(mYear.isChecked())//年选中状态显示
                        .setShowMonthSelect(mMonth.isChecked())//月选中状态显示
                        .setShowDaySelect(mDay.isChecked())//日选中状态显示
                        .setOnCalendarClickListener(MainActivity.this)//设置选中回调
                        .show();
            }
        });
        mWeekBt = findViewById(R.id.week_bt);
        mWeekBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LionCalendarPopup.getInstance(MainActivity.this)
                        .setSetelectMode(SelectMoed.MODE_WEEK)
                        .setSetelectDate(weekDate)
                        .setShowAsDropDown(v)
                        //非必须参数
                        .setShowYearSelect(mYear.isChecked())
                        .setShowMonthSelect(mMonth.isChecked())
                        .setShowDaySelect(mDay.isChecked())
                        .setOnCalendarClickListener(MainActivity.this)
                        .show();
            }
        });
        mAllDay = findViewById(R.id.allday_bt);
        mAllDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LionCalendarPopup.getInstance(MainActivity.this)
                        .setSetelectMode(SelectMoed.MODE_ALLDAY)
                        .setSetelectDate(dayDate)
                        .setShowAsCenter(v)
                        //非必须参数
                        .setShowYearSelect(mYear.isChecked())
                        .setShowMonthSelect(mMonth.isChecked())
                        .setShowDaySelect(mDay.isChecked())
                        .setOnCalendarClickListener(MainActivity.this)
                        .show();
            }
        });
    }


    @Override
    public void onMonthCalendarClick(String dateFormat) {
        monthDate = dateFormat;
        mMonthBt.setText("（月份）日历：" + dateFormat);
    }

    @Override
    public void onWeekDayCalendarClick(SelectMoed selectMoed, String dateFormat, int weekForMonth) {
        if (selectMoed == SelectMoed.MODE_WEEK) {
            weekDate = dateFormat;
            mWeekBt.setText("（周日）日历：" + dateFormat + "\t第" + weekForMonth + "周");
        }else if (selectMoed==SelectMoed.MODE_ALLDAY){
            dayDate= dateFormat;
            mAllDay.setText("日历：" + dateFormat );
        }
    }

}
