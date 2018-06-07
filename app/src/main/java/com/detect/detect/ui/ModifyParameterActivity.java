package com.detect.detect.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.shared_preferences.CommonSP;
import com.detect.detect.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyParameterActivity extends BaseActivity {
    @BindView(R.id.params_1_et)
    EditText params1Et;
    @BindView(R.id.params_2_et)
    EditText params2Et;
    @BindView(R.id.common_back_ll)
    LinearLayout commonBackLl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_parameter;
    }

    @Override
    public void initView() {
        commonTitleTv.setText("修改曲线参数");
    }

    @OnClick({R.id.confirm_bt, R.id.common_back_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm_bt:
                modify();
                break;
            case R.id.common_back_ll:
                finish();
                break;
        }
    }

    private void modify() {
        String params1 = params1Et.getText().toString().trim();
        String params2 = params2Et.getText().toString().trim();
        if (TextUtils.isEmpty(params1) || TextUtils.isEmpty(params2)) {
            ToastUtils.showToast("参数不能为空");
            return;
        }
        String paramsStr = params1.concat("_").concat(params2);
        CommonSP.getInstance().setLineParams(paramsStr);
        ToastUtils.showToast("参数保存成功");
        finish();
    }
}
