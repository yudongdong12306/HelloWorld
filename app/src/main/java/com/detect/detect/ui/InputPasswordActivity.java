package com.detect.detect.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.shared_preferences.CommonSP;
import com.detect.detect.utils.ToastUtils;
import com.detect.detect.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class InputPasswordActivity extends BaseActivity {
    @BindView(R.id.input_password_et)
    EditText input_password_et;
    @BindView(R.id.confirm_bt)
    Button confirm_bt;

    @BindView(R.id.common_back_ll)
    LinearLayout commonBackLl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_input_password;
    }

    @Override
    public void initView() {
        commonTitleTv.setText("请输入密码");
    }

    @OnClick({R.id.confirm_bt, R.id.common_back_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm_bt:
                inputPassword();
                break;
            case R.id.common_back_ll:
                finish();
                break;
        }
    }

    private void inputPassword() {
        String password = input_password_et.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showToast("输入密码为空");
            return;
        }
        if (!TextUtils.equals(CommonSP.getInstance().getPassword(),password)) {
            ToastUtils.showToast("密码输入错误");
            return;
        }
        UIUtils.intentActivity(ModifyParameterActivity.class,null,this);
        finish();
    }
}
