package com.detect.detect.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.shared_preferences.ProjectDataManager;
import com.detect.detect.shared_preferences.TestPoint;
import com.detect.detect.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.detect.detect.constant.SkipActivityConstant.PROJECT_INFO_TEST_POINT;

/**
 * Created by yu on 18/1/13.
 */

public class ProjectInfoDetailSetActivity extends BaseActivity implements ProjectNameSelectDialog.ConfirmClickCallback {
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
    @BindView(R.id.confirm_bt)
    Button confirmBt;
//    private String projectName;

    @Override
    protected void initData() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_project_info_detail_set;
    }

    @Override
    public void initView() {
        initUI();
    }

    private void initUI() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        TestPoint testPoint = (TestPoint) intent.getSerializableExtra(PROJECT_INFO_TEST_POINT);
        if (testPoint == null) {
            return;
        }
        //取上次的,对界面进行恢复,仅仅是恢复,方便用户修改再用
        projectNameEt.setText(testPoint.getProjectName());
        constructionOrganizationTv.setText(testPoint.getConstructionOrganization());
        fillerTypeEt.setText(testPoint.getFillerType());
        instrumentNumberEt.setText(testPoint.getInstrumentNumber());
        detectPersonEt.setText(testPoint.getDetectPerson());
    }

    @OnClick({R.id.newly_build_bt, R.id.project_name_et, R.id.common_back_ll, R.id.confirm_bt})
    public void onViewClicked(View view) {
        String projectName = projectNameEt.getText().toString().trim();
        String constructionOrganization = constructionOrganizationTv.getText().toString().trim();
        String fillerType = fillerTypeEt.getText().toString().trim();
        String instrumentNumber = instrumentNumberEt.getText().toString().trim();
        String detectPerson = detectPersonEt.getText().toString().trim();
        switch (view.getId()) {
            case R.id.newly_build_bt:
                if (!checkEmpty(projectName, constructionOrganization,
                        fillerType, instrumentNumber, detectPerson)) {
                    return;
                }
                //判断项目是否已经存在
                if (ProjectDataManager.getInstance().isProjectNameExit(projectName)) {
                    ToastUtils.showToast("该项目已经存在,无法新建!");
                    return;
                }
                //新建项目测试点
                setNewBuildPointResult(projectName, constructionOrganization, fillerType, instrumentNumber, detectPerson);
                break;
            case R.id.common_back_ll:
                finish();
                break;
            case R.id.project_name_et:
                List<String> allProjectNames = ProjectDataManager.getInstance().getAllProjectNames();
                if (allProjectNames == null || allProjectNames.size() == 0) {
                    return;
                }
                FragmentTransaction mFragTransaction = getFragmentManager().beginTransaction();
                ProjectNameSelectDialog fragment = ((ProjectNameSelectDialog) getFragmentManager().findFragmentByTag(FRAGMENT_TAG_SELSTE));
                if (fragment == null) {
                    ProjectNameSelectDialog upgradeDialog = new ProjectNameSelectDialog();
                    upgradeDialog.show(mFragTransaction, FRAGMENT_TAG_SELSTE);
                } else {
                    fragment.show(mFragTransaction, FRAGMENT_TAG_SELSTE);
                }

                break;
            case R.id.confirm_bt:
                if (!checkEmpty(projectName, constructionOrganization,
                        fillerType, instrumentNumber, detectPerson)) {
                    return;
                }
                //判断项目是否已经存在
                if (!ProjectDataManager.getInstance().isProjectNameExit(projectName)) {
                    ToastUtils.showToast("该项目不存在,需要新建!");
                    return;
                }

                //插入新测试点
                setInsertTestPointResult(projectName, constructionOrganization, fillerType, instrumentNumber, detectPerson);
                break;
        }
    }


    /**
     * 插入新测试点
     */
    private void setInsertTestPointResult(String projectName, String constructionOrganization, String fillerType, String instrumentNumber, String detectPerson) {
        TestPoint testPoint = new TestPoint();
        testPoint.setProjectName(projectName);
        testPoint.setConstructionOrganization(constructionOrganization);
        testPoint.setFillerType(fillerType);
        testPoint.setInstrumentNumber(instrumentNumber);
        testPoint.setDetectPerson(detectPerson);
        Intent intent = new Intent();
        intent.putExtra(INSERT_TEST_POINT, testPoint);
        setResult(RESULT_CODE_START_DETECT_INSERT, intent);
        finish();
//        ProjectInfoSP.getInstance().insertTestPoint(projectName, testPoint);
    }

    /**
     * 新建项目测试点
     */
    private void setNewBuildPointResult(String projectName, String constructionOrganization, String fillerType, String instrumentNumber, String detectPerson) {
        TestPoint testPoint = new TestPoint();
        testPoint.setProjectName(projectName);
        testPoint.setConstructionOrganization(constructionOrganization);
        testPoint.setFillerType(fillerType);
        testPoint.setInstrumentNumber(instrumentNumber);
        testPoint.setDetectPerson(detectPerson);
        Intent intent = new Intent();
        intent.putExtra(INSERT_TEST_POINT, testPoint);
        setResult(RESULT_CODE_START_DETECT_INSERT, intent);
        finish();
//        ProjectInfoSP.getInstance().newBuildProject(projectName, testPoint);
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
