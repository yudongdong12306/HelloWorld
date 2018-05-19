package com.detect.detect.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.constant.CommonConstant;
import com.detect.detect.db.FileConstant;
import com.detect.detect.shared_preferences.Project;
import com.detect.detect.shared_preferences.ProjectDataManager;
import com.detect.detect.shared_preferences.TestPoint;
import com.detect.detect.utils.CSVUtils;
import com.detect.detect.utils.PicDisplayUtils;
import com.detect.detect.utils.ToastUtils;
import com.detect.detect.widgets.PersonalPopupWindow;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongdong.yu on 2018/1/8.
 */

public class DataManagerActivity extends BaseActivity implements IProjectTestPointClickCallback, ITakePhoto, ConfirmOrCancelDialog.ConfirmClickCallback {
    private static final String TAG = "DataManagerActivity";
    private static final String FRAGMENT_TAG_DELETE = "fragment_tag_delete";
    @BindView(R.id.project_data_lv)
    ExpandableListView projectDataLv;
    @BindView(R.id.project_name_tv)
    TextView projectNameTv;
    @BindView(R.id.test_point_data_bt)
    Button testPointDataBt;
    @BindView(R.id.test_point_pic_bt)
    Button testPointPicBt;
    @BindView(R.id.data_fl)
    FrameLayout dataFl;
    @BindView(R.id.common_back_ll)
    LinearLayout backBt;
    @BindView(R.id.add_pic_bt)
    Button addPicBt;
    @BindView(R.id.out_put_bt)
    Button outPutBt;
    private String mProjectName;
    private String mBuildSerialNum;
    private PersonalPopupWindow popupWindow;
    private DataManagerPresenter mPresenter;
    private List<String> mProjectNameList;
    private List<List<String>> mAllTestPointList;
    private ProjectDataAdapter mAdapter;
    private LineChart mChart;
    private ArrayList<int[]> waveDataList;

    @Override
    protected void initData() {
        mPresenter = new DataManagerPresenter(this);
        initPopupView();

        mChart = new LineChart(this);
        waveDataList = new ArrayList<>();
        addTestData();
        //添加波形图的测试数据
        mChart.setViewPortOffsets(0, 0, 0, 0);
        mChart.setBackgroundColor(Color.parseColor("#A9A9A9"));

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setMaxHighlightDistance(300);

        //        XAxis x = mChart.getXAxis();
//        x.setEnabled(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setLabelCount(40);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.WHITE);

        YAxis y = mChart.getAxisLeft();
//        y.setTypeface(mTfLight);
        y.setLabelCount(6, false);
        y.setTextColor(Color.WHITE);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.WHITE);

        mChart.getAxisRight().setEnabled(false);

        // add data
        setData();

        mChart.getLegend().setEnabled(false);

        mChart.animateXY(2000, 2000);
        mChart.setVisibleXRange(0.0f, 40.0f);


        for (IDataSet set : mChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

        mChart.invalidate();


        // dont forget to refresh the drawing
        mChart.invalidate();
    }

    private void addTestData() {
        int[] ints1 = new int[1000];
        int[] ints2 = new int[1000];
        int[] ints3 = new int[1000];
        for (int i = 0; i < 1000; i++) {
            int round = (int) (Math.random() * 5000);
            ints1[i] = round;
        }
        for (int i = 0; i < 1000; i++) {
            int round = (int) (Math.random() * 5000);
            ints2[i] = round;
        }
        for (int i = 0; i < 1000; i++) {
            int round = (int) (Math.random() * 5000);
            ints3[i] = round;
        }
        waveDataList.add(ints1);
        waveDataList.add(ints2);
        waveDataList.add(ints3);
    }


    private void setData() {
        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<Entry> yVals2 = new ArrayList<>();
        ArrayList<Entry> yVals3 = new ArrayList<>();
        int[] intArr1 = waveDataList.get(0);
        int[] intArr2 = waveDataList.get(1);
        int[] intArr3 = waveDataList.get(2);
        for (int i = 0; i < intArr1.length; i++) {
            yVals1.add(new Entry(i, intArr1[i]));
        }

        for (int i = 0; i < intArr2.length; i++) {
            yVals2.add(new Entry(i, intArr2[i]));
        }
        for (int i = 0; i < intArr3.length; i++) {
            yVals3.add(new Entry(i, intArr3[i]));
        }
//        ArrayList<Entry> yVals = new ArrayList<Entry>();
//        for (int i = 0; i < count; i++) {
//            float mult = (range + 1);
//            float val = (float) (Math.random() * mult) + 20;// + (float)
//            // ((mult *
//            // 0.1) / 10);
//            Log.d(TAG, "setData: val: " + val);
//            yVals.add(new Entry(i, val));
//        }

        LineDataSet set1, set2, set3;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() >= 3) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
            set3 = (LineDataSet) mChart.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "DataSet 1");
            set2 = new LineDataSet(yVals2, "DataSet 2");
            set3 = new LineDataSet(yVals3, "DataSet 3");
            getSetList(set1);
            getSetList(set2);
            getSetList(set3);

            // create a data object with the datasets
            LineData data = new LineData(set1, set2, set3);
