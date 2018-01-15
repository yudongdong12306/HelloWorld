package com.bigkoo.pickerview.view;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.R;
import com.bigkoo.pickerview.TimePickerView.Type;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.adapter.NumericWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.bigkoo.pickerview.utils.PickerViewUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class WheelTime {
    private static final String TAG = "WheelTime";
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;

    private Type type;
    private static final int DEFULT_START_YEAR = 1990;
    private static final int DEFULT_END_YEAR = 2100;
    private int startYear = DEFULT_START_YEAR;
    private int endYear = DEFULT_END_YEAR;
    private SlippingListener slippingListener;


    public WheelTime(View view) {
        super();
        this.view = view;
        type = Type.ALL;
        setView(view);
    }

    public WheelTime(View view, Type type, int startYear, int endYear) {
        super();
        this.view = view;
        this.type = type;
        this.startYear = startYear;
        this.endYear = endYear;
        setView(view);
    }

    public WheelTime(View view, Type type) {
        super();
        this.view = view;
        this.type = type;
        setView(view);
    }

    public void setPicker(int year, int month, int day) {
        this.setPicker(year, month, day, 0, 0);
    }

    /**
     * TODO 弹出日期时间选择器
     */
    public void setPicker(int year, int month, final int day, int h, int m) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        Context context = view.getContext();
        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(startYear, endYear));// 设置"年"的显示数据
        wv_year.setLabel(context.getString(R.string.pickerview_year));// 添加文字
        wv_year.setCurrentItem(year - startYear);// 初始化时显示的数据
        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);
        wv_month.setAdapter(new ArrayWheelAdapter(PickerViewUtils.getMonths()));
//        wv_month.setAdapter(new NumericWheelAdapter(1, 12));
//        wv_month.setLabel(context.getString(R.string.pickerview_month));
        wv_month.setCurrentItem(month);
        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
            else
                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
        }
        wv_day.setLabel(context.getString(R.string.pickerview_day));
        wv_day.setCurrentItem(day - 1);


        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hours.setLabel(context.getString(R.string.pickerview_hours));// 添加文字
        wv_hours.setCurrentItem(h);

        wv_mins = (WheelView) view.findViewById(R.id.min);
        wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
        wv_mins.setLabel(context.getString(R.string.pickerview_minutes));// 添加文字
        wv_mins.setCurrentItem(m);

        // 添加"年"监听
        OnItemSelectedListener wheelListener_year = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int year_num = index + startYear;
                // 判断大小月及是否闰年,用来确定"日"的数据
                int maxItem = 30;
                if (list_big
                        .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                    maxItem = 31;
                } else if (list_little.contains(String.valueOf(wv_month
                        .getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                    maxItem = 30;
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                        maxItem = 29;
                    } else {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                        maxItem = 28;
                    }
                }
                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }
                fetchCurrentTime();
            }
        };
        // 添加"月"监听
        OnItemSelectedListener wheelListener_month = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index + 1;
                int maxItem = 30;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                    maxItem = 31;
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                    maxItem = 30;
                } else {
                    if (((wv_year.getCurrentItem() + startYear) % 4 == 0 && (wv_year
                            .getCurrentItem() + startYear) % 100 != 0)
                            || (wv_year.getCurrentItem() + startYear) % 400 == 0) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                        maxItem = 29;
                    } else {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                        maxItem = 28;
                    }
                }
                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }
                fetchCurrentTime();
            }
        };
        //"日"的监听,为了监听当前所设置的日期是否大于今天的日期而设置的
        OnItemSelectedListener wheelListener_day = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                fetchCurrentTime();
            }
        };
        //"小时"的监听,为了监听当前所设置的日期是否大于今天的日期而设置的
        OnItemSelectedListener wheelListener_hour = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                fetchCurrentTime();
            }
        };
        //"分钟"的监听,为了监听当前所设置的日期是否大于今天的日期而设置的
        OnItemSelectedListener wheelListener_min = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                fetchCurrentTime();

            }
        };
        wv_year.setOnItemSelectedListener(wheelListener_year);
        wv_month.setOnItemSelectedListener(wheelListener_month);
        wv_day.setOnItemSelectedListener(wheelListener_day);
        wv_hours.setOnItemSelectedListener(wheelListener_hour);
        wv_mins.setOnItemSelectedListener(wheelListener_min);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 6;
        switch (type) {
            case ALL:
                textSize = textSize * 3;
                break;
            case YEAR_MONTH_DAY:
                textSize = textSize * 4;
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
                break;
            case HOURS_MINS:
                textSize = textSize * 4;
                wv_year.setVisibility(View.GONE);
                wv_month.setVisibility(View.GONE);
                wv_day.setVisibility(View.GONE);
                break;
            case MONTH_DAY_HOUR_MIN:
                textSize = textSize * 3;
                wv_year.setVisibility(View.GONE);
                break;
            case YEAR_MONTH:
                textSize = textSize * 4;
                wv_day.setVisibility(View.GONE);
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
        }
        if (Type.YEAR_MONTH_DAY != type) {
            wv_day.setTextSize(textSize);
            wv_month.setTextSize(textSize);
            wv_year.setTextSize(textSize);
            wv_hours.setTextSize(textSize);
            wv_mins.setTextSize(textSize);
        } else {
//            17  表示 两天线中间的字体大小  15 表示年的那一列的外边的字体大小    14 表示月的那一列的外边的字体大小  13 表示日的那一列的外边的字体大小
            wv_year.setTextSizeCenter(17);
            wv_year.setTextSizeOuter(15);
            wv_month.setTextSizeCenter(17);
            wv_month.setTextSizeOuter(14);
            wv_day.setTextSizeCenter(17);
            wv_day.setTextSizeOuter(13);
        }
    }

    /**
     * 滚轮滚动过程中获取时间
     */
    private void fetchCurrentTime() {
        try {
            Date date = dateFormat.parse(getTime());
            if (null != slippingListener) {
                slippingListener.onSlippingTime(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环滚动
     */
    public void setCyclic(boolean cyclic) {
        wv_year.setCyclic(cyclic);
        wv_month.setCyclic(cyclic);
        wv_day.setCyclic(cyclic);
        wv_hours.setCyclic(cyclic);
        wv_mins.setCyclic(cyclic);
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic, boolean yearCycle) {
        wv_year.setCyclic(yearCycle);
        wv_month.setCyclic(cyclic);
        wv_day.setCyclic(cyclic);
        wv_hours.setCyclic(cyclic);
        wv_mins.setCyclic(cyclic);
    }

    public String getTime() {
        StringBuffer sb = new StringBuffer();
        sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                .append((wv_month.getCurrentItem() + 1)).append("-")
                .append((wv_day.getCurrentItem() + 1)).append(" ")
                .append(wv_hours.getCurrentItem()).append(":")
                .append(wv_mins.getCurrentItem());
        return sb.toString();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getStartYear() {
        return startYear;
    }


    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    /**
     * 滑动过程中的监听,用于滑动超过某一个临界值时,"回滚"到,某一值
     */
    public interface SlippingListener {
        //滑动的时间
        void onSlippingTime(Date date);
    }

    /**
     * 设置滑动过程中的监听
     */
    public void setSlippingListener(SlippingListener listener) {
        this.slippingListener = listener;
    }
}
