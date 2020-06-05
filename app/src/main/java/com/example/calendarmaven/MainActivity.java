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

    Button mMonthBt;
    Button mWeekBt;

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
                LionCalendarPopup.getInstance(MainActivity.this)
                        .setSetelectMode(SelectMoed.MODE_MONTH)
                        .setSetelectDate(monthDate)
                        .setShowAsDropDown(v)
                        //非必须参数
                        .setShowYearSelect(mYear.isChecked())
                        .setShowMonthSelect(mMonth.isChecked())
                        .setShowDaySelect(mDay.isChecked())
                        .setOnCalendarClickListener(MainActivity.this)
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
        mMonthBt.setText("（月）日期：" + dateFormat);
    }

    @Override
    public void onWeekCalendarClick(String dateFormat, int weekForMonth) {
        weekDate = dateFormat;
        mWeekBt.setText("（周）日期：" + dateFormat + "\t第" + weekForMonth + "周");
    }

}
