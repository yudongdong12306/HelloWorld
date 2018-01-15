package com.gsh.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * IOUtils.
 *
 * @author GSH
 */

/**
 * 写入字符流长度--long---- copyLarge(Reader input, Writer output)
 * <p/>
 * 两个字符流是否相等--boolean---- contentEquals(Reader input1, Reader input2)
 * <p/>
 * 两个输入流是否相等--boolean---- contentEquals(InputStream input1, InputStream input2)
 * <p/>
 * char[]----- toCharArray(InputStream is)
 * <p/>
 * char[] toCharArray(Reader input)
 * <p/>
 * char[] toCharArray(InputStream is, String encoding)
 * <p/>
 * byte[] toByteArray(Reader input, String encoding)
 * <p/>
 * byte[] toByteArray(Reader input)
 * <p/>
 * byte[] toByteArray(InputStream input)
 * <p/>
 * String toString(InputStream input)
 * <p/>
 * String toString(InputStream input, String encoding)
 * <p/>
 * String toString(Reader input)
 * <p/>
 * List<String> readLines(InputStream input)
 * <p/>
 * List<String> readLines(InputStream input, String encoding)
 * <p/>
 * List<String> readLines(Reader input)
 * <p/>
 * write(byte[] data, OutputStream output)
 * <p/>
 * write(byte[] data, Writer output)
 * <p/>
 * write(byte[] data, Writer output, String encoding)
 * <p/>
 * write(String data, OutputStream output)
 * <p/>
 * write(String data, OutputStream output, String encoding)
 * <p/>
 * write(StringBuffer data, Writer output)
 * <p/>
 * writeLines(Collection lines, String lineEnding,OutputStream output)
 */
@SuppressWarnings("unchecked")
public class IOUtils {
    public static final char DIR_SEPARATOR_UNIX = 47;

    public static final char DIR_SEPARATOR_WINDOWS = 92;

    public static final char DIR_SEPARATOR = File.separatorChar;

    public static final String LINE_SEPARATOR_UNIX = "\n";

    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";

    public static final String LINE_SEPARATOR;

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    static {
        StringWriter buf = new StringWriter(4);
        PrintWriter out = new PrintWriter(buf);
        out.println();
        LINE_SEPARATOR = buf.toString();
    }

    public static void closeQuietly(Reader input) {
        try {
            if (input != null)
                input.close();
        } catch (IOException ioe) {
        }
    }

    public static void closeQuietly(Writer output) {
        try {
            if (output != null)
                output.close();
        } catch (IOException ioe) {
        }
    }

    public static void closeQuietly(InputStream input) {
        try {
            if (input != null)
                input.close();
        } catch (IOException ioe) {
        }
    }

    public static void closeQuietly(OutputStream output) {
        try {
            if (output != null)
                output.close();
        } catch (IOException ioe) {
        }
    }

