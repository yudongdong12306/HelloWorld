package com.detect.detect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.shared_preferences.ProjectInfo;
import com.detect.detect.shared_preferences.ProjectNameUUIDSP;
import com.detect.detect.utils.ToastUtils;
import com.detect.detect.utils.UIUtils;
import com.gsh.dialoglibrary.RaiingAlertDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yu on 18/1/13.
 */

public class ProjectInfoActivity extends BaseActivity implements ProjectNameSelectDialog.ConfirmClickCallback {
    private static final String FRAGMENT_TAG_SELSTE = "FRAGMENT_TAG_SELECT";
    public static final String INSERT_TEST_POINT = "insert_test_point";
    //    public static final String NEW_BUILD_TEST_POINT = "new_build_test_point";
    public static final int RESULT_CODE_START_DETECT_INSERT = 101;
    //    public static final int RESULT_CODE_START_DETECT_NEW_BUILD = 102;
    @BindView(R.id.common_back_ll)
    LinearLayout commonBackLl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.project_name_et)
    EditText projectNameEt;
    //    @BindView(R.id.project_name_sp)
//    Spinner projectNameSp;
    @BindView(R.id.construction_organization_tv)
    EditText constructionOrganizationTv;
    @BindView(R.id.filler_type_et)
    EditText fillerTypeEt;
    @BindView(R.id.instrument_number_et)
    EditText instrumentNumberEt;
    @BindView(R.id.detect_person_et)
    EditText detectPersonEt;
    @BindView(R.id.newly_build_bt)
    Button newlyBuildBt;
    //    @BindView(R.id.confirm_bt)
//    Button confirmBt;
//    private String projectName;

    @Override
    protected void initData() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_project_info;
    }

    boolean isFirstEdit;

    @Override
    public void initView() {
        initUI();
    }

    private void initUI() {
        commonTitleTv.setText("工程信息");
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        ProjectInfo projectInfoToEdit = (ProjectInfo) intent.getSerializableExtra("PROJECT_INFO_TO_EDIT");
        if (projectInfoToEdit == null) {
            isFirstEdit = true;
            return;
        }
        //取上次的,对界面进行恢复,仅仅是恢复,方便用户修改再用
        projectNameEt.setText(projectInfoToEdit.getProjectName());
        //进来修改的,不允许修改工程名称了
        projectNameEt.setEnabled(false);
        constructionOrganizationTv.setText(projectInfoToEdit.getConstructionOrganization());
        fillerTypeEt.setText(projectInfoToEdit.getFillerType());
        instrumentNumberEt.setText(projectInfoToEdit.getInstrumentNumber());
        detectPersonEt.setText(projectInfoToEdit.getDetectPerson());
    }

    @OnClick({R.id.newly_build_bt, R.id.common_back_ll})
    public void onViewClicked(View view) {
        final String projectName = projectNameEt.getText().toString().trim();
        final String constructionOrganization = constructionOrganizationTv.getText().toString().trim();
        final String fillerType = fillerTypeEt.getText().toString().trim();
        final String instrumentNumber = instrumentNumberEt.getText().toString().trim();
        final String detectPerson = detectPersonEt.getText().toString().trim();
        switch (view.getId()) {
            case R.id.newly_build_bt:
                if (!checkEmpty(projectName, constructionOrganization,
                        fillerType, instrumentNumber, detectPerson)) {
                    return;
                }
//                //判断项目是否已经存在
//                if (ProjectDataManager.getInstance().isProjectNameExit(projectName)) {
//                    ToastUtils.showToast("该项目已经存在,无法新建!");
//                    return;
//                }
//                isFirstEdit
                //选择新建项目,但是输入的项目名称已经存在
                if (ProjectNameUUIDSP.getInstance().checkIsExist(projectName) && isFirstEdit) {
                    new RaiingAlertDialog(this, "提示",
                            "要新建的项目已经存在,是否修改该项目信息?",
                            "修改", "取消",
                            new RaiingAlertDialog.CallbackRaiingAlertDialog() {
                                @Override
                                public void onPositive() {
                                    savaProjectInfo(projectName, constructionOrganization, fillerType, instrumentNumber, detectPerson);

                                }

                                @Override
                                public void onNegative() {

                                }
                            }).show();
                } else {
                    savaProjectInfo(projectName, constructionOrganization, fillerType, instrumentNumber, detectPerson);

                }


                break;
            case R.id.common_back_ll:
                finish();
                break;
//            case R.id.project_name_et:
//                List<String> allProjectNames = ProjectDataManager.getInstance().getAllProjectNames();
//                if (allProjectNames == null || allProjectNames.size() == 0) {
//                    return;
//                }
//                FragmentTransaction mFragTransaction = getFragmentManager().beginTransaction();
//                ProjectNameSelectDialog fragment = ((ProjectNameSelectDialog) getFragmentManager().findFragmentByTag(FRAGMENT_TAG_SELSTE));
//                if (fragment == null) {
//                    ProjectNameSelectDialog upgradeDialog = new ProjectNameSelectDialog();
//                    upgradeDialog.show(mFragTransaction, FRAGMENT_TAG_SELSTE);
//                } else {
//                    fragment.show(mFragTransaction, FRAGMENT_TAG_SELSTE);
//                }
//
//                break;
//            case R.id.confirm_bt:
//                if (!checkEmpty(projectName, constructionOrganization,
//                        fillerType, instrumentNumber, detectPerson)) {
//                    return;
//                }
//                //判断项目是否已经存在
//                if (!ProjectDataManager.getInstance().isProjectNameExit(projectName)) {
//                    ToastUtils.showToast("该项目不存在,需要新建!");
//                    return;
//                }
//
//                //插入新测试点
//                setInsertTestPointResult(projectName, constructionOrganization, fillerType, instrumentNumber, detectPerson);
//                break;
        }
    }

    private void savaProjectInfo(String projectName, String constructionOrganization, String fillerType, String instrumentNumber, String detectPerson) {
        //新建项目测试点
        setNewBuildPointResult(projectName, constructionOrganization, fillerType, instrumentNumber, detectPerson);
        //进入新建节点页面
        Bundle bundle = new Bundle();
        bundle.putSerializable("TO_TEST_POINT", ProjectNameUUIDSP.getInstance().getProjectInfo(projectName));
        UIUtils.intentActivity(NewTestPointActivity.class, bundle);
    }

