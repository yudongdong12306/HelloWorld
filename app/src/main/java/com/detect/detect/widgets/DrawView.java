package com.detect.detect.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.detect.detect.db.FileConstant;
import com.detect.detect.utils.DisplayUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;


public class DrawView extends View {
    private Context context;
    private Paint linePaint;
    private Paint linePaint2;
    private Paint linePaint31;
    private Paint linePaint32;
    private Paint linePaint33;
    private float lineWidth = DisplayUtils.dp2px(8);
    private float dashWidth = DisplayUtils.dp2px(8);
    private int[] data1;
    private int[] data2;
    private int[] data3;
    private static final int dataSize = 20;
    private Path path1;
    private Path path2;
    private Path path3;
    private Paint textPaint;

    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        setData1();
    }

    public String viewSaveToImage() {
        setDrawingCacheEnabled(true);
        buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap cachebmp = Bitmap.createBitmap(getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        FileOutputStream fos;
        String imagePath = "";
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
//                File sdRoot = Environment.getExternalStorageDirectory();
                File file = new File(FileConstant.DB_PATH, Calendar.getInstance().getTimeInMillis() + ".png");
                fos = new FileOutputStream(file);
                imagePath = file.getAbsolutePath();
            } else
                throw new Exception("创建文件失败!");

            cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);

            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
