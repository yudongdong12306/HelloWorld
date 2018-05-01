package com.detect.detect.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.shared_preferences.ProjectInfo;
import com.detect.detect.shared_preferences.ProjectNameUUIDSP;
import com.detect.detect.utils.UIUtils;
import com.gsh.dialoglibrary.RaiingAlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongdong.yu on 2018/1/7.
 */
public class ShowHistoryActivity extends BaseActivity {
    @BindView(R.id.project_name_rv)
    RecyclerView projectName_Rv;
    @BindView(R.id.new_build_bt)
    Button newBuildBt;
    @BindView(R.id.common_title_tv)
    TextView common_title_tv;
    private ProjectNamesListAdapter adapter;
    private static final String FRAGMENT_TAG_NO_DAT = "fragment_tag_no_dat";

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_show_history;
    }


    @Override
    public void initView() {
        common_title_tv.setText("历史工程");

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<ProjectInfo> allProjects = ProjectNameUUIDSP.getInstance().getAllProjectInfos();
        if (allProjects == null || allProjects.size() == 0) {
            //新建项目
            projectName_Rv.setVisibility(View.GONE);
            newBuildBt.setVisibility(View.GONE);
//            CommonDialog fragment = ((CommonDialog) getFragmentManager().findFragmentByTag(FRAGMENT_TAG_NO_DAT));
//            if (fragment == null) {
//                fragment = new CommonDialog();
//                fragment.setContent("当前没有历史工程,点击确认新建", "确认", "取消");
//            }
//            fragment.show(getFragmentManager().beginTransaction(), FRAGMENT_TAG_NO_DAT);

            new RaiingAlertDialog(this, "提示"
                    , "当前没有历史工程,点击确认新建", "确认", "取消", new RaiingAlertDialog.CallbackRaiingAlertDialog() {
                @Override
                public void onPositive() {
                    UIUtils.intentActivity(ProjectInfoActivity.class, null);
                }

                @Override
                public void onNegative() {
                    finish();
                }
            }).show();


        } else {
            newBuildBt.setVisibility(View.VISIBLE);
            projectName_Rv.setVisibility(View.VISIBLE);
            projectName_Rv.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ProjectNamesListAdapter(this, allProjects);
            projectName_Rv.setAdapter(adapter);

            DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.shape_user_info_recycle_divider));
            projectName_Rv.addItemDecoration(itemDecoration);

            adapter.setOnItemClickListener(new ProjectNamesListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, ProjectInfo project) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("TO_TEST_POINT", project);
                    UIUtils.intentActivity(NewTestPointActivity.class, bundle);
                }

                @Override
                public void onLongItemClick(View v, ProjectInfo projectInfo) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PROJECT_INFO_TO_EDIT", projectInfo);
                    UIUtils.intentActivity(ProjectInfoActivity.class, bundle);
                }
            });
        }
    }

    @OnClick({R.id.common_back_ll, R.id.new_build_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_ll:
                finish();
                break;
            case R.id.new_build_bt:
                UIUtils.intentActivity(ProjectInfoActivity.class, null);
                break;
        }
    }
//    @Override
//    public void onConfirm() {
////        ToastUtils.showToast("进入新建工程界面");
//        UIUtils.intentActivity(ProjectInfoActivity.class, null);
//    }
//
//    @Override
//    public void onCancel() {
//        finish();
//    }
}