//    private boolean checkIsExist(String projectName) {
//
//
//        List<String> allProjectNames = ProjectNameUUIDSP.getInstance().getAllProjectNames();
//        if (allProjectNames != null) {
//            for (String projectN : allProjectNames) {
//                if (TextUtils.equals(projectN, projectName)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }


//    /**
//     * 插入新测试点
//     */
//    private void setInsertTestPointResult(String projectName, String constructionOrganization, String fillerType, String instrumentNumber, String detectPerson) {
//        TestPoint testPoint = new TestPoint();
//        testPoint.setProjectName(projectName);
//        testPoint.setConstructionOrganization(constructionOrganization);
//        testPoint.setFillerType(fillerType);
//        testPoint.setInstrumentNumber(instrumentNumber);
//        testPoint.setDetectPerson(detectPerson);
//        Intent intent = new Intent();
//        intent.putExtra(INSERT_TEST_POINT, testPoint);
//        setResult(RESULT_CODE_START_DETECT_INSERT, intent);
//        finish();
////        ProjectInfoSP.getInstance().insertTestPoint(projectName, testPoint);
//    }

    /**
     * 新建项目测试点
     */
    private void setNewBuildPointResult(String projectName, String constructionOrganization, String fillerType, String instrumentNumber, String detectPerson) {
//        TestPoint testPoint = new TestPoint();
//        testPoint.setProjectName(projectName);
//        testPoint.setConstructionOrganization(constructionOrganization);
//        testPoint.setFillerType(fillerType);
//        testPoint.setInstrumentNumber(instrumentNumber);
//        testPoint.setDetectPerson(detectPerson);
//        Intent intent = new Intent();
//        intent.putExtra(INSERT_TEST_POINT, testPoint);
//        setResult(RESULT_CODE_START_DETECT_INSERT, intent);
//        finish();
//        ProjectNameUUIDSP.getInstance().setProjectInfoForUUID(projectName);
//        ProjectNameUUIDSP.getInstance().setProjectNameMd5(projectName);
        ProjectNameUUIDSP.getInstance().setProjectInfo(projectName, new ProjectInfo(projectName,
                constructionOrganization, fillerType, instrumentNumber, detectPerson));
    }

    /**
     * 判空
     */
    private boolean checkEmpty(String projectName, String constructionOrganization, String fillerType, String instrumentNumber, String detectPerson) {

        if (TextUtils.isEmpty(projectName)) {
            ToastUtils.showToast("项目名称不能为空!");
            return false;
        }
        if (TextUtils.isEmpty(constructionOrganization)) {
            ToastUtils.showToast("施工单位不能为空!");
            return false;
        }
        if (TextUtils.isEmpty(fillerType)) {
            ToastUtils.showToast("填料类型不能为空!");
            return false;
        }
        if (TextUtils.isEmpty(instrumentNumber)) {
            ToastUtils.showToast("仪器编号不能为空!");
            return false;
        }
        if (TextUtils.isEmpty(detectPerson)) {
            ToastUtils.showToast("检测人不能为空!");
            return false;
        }
        return true;
    }

    @Override
    public void onConfirm(String projectName) {
        projectNameEt.setText(projectName);
    }

    @Override
    public void onCustomProjectName(String projectName) {
        projectNameEt.setText(projectName);
    }
}
