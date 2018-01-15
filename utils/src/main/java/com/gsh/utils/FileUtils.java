package com.gsh.utils;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileUtils {
    private static final String TAG = "FileUtils";

    /**
     * 复制from输入流to文件--copyFile(InputStream inStream, String newPathFile)
     * <p/>
     * 复制from文件to文件---copyFile(String oldPathFile, String newPathFile)
     * <p/>
     * 复制from文件to目录---copyFileToDirectory(File srcFile, File destDir)
     * <p/>
     * 复制from目录to目录---copyDirectoryToDirectory(File srcDir, File destDir)
     * <p/>
     * 创建 文件输出流----FileOutputStream openOutputStream(File file)
     * <p/>
     * 字符串写入文件----writeStringToFile(File file, String data)
     * <p/>
     * 数组数据写入文件---writeStringToFile(File file, String data, String encoding)
     * <p/>
     * 数组数据写入文件---writeByteArrayToFile(File file, byte[] data)
     * <p/>
     * 将CharS写入文件----write(File file, CharSequence data)
     * <p/>
     * 将CharS写入文件----write(File file, CharSequence data, String encoding)
     * <p/>
     * 读取配置文件----Properties readConfiguration(String resname)
     */

    public static final long ONE_KB = 1024L;

    public static final long ONE_MB = 1048576L;
    public static final long ONE_GB = 1073741824L;
    public static final File[] EMPTY_FILE_ARRAY = new File[0];
    private static final long FIFTY_MB = 52428800L;
    private static final Charset UTF8 = Charset.forName("UTF-8");

    // ******* 复制文件********* start
    // *******inputStream*******newPath****************
    public static void copyFile(InputStream inStream, String newPathFile) {
        try {
            int bytesum = 0;
            int byteread = 0;
            if (null != inStream) {
                FileOutputStream fs = new FileOutputStream(newPathFile);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;// 文件大小
                    fs.write(buffer, 0, byteread);
                }
                if (null != fs) {
                    fs.close();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "复制文件错误--inStream----" + e.getMessage());
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    //
                    e.printStackTrace();
                }
            }
        }
    }

    // ********************** 复制文件********* end ***********************

    // ******* 复制文件****** start*******oldPathFile********newPathFile******
    public static void copyFile(String oldPathFile, String newPathFile) {
        InputStream inStream = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPathFile);
            if (oldfile.exists()) {
                inStream = new FileInputStream(oldPathFile);
                FileOutputStream fs = new FileOutputStream(newPathFile);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
                if (null != fs) {
                    fs.close();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "复制文件错误--old----" + e.getMessage());
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    //
                    e.printStackTrace();
                }
            }
        }
    }

    // ********************** 复制文件********* end ***********************
    // ************ 复制文件到指定目录下面********* Start ***********************
    public static void copyFileToDirectory(File srcFile, File destDir)
            throws IOException {
        copyFileToDirectory(srcFile, destDir, true);
    }

    public static void copyFileToDirectory(File srcFile, File destDir,
                                           boolean preserveFileDate) throws IOException {
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if ((destDir.exists()) && (!(destDir.isDirectory()))) {
            throw new IllegalArgumentException("Destination '" + destDir
                    + "' is not a directory");
        }
        File destFile = new File(destDir, srcFile.getName());
        copyFile(srcFile, destFile, preserveFileDate);
    }

    public static void copyFile(File srcFile, File destFile) throws IOException {
        copyFile(srcFile, destFile, true);
    }

    public static void copyDirectoryToDirectory(File srcDir, File destDir)
            throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        }
        if ((srcDir.exists()) && (!(srcDir.isDirectory()))) {
            throw new IllegalArgumentException("Source '" + destDir
                    + "' is not a directory");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if ((destDir.exists()) && (!(destDir.isDirectory()))) {
            throw new IllegalArgumentException("Destination '" + destDir
                    + "' is not a directory");
        }
        copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
    }

    public static void copyDirectory(File srcDir, File destDir)
            throws IOException {
        copyDirectory(srcDir, destDir, true);
    }

    public static void copyDirectory(File srcDir, File destDir,
                                     boolean preserveFileDate) throws IOException {
        copyDirectory(srcDir, destDir, null, preserveFileDate);
    }

    public static void copyDirectory(File srcDir, File destDir,
                                     FileFilter filter) throws IOException {
        copyDirectory(srcDir, destDir, filter, true);
    }

    public static void copyDirectory(File srcDir, File destDir,
                                     FileFilter filter, boolean preserveFileDate) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!(srcDir.exists())) {
            throw new FileNotFoundException("Source '" + srcDir
                    + "' does not exist");
        }
        if (!(srcDir.isDirectory())) {
            throw new IOException("Source '" + srcDir
                    + "' exists but is not a directory");
        }
        if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
            throw new IOException("Source '" + srcDir + "' and destination '"
                    + destDir + "' are the same");
        }

        List<String> exclusionList = null;
        if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
            File[] srcFiles = (filter == null) ? srcDir.listFiles() : srcDir
                    .listFiles(filter);
            if ((srcFiles != null) && (srcFiles.length > 0)) {
                exclusionList = new ArrayList<String>(srcFiles.length);
                for (File srcFile : srcFiles) {
                    File copiedFile = new File(destDir, srcFile.getName());
                    exclusionList.add(copiedFile.getCanonicalPath());
                }
            }
        }
        doCopyDirectory(srcDir, destDir, filter, preserveFileDate,
                exclusionList);
    }

    private static void doCopyDirectory(File srcDir, File destDir,
                                        FileFilter filter, boolean preserveFileDate,
                                        List<String> exclusionList) throws IOException {
        File[] files = (filter == null) ? srcDir.listFiles() : srcDir
                .listFiles(filter);
        if (files == null) {
            throw new IOException("Failed to list contents of " + srcDir);
        }
        if (destDir.exists()) {
            if (!(destDir.isDirectory())) {
                throw new IOException("Destination '" + destDir
                        + "' exists but is not a directory");
            }
        } else if (!(destDir.mkdirs())) {
            throw new IOException("Destination '" + destDir
                    + "' directory cannot be created");
        }

        if (!(destDir.canWrite())) {
            throw new IOException("Destination '" + destDir
                    + "' cannot be written to");
        }
        // 迭代方法，，，复制文件目录
        for (File file : files) {
            File copiedFile = new File(destDir, file.getName());
            if ((exclusionList == null)
                    || (!(exclusionList.contains(file.getCanonicalPath())))) {
                if (file.isDirectory())
                    doCopyDirectory(file, copiedFile, filter, preserveFileDate,
                            exclusionList);
                else {
                    doCopyFile(file, copiedFile, preserveFileDate);
                }
            }

        }

        if (preserveFileDate)
            destDir.setLastModified(srcDir.lastModified());
    }

    // File srcFile复制的 文件，File destFile目标目录的文件
    public static void copyFile(File srcFile, File destFile,
                                boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!(srcFile.exists())) {
            throw new FileNotFoundException("Source '" + srcFile
                    + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile
                    + "' exists but is a directory");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '"
                    + destFile + "' are the same");
        }
        if ((destFile.getParentFile() != null)
                && (!(destFile.getParentFile().exists()))
                && (!(destFile.getParentFile().mkdirs()))) {
            throw new IOException("Destination '" + destFile
                    + "' directory cannot be created");
        }

        if ((destFile.exists()) && (!(destFile.canWrite()))) {
            throw new IOException("Destination '" + destFile
                    + "' exists but is read-only");
        }
        doCopyFile(srcFile, destFile, preserveFileDate);
    }

    private static void doCopyFile(File srcFile, File destFile,
                                   boolean preserveFileDate) throws IOException {
        if ((destFile.exists()) && (destFile.isDirectory())) {
            throw new IOException("Destination '" + destFile
                    + "' exists but is a directory");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0L;
            long count = 0L;
            while (pos < size) {
                count = (size - pos > 52428800L) ? 52428800L : size - pos;
                pos += output.transferFrom(input, pos, count);
            }
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(fis);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '"
                    + srcFile + "' to '" + destFile + "'");
        }

        if (preserveFileDate)
            // setLastModified()//设置最后修改时间
            // lastModified() 返回文件的最后修改时间
            destFile.setLastModified(srcFile.lastModified());// 设置最后修改时间
    }

    public static void writeStringToFile(File file, String data, String encoding)
            throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file);
            IOUtils.write(data, out, encoding);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static void writeStringToFile(File file, String data)
            throws IOException {
        writeStringToFile(file, data, null);
    }

    public static void write(File file, CharSequence data) throws IOException {
        String str = (data == null) ? null : data.toString();
        writeStringToFile(file, str);
    }

    public static void write(File file, CharSequence data, String encoding)
            throws IOException {
        String str = (data == null) ? null : data.toString();
        writeStringToFile(file, str, encoding);
    }

    public static void writeByteArrayToFile(File file, byte[] data)
            throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file);
            out.write(data);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static FileOutputStream openOutputStream(File file)
            throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file
                        + "' exists but is a directory");
            }
            if (!(file.canWrite()))
                throw new IOException("File '" + file
                        + "' cannot be written to");
        } else {
            File parent = file.getParentFile();
            // !file.getParentFile().exists() //判断目标文件所在的目录不存在
            // !file.getParentFile().mkdirs()//创建目标文件所在目录失败！
            if ((parent != null) && (!(parent.exists()))
                    && (!(parent.mkdirs()))) {
                throw new IOException("File '" + file
                        + "' could not be created");
            }
        }

        return new FileOutputStream(file);
    }

    public static Properties readConfiguration(String resname) {
        try {

            Properties pro = new Properties();
            // InputStream in =
            // YiyouAppliaction.getContext().getAssets().open(resname);

            // pro.load(in);
            return pro;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 个人理解应该算得上是char类型得一个集合，是interface）
     *
     * String 类代表字符串。Java 程序中的所有字符串字面值（如 "abc" ）都作为此类的实例来实现。
     *
     * （这个没啥了解的吧，经常用 class） public interface CharSequence
     *
     * public int length();
     *
     * public char charAt(int index);
     *
     * public CharSequence subSequence(int start,int end);
     *
     * public String toString(); }
     *
     * public final class String implements Serializable, Comparable,
     * CharSequence { xxxxxx } String里面方法很多就不写了，应该能看出来不同把。
     * 这是一个接口：在JDK1.4中，引入了CharSequence接口
     * ，实现了这个接口的类有：CharBuffer、String、StringBuffer、StringBuilder这个四个类。
     *
     * CharBuffer为nio里面用的一个类，String实现这个接口理所当然，StringBuffer也是一个
     * CharSequence，StringBuilder是Java抄袭C#的一个类，基本和StringBuffer类一样，效率高，但是不保证线程安
     * 全，在不需要多线程的环境下可以考虑。
     *
     */
}
