package com.detect.detect.utils;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.detect.detect.db.WaveData;
import com.detect.detect.shared_preferences.TestPoint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 读写本地的数据(csv格式的测试数据)的工具类.
 * Created by dongdong.yu on 18/1/15.
 */
public class CSVUtils {
    private static final String TAG = "CSVUtils";

    /**
     * 获得的MBBT数据 写入到sdcard文件中
     *
     * @param path sdcard存在的csv文件
     * @param list 待写入的MBBT数据集合
     */
    public static void writeTestPointListToCSV(String path, List<TestPoint> list) {
        if (TextUtils.isEmpty(path)) {
            Log.d(TAG, "writeTestPointListToCSV:路径不能为空 ");
            return;
        }
        if (list == null) {
            Log.d(TAG, "writeTestPointListToCSV: list集合不能为空");
            return;
        }
        try {
            File csv = new File(path);
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));
            // 新增一行数据
            for (int s = 0; s < list.size(); s++) {
                TestPoint testPoint = list.get(s);
//            for (TestPoint testPoint : list) {
                String projectName = testPoint.getProjectName();
                String constructionOrganization = testPoint.getConstructionOrganization();
                String buildSerialNum = testPoint.getBuildSerialNum();
//                int[] advArr = testPoint.getWaveListStr();
                String waveListStr = testPoint.getWaveListStr();

                String coordinateInfo = testPoint.getCoordinateInfo();
                String detectPerson = testPoint.getDetectPerson();
                long detectTime = testPoint.getDetectTime();
                String fillerType = testPoint.getFillerType();
                String instrumentNumber = testPoint.getInstrumentNumber();
                String picPath = testPoint.getPicPath();
                // 新增一行数据
//                bw.newLine();
//                for (int adv : advArr) {
//                    bw.write(projectName + "," + constructionOrganization + ","
//                            + buildSerialNum + "," + coordinateInfo + "," + detectPerson + ","
//                            + detectTime + "," + fillerType + "," + instrumentNumber + "," + picPath + "," + adv);
//                    bw.newLine();
//                }
                bw.newLine();
                bw.newLine();
                bw.newLine();
                bw.newLine();
                bw.newLine();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                String formatData = simpleDateFormat.format(new Date(detectTime * 1000L));
                bw.write("第 " + (s + 1) + " 个节点信息:  工程名称: " + projectName + "  ,施工单位: " + constructionOrganization + "  ,构建序号: "
                        + buildSerialNum + "  ,坐标信息: " + coordinateInfo + "  ,检测人: " + detectPerson + "  ,检测时间: "
                        + formatData + "  ,填料类型: " + fillerType + "  ,仪器编号: " + instrumentNumber);
                bw.newLine();
                List<WaveData> arrayLists = JSON.parseArray(waveListStr, WaveData.class);
                for (int j = 0; j < arrayLists.size(); j++) {
                    WaveData waveData = arrayLists.get(j);
                    int[] waveArr = waveData.waveArr;
                    for (int i = 0; i < 100; i++) {
                        for (int m = 0; m < 10; m++) {
                            StringBuilder stringBuilder = new StringBuilder();
                            if (m != 9) {
                                stringBuilder.append(waveArr[i * 10 + m] + ",");
                            } else {
                                stringBuilder.append(waveArr[i * 10 + m]);
                            }
                            bw.write(stringBuilder.toString());
                        }
                        bw.newLine();
                    }
                }


            }
            bw.close();
            ToastUtils.showToast("导出数据成功,导出目录:/detect_project/out_put!");
            Log.d(TAG, "---写入成功");
        } catch (IOException e) {
            // 捕获BufferedWriter对象关闭时的异常
            e.printStackTrace();
        }
    }

}