//            data.setValueTypeface(mTfLight);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            mChart.setData(data);
        }
    }

    private void getSetList(LineDataSet set) {
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        //set1.setDrawFilled(true);
        set.setDrawCircles(false);
        set.setLineWidth(1.8f);
        set.setCircleRadius(4f);
        set.setCircleColor(Color.WHITE);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setColor(Color.WHITE);
        set.setFillColor(Color.WHITE);
        set.setFillAlpha(100);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });
    }

    @Override
    public void initPopupView() {
        popupWindow = new PersonalPopupWindow(this);
        popupWindow.setOnItemClickListener(new PersonalPopupWindow.OnItemClickListener() {
            @Override
            public void onCaremaClicked() {
                mPresenter.btnCameraClicked();
            }

            @Override
            public void onPhotoClicked() {
                mPresenter.btnPhotoClicked();
            }

            @Override
            public void onCancelClicked() {
                mPresenter.btnCancelClicked();
            }
        });
    }


    @Override
    public void showHead(Bitmap bitmap) {
//        civHead.setImageBitmap(bitmap);
    }

    @Override
    public void showPopupView() {
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_new_build_test_point, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    @Override
    public void dismissPopupView() {
        popupWindow.dismiss();
    }

    @Override
    public boolean popupIsShowing() {
        return popupWindow.isShowing();
    }

    @Override
    public void gotoHeadSettingActivity(String path) {
        if (TextUtils.isEmpty(path)) {
            ToastUtils.showToast("获取图片失败,请重试!");
            return;
        }
        ToastUtils.showToast("添加图片成功,图片路径: " + path);
    }

    @Override
    public void gotoSystemPhoto(int requestCode) {
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media
                .EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), requestCode);
    }

    @Override
    public void gotoSystemCamera(File tempFile, int requestCode) {
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            //            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //            Uri contentUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + "" +
            //                    ".fileProvider", tempFile);
            //            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        mPresenter.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_data_manager;
    }


    @Override
    public void initView() {
//        List<Project> allProjects = ProjectInfoSP.getInstance().getAllProjects();
        if (prepareAdapterData()) return;
        mAdapter = new ProjectDataAdapter(this, mProjectNameList, mAllTestPointList, this);
        projectDataLv.setAdapter(mAdapter);
    }

    private boolean prepareAdapterData() {
        mProjectNameList = new ArrayList<>();
        mAllTestPointList = new ArrayList<>();
        List<Project> allProjects = ProjectDataManager.getInstance().getAllProjects();
        if (allProjects == null || allProjects.size() == 0) {
            if (mProjectNameList != null) {
                mProjectNameList.clear();
            }
            if (mAllTestPointList != null) {
                mAllTestPointList.clear();
            }
            return true;
        }

        for (Project project : allProjects) {
            if (project != null) {
                mProjectNameList.add(project.getProjectName());
                List<TestPoint> testPoints = project.getTestPointList();
                List<String> testPointList = new ArrayList<>();
                for (TestPoint testPoint : testPoints) {
                    if (testPoint != null) {
                        testPointList.add(testPoint.getBuildSerialNum());
                    }
                }
                mAllTestPointList.add(testPointList);
            }
        }
        return false;
    }

    @OnClick({R.id.project_name_tv, R.id.test_point_data_bt, R.id.test_point_pic_bt, R.id.common_back_ll, R.id.add_pic_bt, R.id.out_put_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.project_name_tv:
                break;
            case R.id.test_point_data_bt:
                dataFl.removeAllViews();
                dataFl.addView(mChart);
                break;
            case R.id.test_point_pic_bt:
                showPic();
                break;
            case R.id.common_back_ll:
                finish();
                break;
            case R.id.add_pic_bt:
                addPic();
                break;
            case R.id.out_put_bt:
                outputData(mProjectName);
                break;
        }
    }

    private void outputData(String projectName) {
        if (TextUtils.isEmpty(projectName)) {
            ToastUtils.showToast("请选择工程或者工程节点");
            return;
        }
        File file = new File(FileConstant.OUT_PUT_PATH + projectName + File.separator);
        if (!file.exists()) {
            file.mkdirs();
        }
        String path = FileConstant.OUT_PUT_PATH + projectName + File.separator + projectName + ".csv";
        File fileDir = new File(path);
        if (fileDir.exists()) {
            fileDir.delete();
        }
        try {
            boolean newFile = fileDir.createNewFile();
            Log.d(TAG, "outputData: newFile: " + newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Project> allProjects = ProjectDataManager.getInstance().getAllProjects();
        if (allProjects != null && allProjects.size() > 0) {
            for (Project project : allProjects) {
                if (project == null) {
                    continue;
                }
                if (TextUtils.equals(project.getProjectName(), projectName)) {
                    List<TestPoint> testPointList = project.getTestPointList();
                    if (testPointList != null && testPointList.size() > 0) {
                        CSVUtils.writeTestPointListToCSV(fileDir.getAbsolutePath(), testPointList);
                    }
                    //导出图片到指定文件夹
                    saveTestPointsPic(projectName, testPointList);
                }
            }
        }
    }

    private void saveTestPointsPic(String projectName, List<TestPoint> testPointList) {
        for (TestPoint testPoint : testPointList) {
            if (testPoint == null) {
                continue;
            }
            String picPath = testPoint.getPicPath();
            if (!TextUtils.isEmpty(picPath)) {
                savePicToLocal(projectName, picPath, testPoint);
            }
        }
    }

    private void savePicToLocal(String projectName, String oldPicPath, TestPoint testPoint) {
        String newPath = FileConstant.OUT_PUT_PATH + projectName;
        File fileDir = new File(newPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File newFile = new File(newPath, testPoint.getBuildSerialNum() + ".jpg");
        if (newFile.exists()) {
            newFile.delete();
        }
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        copyPic(oldPicPath, newFile.getAbsolutePath());

    }

    private void copyPic(String oldPicPath, String newPicPath) {
        int length;
        try {
            FileInputStream fis = new FileInputStream(oldPicPath);
            FileOutputStream fos = new FileOutputStream(newPicPath);
            byte[] buffer = new byte[1024 * 8];
            while ((length = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPic() {
        if (TextUtils.isEmpty(mProjectName) || TextUtils.isEmpty(mBuildSerialNum)) {
            ToastUtils.showToast("未选择构件,无法添加图片!");
            return;
        }
        showPopupView();
    }

    private void showPic() {
        dataFl.removeAllViews();
        if (TextUtils.isEmpty(mProjectName) || TextUtils.isEmpty(mBuildSerialNum)) {
            return;
        }
//        String testPointPicPath = ProjectInfoSP.getInstance().getTestPointPicPath(mProjectName
//                , mBuildSerialNum);
        String testPointPicPath = ProjectDataManager.getInstance().getTestPointPicPath(mProjectName
                , mBuildSerialNum);
        if (TextUtils.isEmpty(testPointPicPath)) {
            return;
        }
        Bitmap bitmap = PicDisplayUtils.decodeSampledBitmap(testPointPicPath, 2000, 720);
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        dataFl.addView(imageView);

    }

    @Override
    public void onTestPointClicked(String projectName, String buildSerialNum) {
        this.mProjectName = projectName;
        this.mBuildSerialNum = buildSerialNum;
        projectNameTv.setText("工程" + projectName + "构件序号" + buildSerialNum);
    }

    @Override
    public void onProjectClicked(String projectName) {
        this.mProjectName = projectName;
        this.mBuildSerialNum = null;
        projectNameTv.setText("工程" + projectName);
    }

    @Override
    public void onTestPointLongClicked(String projectName, String buildSerialNum) {
        this.mProjectName = projectName;
        this.mBuildSerialNum = buildSerialNum;
//        ToastUtils.showToast("选中了: " + projectName + "工程" + buildSerialNum + "节点");
        FragmentTransaction mFragTransaction = getFragmentManager().beginTransaction();
        ConfirmOrCancelDialog fragment = ((ConfirmOrCancelDialog) getFragmentManager().findFragmentByTag(FRAGMENT_TAG_DELETE));
        if (fragment == null) {
            ConfirmOrCancelDialog confirmDialog = new ConfirmOrCancelDialog();
            Bundle args = new Bundle();
            args.putString(CommonConstant.PROJECT_NAME, projectName);
            args.putString(CommonConstant.BUILD_SERIAL_NUM, buildSerialNum);
            confirmDialog.setArguments(args);
            confirmDialog.show(mFragTransaction, FRAGMENT_TAG_DELETE);
        } else {
            fragment.show(mFragTransaction, FRAGMENT_TAG_DELETE);
        }
    }

    @Override
    public void onConfirm(String projectName, String buildSerialNum) {
        if (TextUtils.isEmpty(projectName)) {
            return;
        }
        if (TextUtils.isEmpty(buildSerialNum)) {
            return;
        }
        //执行删除逻辑
        ProjectDataManager dataManager = ProjectDataManager.getInstance();
        boolean flag = dataManager.deleteTestPoint(projectName, buildSerialNum);
        if (flag) {
            ToastUtils.showToast("删除成功!");
        } else {
            ToastUtils.showToast("删除失败!");
        }
        int itemNum = dataManager.queryTableItemNum(projectName);
        Log.d(TAG, "onConfirm: 表还有数据条目: " + itemNum);
        if (itemNum <= 0) {
            dataManager.deleteProject(projectName);
        }
        prepareAdapterData();
        mAdapter.refreshUi(mProjectNameList, mAllTestPointList);
    }
}
