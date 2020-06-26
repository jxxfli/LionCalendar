# LionCalendar
a private lib of calendar

![image](https://img.shields.io/badge/LionCalendar-v1.0.2-green.svg)
[![GitHub stars](https://img.shields.io/github/stars/jxxfli/LionCalendar)](https://github.com/jxxfli/LionCalendar/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/jxxfli/LionCalendar)](https://github.com/jxxfli/LionCalendar/network)
[![GitHub issues](https://img.shields.io/github/issues/jxxfli/LionCalendar)](https://github.com/jxxfli/LionCalendar/issues)

#### 因项目需求写的库，有类似功能的可以clone下来改改😁
在主项目app的build.gradle中依赖
```gradle
dependencies {
	...
	implementation 'com.lion:LionCalendar:1.0.2'
}
```
#### 使用方法
```java
LionCalendarPopup.getInstance(context)
        .setSetelectMode(SelectMoed.MODE_MONTH)//选择模式 MODE_WEEK：周 MODE_MONTH：月 MODE_ALLDAY：所有日期
        .setSetelectDate(mSelectedDate)//设置选中的日期 格式：周&所有日期：yyyy-MM-dd  月：yyyy-MM
        .setShowAsDropDown(v)//显示在指定控件下方 .setShowAsCenter(v)//显示在屏幕中间
        .setShowYearSelect(boolean)//年选中状态显示 默认为true
        .setShowMonthSelect(boolean)//月选中状态显示 默认为true
        .setShowDaySelect(boolean)//日选中状态显示 默认为true
        .setOnCalendarClickListener(listener)//设置选中回调
        .show();
```

没有架构可言
### 支持3种选择模式：月份选择模式、周日选择模式和普通模式

<img src="https://s1.ax1x.com/2020/06/27/NyAwbd.jpg" width="300" alt="1" border="0"><img src="https://s1.ax1x.com/2020/06/27/NyADUI.jpg" width="300" alt="2" border="0"><img src="https://s1.ax1x.com/2020/06/27/NyAr5t.jpg" width="300" alt="3" border="0"><img src="https://s1.ax1x.com/2020/06/27/NyABVA.jpg" width="300" alt="4" border="0"><img src="https://s1.ax1x.com/2020/06/27/NyAdDH.jpg" width="300" alt="5" border="0">