    public static void closeQuietly(Socket sock) {
        if (sock == null)
            return;
        try {
            sock.close();
        } catch (IOException ioe) {
        }
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException ioe) {
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input, String encoding)
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output, encoding);
        return output.toByteArray();
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static byte[] toByteArray(String input) throws IOException {
        return input.getBytes();
    }

    public static char[] toCharArray(InputStream is) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        copy(is, output);
        return output.toCharArray();
    }

    public static char[] toCharArray(InputStream is, String encoding)
            throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        copy(is, output, encoding);
        return output.toCharArray();
    }

    public static char[] toCharArray(Reader input) throws IOException {
        CharArrayWriter sw = new CharArrayWriter();
        copy(input, sw);
        return sw.toCharArray();
    }

    public static String toString(InputStream input) throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw);
        return sw.toString();
    }

    public static String toString(InputStream input, String encoding)
            throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw, encoding);
        return sw.toString();
    }

    public static String toString(Reader input) throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw);
        return sw.toString();
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static String toString(byte[] input) throws IOException {
        return new String(input);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static String toString(byte[] input, String encoding)
            throws IOException {
        if (encoding == null) {
            return new String(input);
        }
        return new String(input, encoding);
    }

    public static List<String> readLines(InputStream input) throws IOException {
        InputStreamReader reader = new InputStreamReader(input);
        return readLines(reader);
    }

    public static List<String> readLines(InputStream input, String encoding)
            throws IOException {
        if (encoding == null) {
            return readLines(input);
        }
        InputStreamReader reader = new InputStreamReader(input, encoding);
        return readLines(reader);
    }

    public static List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = new BufferedReader(input);
        List<String> list = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }

    public static InputStream toInputStream(String input) {
        byte[] bytes = input.getBytes();
        return new ByteArrayInputStream(bytes);
    }

    public static InputStream toInputStream(String input, String encoding)
            throws IOException {
        byte[] bytes = (encoding != null) ? input.getBytes(encoding) : input
                .getBytes();
        return new ByteArrayInputStream(bytes);
    }

    public static void write(byte[] data, OutputStream output)
            throws IOException {
        if (data != null)
            output.write(data);
    }

    public static void write(byte[] data, Writer output) throws IOException {
        if (data != null)
            output.write(new String(data));
    }

    public static void write(byte[] data, Writer output, String encoding)
            throws IOException {
        if (data != null)
            if (encoding == null)
                write(data, output);
            else
                output.write(new String(data, encoding));
    }

    public static void write(char[] data, Writer output) throws IOException {
        if (data != null)
            output.write(data);
    }

    public static void write(char[] data, OutputStream output)
            throws IOException {
        if (data != null)
            output.write(new String(data).getBytes());
    }

    public static void write(char[] data, OutputStream output, String encoding)
            throws IOException {
        if (data != null)
            if (encoding == null)
                write(data, output);
            else
                output.write(new String(data).getBytes(encoding));
    }

    public static void write(String data, Writer output) throws IOException {
        if (data != null)
            output.write(data);
    }

    public static void write(String data, OutputStream output)
            throws IOException {
        if (data != null)
            output.write(data.getBytes());
    }

    public static void write(String data, OutputStream output, String encoding)
            throws IOException {
        if (data != null)
            if (encoding == null)
                write(data, output);
            else
                output.write(data.getBytes(encoding));
    }

    public static void write(StringBuffer data, Writer output)
            throws IOException {
        if (data != null)
            output.write(data.toString());
    }

    public static void write(StringBuffer data, OutputStream output)
            throws IOException {
        if (data != null)
            output.write(data.toString().getBytes());
    }

    public static void write(StringBuffer data, OutputStream output,
                             String encoding) throws IOException {
        if (data != null)
            if (encoding == null)
                write(data, output);
            else
                output.write(data.toString().getBytes(encoding));
    }

    public static void writeLines(Collection lines, String lineEnding,
                                  OutputStream output) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = LINE_SEPARATOR;
        }
        for (Iterator it = lines.iterator(); it.hasNext(); ) {
            Object line = it.next();
            if (line != null) {
                output.write(line.toString().getBytes());
            }
            output.write(lineEnding.getBytes());
        }
    }

    public static void writeLines(Collection<Object> lines, String lineEnding,
                                  OutputStream output, String encoding) throws IOException {
        Iterator<Object> it;
        if (encoding == null) {
            writeLines(lines, lineEnding, output);
        } else {
            if (lines == null) {
                return;
            }
            if (lineEnding == null) {
                lineEnding = LINE_SEPARATOR;
            }
            for (it = lines.iterator(); it.hasNext(); ) {
                Object line = it.next();
                if (line != null) {
                    output.write(line.toString().getBytes(encoding));
                }
                output.write(lineEnding.getBytes(encoding));
            }
        }
    }

    public static void writeLines(Collection lines, String lineEnding,
                                  Writer writer) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = LINE_SEPARATOR;
        }
        for (Iterator it = lines.iterator(); it.hasNext(); ) {
            Object line = it.next();
            if (line != null) {
                writer.write(line.toString());
            }
            writer.write(lineEnding);
        }
    }

    public static int copy(InputStream input, OutputStream output)
            throws IOException {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void copy(InputStream input, Writer output)
            throws IOException {
        InputStreamReader in = new InputStreamReader(input);
        copy(in, output);
    }

    public static void copy(InputStream input, Writer output, String encoding)
            throws IOException {
        if (encoding == null) {
            copy(input, output);
        } else {
            InputStreamReader in = new InputStreamReader(input, encoding);
            copy(in, output);
        }
    }

    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(Reader input, Writer output)
            throws IOException {
        char[] buffer = new char[4096];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void copy(Reader input, OutputStream output)
            throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(output);
        copy(input, out);

        out.flush();
    }

    public static void copy(Reader input, OutputStream output, String encoding)
            throws IOException {
        if (encoding == null) {
            copy(input, output);
        } else {
            OutputStreamWriter out = new OutputStreamWriter(output, encoding);
            copy(input, out);

            out.flush();
        }
    }

    public static boolean contentEquals(InputStream input1, InputStream input2)
            throws IOException {
        if (!(input1 instanceof BufferedInputStream)) {
            input1 = new BufferedInputStream(input1);
        }
        if (!(input2 instanceof BufferedInputStream)) {
            input2 = new BufferedInputStream(input2);
        }

        int ch = input1.read();
        while (-1 != ch) {
            int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        int ch2 = input2.read();
        return (ch2 == -1);
    }

    public static boolean contentEquals(Reader input1, Reader input2)
            throws IOException {
        if (!(input1 instanceof BufferedReader)) {
            input1 = new BufferedReader(input1);
        }
        if (!(input2 instanceof BufferedReader)) {
            input2 = new BufferedReader(input2);
        }

        int ch = input1.read();
        while (-1 != ch) {
            int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        int ch2 = input2.read();
        return (ch2 == -1);
    }
}
/**
 * StringBufferInputStream(String) 据指定串创建一个读取数据的输入流串。
 * <p/>
 * (A) ByteArrayInputStream(byte[]) 创建一个新字节数组输入流
 * <p/>
 * (B) ByteArrayInputStream(byte[], int, int) 创建一个新字节数组输入流，它从指定字节数组中读取数据。
 * <p/>
 * (A)FileInputStream(File name) 创建一个输入文件流
 * <p/>
 * ---read(byte[])，从指定的 File 对象读取数据。将当前输入流中 b.length 个字节数据读到一个字节数组中。
 * <p/>
 * ---read(byte[], int, int) 将输入流中 len 个字节数据读入一个字节数组中。
 * <p/>
 * (B)FileInputStream(String name) 创建一个输入文件流，从指定名称的文件读取数据。
 */
