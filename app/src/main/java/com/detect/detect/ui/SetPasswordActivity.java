package com.detect.detect.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.shared_preferences.CommonSP;
import com.detect.detect.utils.StringUtils;
import com.detect.detect.utils.ToastUtils;
import com.detect.detect.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SetPasswordActivity extends BaseActivity {
    @BindView(R.id.confirm_password_et)
    EditText confirm_password_et;

    @BindView(R.id.set_password_et)
    EditText set_password_et;

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
        return R.layout.activity_set_password;
    }

    @OnClick({R.id.confirm_bt, R.id.common_back_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm_bt:
                setPassword();
                break;
            case R.id.common_back_ll:
                finish();
                break;
        }
    }

    private void setPassword() {
        String firstPassword = set_password_et.getText().toString().trim();
        String confirmPassword = confirm_password_et.getText().toString().trim();
        if (TextUtils.isEmpty(firstPassword) || TextUtils.isEmpty(confirmPassword)) {
            ToastUtils.showToast("密码不能为空");
            return;
        }
        if (!StringUtils.filterCharToNormal(firstPassword) || !StringUtils.filterCharToNormal(confirmPassword)) {
            ToastUtils.showToast("密码不能包含特殊字符");
            return;
        }
        if (!TextUtils.equals(firstPassword, confirmPassword)) {
            ToastUtils.showToast("两次密码输入不一致,请重新输入");
            return;
        }
        CommonSP.getInstance().setPassword(firstPassword);
        ToastUtils.showToast("密码保存成功");
        UIUtils.intentActivity(ModifyParameterActivity.class,null,this);
        finish();
    }

    @Override
    public void initView() {
        commonTitleTv.setText("请设置密码");
    }
}