//    LogUtil.e("imagePath="+imagePath);
        destroyDrawingCache();
        return imagePath;
    }

    public void setData1() {
        data1 = new int[dataSize];
        data2 = new int[dataSize];
        data3 = new int[dataSize];
        for (int i = 0; i < dataSize; i++) {
            data1[i] = (int) (Math.random() * 2000);
            data2[i] = (int) (Math.random() * 2000);
            data3[i] = (int) (Math.random() * 2000);
        }
    }

    private int times = 0;

    public void setData(int[] data) {
        if (data == null || data.length != dataSize) {
            return;
        }
        if (times == 0) {
            times++;
            data1 = new int[dataSize];
            for (int i = 0; i < dataSize; i++) {
                data1[i] = 2000 - data[i];
            }
        } else if (times == 1) {
            times++;
            data2 = new int[dataSize];
            for (int i = 0; i < dataSize; i++) {
                data2[i] = 2000 - data[i];
            }

        } else if (times == 2) {
            times++;
            data3 = new int[dataSize];
            for (int i = 0; i < dataSize; i++) {
                data3[i] = 2000 - data[i];
            }
        }
        invalidate();
    }

    private void init(Context context) {
        this.context = context;
        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(DisplayUtils.dp2px(2));

        linePaint2 = new Paint();
        linePaint2.setColor(Color.BLACK);
        linePaint2.setStyle(Paint.Style.FILL);
        linePaint2.setStrokeWidth(DisplayUtils.dp2px(1));

        linePaint31 = new Paint();
        linePaint31.setColor(Color.YELLOW);
        linePaint31.setStyle(Paint.Style.STROKE);
        linePaint31.setStrokeWidth(DisplayUtils.dp2px(1));
        linePaint32 = new Paint();
        linePaint32.setColor(Color.BLUE);
        linePaint32.setStyle(Paint.Style.STROKE);
        linePaint32.setStrokeWidth(DisplayUtils.dp2px(1));
        linePaint33 = new Paint();
        linePaint33.setColor(Color.RED);
        linePaint33.setStyle(Paint.Style.STROKE);
        linePaint33.setStrokeWidth(DisplayUtils.dp2px(1));
        path1 = new Path();
        path2 = new Path();
        path3 = new Path();

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(DisplayUtils.dp2px(10));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(DisplayUtils.dp2px(25), 0);
        canvas.drawLine(0, getHeight() - DisplayUtils.dp2px(25), (getWidth() - DisplayUtils.dp2px(25)), getHeight() - DisplayUtils.dp2px(25), linePaint);
        canvas.drawLine(0, 0, 0, getHeight() - DisplayUtils.dp2px(25), linePaint);

        for (int i = 0; i < 5; i++) {
            drawHorizontalLine(canvas, i / 5.0f);
        }

        for (int i = 1; i < 6; i++) {
            drawVerticalLine(canvas, i / 5.0f);
        }
        drawData(canvas, data1, linePaint31, path1);
        drawData(canvas, data2, linePaint32, path2);
        drawData(canvas, data3, linePaint33, path3);
        canvas.restore();
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        for (int i = 0; i < 6; i++) {
            if (0 == i) {
                canvas.drawText("2000", DisplayUtils.dp2px(0), (i / 5.0f) * getHeight() + DisplayUtils.dp2px(10), textPaint);
            } else {
                int num = 2000 - (int) (2000 * (i / 5.0f));
                if (num == 0) {
                    canvas.drawText("    0", 0, (i / 5.0f) * (getHeight() - DisplayUtils.dp2px(25)), textPaint);
                } else {
                    canvas.drawText(String.valueOf(num), DisplayUtils.dp2px(0), (i / 5.0f) * (getHeight() - DisplayUtils.dp2px(25)), textPaint);
                }
            }
        }

        //
        int spaceWith = (int) ((getWidth() - DisplayUtils.dp2px(25)) / 5.0f);

        for (int i = 1; i < 6; i++) {
            if (i != 5) {
                canvas.drawText(String.valueOf(200 * i), DisplayUtils.dp2px(25 - 10 - 5) + spaceWith * i, getHeight() - DisplayUtils.dp2px(25 - 15), textPaint);
            } else {
                canvas.drawText(String.valueOf(200 * i), DisplayUtils.dp2px(25 - 10 - 15) + spaceWith * i, getHeight() - DisplayUtils.dp2px(25 - 15), textPaint);
            }
        }
    }

    private void drawData(Canvas canvas, int[] data1, Paint linePaint31, Path path1) {
        if (data1 != null && data1.length > 0) {
            path1.moveTo(0, (data1[0] / 2000f) * (getHeight() - DisplayUtils.dp2px(25)));
            for (int i = 1; i < data1.length; i++) {
                path1.lineTo(((i / ((dataSize - 1) * 1.0f))) * (getWidth() - DisplayUtils.dp2px(25)), (data1[i] / 2000f) * (getHeight() - DisplayUtils.dp2px(25)));
            }
            canvas.drawPath(path1, linePaint31);
        }
    }


    /**
     * 画水平方向虚线
     *
     * @param canvas
     */
    public void drawHorizontalLine(Canvas canvas, float radio) {
        float totalWidth = 0;
        canvas.save();
        float[] pts = {0, 0, lineWidth, 0};
        //在画线之前需要先把画布向下平移办个线段高度的位置，目的就是为了防止线段只画出一半的高度
        //因为画线段的起点位置在线段左下角
//        for (int i = 0; i < 5; i++) {
        canvas.translate(0, (getHeight() - DisplayUtils.dp2px(25)) * radio);
        while (totalWidth <= (getWidth() - DisplayUtils.dp2px(25))) {
            canvas.drawLines(pts, linePaint2);
            canvas.translate(lineWidth + dashWidth, 0);
            totalWidth += lineWidth + dashWidth;
        }
        canvas.restore();
    }

    /**
     * 画竖直方向虚线
     *
     * @param canvas
     */
    public void drawVerticalLine(Canvas canvas, float radio) {
        float totalWidth = 0;
        canvas.save();
        float[] pts = {0, 0, 0, lineWidth};
        //在画线之前需要先把画布向右平移半个线段高度的位置，目的就是为了防止线段只画出一半的高度
        //因为画线段的起点位置在线段左下角
        canvas.translate((getWidth() - DisplayUtils.dp2px(25)) * radio, 0);
        while (totalWidth <= (getHeight() - DisplayUtils.dp2px(25))) {
            canvas.drawLines(pts, linePaint2);
            canvas.translate(0, lineWidth + dashWidth);
            totalWidth += lineWidth + dashWidth;
        }
        canvas.restore();
    }
}
