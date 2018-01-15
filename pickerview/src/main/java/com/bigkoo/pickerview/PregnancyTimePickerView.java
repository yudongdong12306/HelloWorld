package com.bigkoo.pickerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.SwitchButton;

import java.util.ArrayList;

/**
 * 单个两个滚动选择
 * Created by longfei.zhang on 2016/1/13.
 */
public class PregnancyTimePickerView extends BasePickerView implements View.OnClickListener {

    private static final String TAG = "SingleDoublePickerView";
    private Context mContext;
    private ArrayWheelAdapter mAdapter1;
    private ArrayWheelAdapter mAdapter2;
    private ArrayWheelAdapter mAdapter3;
    private ArrayWheelAdapter mAdapter4;
    private WheelView mWv1;
    private WheelView mWv2;
    private WheelView mWv3;
    private WheelView mWv4;
    private View mCancelBtn;

    private ArrayList<Integer> list1 = new ArrayList<>();
    private ArrayList<String> list2 = new ArrayList<>();
    private ArrayList<Integer> list3 = new ArrayList<>();
    private ArrayList<String> list4 = new ArrayList<>();
    private OnCustomSelectListener mListener;
    /**
     * 默认单行1,两行2
     */
    private int mRowNum = 4;

    /**
     * 选择框标题
     */
    private String mTitle;
    /**
     * 日期选择框滚轴，年
     */
    private String mYear;
    /**
     * 日期选择框滚轴，月
     */
    private String mMonth;

    /**
     * 好孕墙模块用到的时间选择框
     *
     * @param context 上下文
     * @param title   标题本地化字符串
     * @param year    年选择本地化字符串
     * @param month   月本地化字符串
     */
    public PregnancyTimePickerView(Context context, @NonNull String title, @NonNull String year, @NonNull String month) {
        super(context);
        this.mContext = context;
        if (TextUtils.isEmpty(title)) {
            return;
        }
        this.mTitle = title;
        this.mYear = year;
        this.mMonth = month;
        mRowNum = 4;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.pickerview_preganacy_time, contentContainer);
        mWv1 = (WheelView) findViewById(R.id.picker_singledouble_wv1);
        mWv2 = (WheelView) findViewById(R.id.picker_singledouble_wv2);
        mWv3 = (WheelView) findViewById(R.id.picker_singledouble_wv3);
        mWv4 = (WheelView) findViewById(R.id.picker_singledouble_wv4);
        SwitchButton mSb = (SwitchButton) findViewById(R.id.topbar_switch_btn);
        // -----确定和取消按钮;
        View mConfirmBtn = findViewById(R.id.topbar_confirm_btn);
        mCancelBtn = findViewById(R.id.topbar_cancel_btn);
        mConfirmBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        //顶部标题
        TextView mTitleTv = (TextView) findViewById(R.id.topbar_title_tv);
        mTitleTv.setText(mTitle);
        //英文状态下部分标题过长的适配
        if (mTitleTv.length() > 10) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            //文案过长
            //Log.e(TAG, "文案长了");
//            mTitleTv.setTextSize(18);
            layoutParams.setMargins(25, 0, 0, 0);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            mTitleTv.setLayoutParams(layoutParams);
        }


        mSb.setVisibility(View.GONE);

        mWv1.setTextSizeCenter(Constant.TEXT_SIZE);
        mWv1.setTextSizeOuter(Constant.TEXT_SIZE);
        mWv1.setCyclic(false);
        mWv2.setTextSizeCenter(Constant.TEXT_SIZE);
        mWv2.setTextSizeOuter(Constant.TEXT_SIZE);
        mWv2.setCyclic(false);
        mWv3.setTextSizeCenter(Constant.TEXT_SIZE);
        mWv3.setTextSizeOuter(Constant.TEXT_SIZE);
        mWv3.setCyclic(false);
        mWv4.setTextSizeCenter(Constant.TEXT_SIZE);
        mWv4.setTextSizeOuter(Constant.TEXT_SIZE);
        mWv4.setCyclic(false);
        initData();
    }

    /**
     * 初始化填充集合数据
     */
    private void initData() {
        list1.clear();
        list2.clear();
        list3.clear();
        list4.clear();
        //1- 24 表示备孕的时间范围
        for (int i = 0; i <= 10; i++) {
            list1.add(i);
        }
        for (int i = 0; i <= 11; i++) {
            list3.add(i);
        }
        list2.add(mYear);
        list4.add(mMonth);

        mAdapter1 = new ArrayWheelAdapter<>(list1);
        mAdapter2 = new ArrayWheelAdapter<>(list2);
        mAdapter3 = new ArrayWheelAdapter<>(list3);
        mAdapter4 = new ArrayWheelAdapter<>(list4);
        mWv1.setAdapter(mAdapter1);
        mWv2.setAdapter(mAdapter2);
        mWv3.setAdapter(mAdapter3);
        mWv4.setAdapter(mAdapter4);
    }

    /**
     * @param object1 第一列要选择的值
     * @param object3 第三列要选择的值
     */
    public void setSelectItem(Object object1, Object object3) {
        if (object1 == null || object3 == null) {
            RaiingPickerLog.d(TAG, "设置的值不能为空");
            return;
        }
        int position1 = mAdapter1.indexOf(object1);
        int position3 = mAdapter2.indexOf(object3);
        mWv1.setCurrentItem(position1);
        mWv3.setCurrentItem(position3);
    }

    /**
     * @param position1 第一列设定选择的位置
     * @param position3 第二列设定选择的位置
     */
    public void setSelectItem(int position1, int position3) {
        if (position1 < 0 || position3 < 0) {
            RaiingPickerLog.d(TAG, "选择的位置不能为负");
            return;
        }
        mWv1.setCurrentItem(position1);
        mWv3.setCurrentItem(position3);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.topbar_cancel_btn) {
            dismiss();
        } else if (view.getId() == R.id.topbar_confirm_btn) {
            if (mListener != null) {
                switch (mRowNum) {
                    case 4:
                        int position1 = mWv1.getCurrentItem();
                        int position3 = mWv3.getCurrentItem();
                        RaiingPickerLog.d(TAG, "选择的position：" + position1 + ", " + position3);
                        mListener.onDoubleSelect(mAdapter1.getItem(position1), position1,
                                mAdapter3.getItem(position3), position3);
                        break;
                    default:
                        break;
                }
            }
            dismiss();
        }
    }

    public void setOnSelectListener(OnCustomSelectListener mListener) {
        this.mListener = mListener;
    }

    public interface OnCustomSelectListener {

        void onDoubleSelect(Object object1, int position1, Object object3, int position3);
    }

    public void hideCancel() {
        mCancelBtn.setVisibility(View.GONE);
    }
}
