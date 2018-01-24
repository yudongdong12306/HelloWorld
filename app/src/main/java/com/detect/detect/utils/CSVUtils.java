package com.detect.detect.utils;

import android.text.TextUtils;
import android.util.Log;

import com.detect.detect.shared_preferences.TestPoint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
            for (TestPoint testPoint : list) {
                String projectName = testPoint.getProjectName();
                String constructionOrganization = testPoint.getConstructionOrganization();
                int buildSerialNum = testPoint.getBuildSerialNum();
                int[] advArr = testPoint.getAdvArr();
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
                bw.write(projectName + "," + constructionOrganization + ","
                        + buildSerialNum + "," + coordinateInfo + "," + detectPerson + ","
                        + detectTime + "," + fillerType + "," + instrumentNumber);
                bw.newLine();
                for (int i = 0; i < 100; i++) {
                    for (int j = 0; j < 10; j++) {
                        StringBuilder stringBuilder = new StringBuilder();
                        if (j != 9) {
                            stringBuilder.append(advArr[i * 10 + j] + ",");
                        } else {
                            stringBuilder.append(advArr[i * 10 + j]);
                        }
                        bw.write(stringBuilder.toString());
                    }
                    bw.newLine();
                }

            }
            bw.close();
            ToastUtils.showToast("导出数据成功!");
            Log.d(TAG, "---写入成功");
        } catch (IOException e) {
            // 捕获BufferedWriter对象关闭时的异常
            e.printStackTrace();
        }
    }

}
