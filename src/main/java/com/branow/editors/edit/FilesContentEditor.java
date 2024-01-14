package com.branow.editors.edit;

import java.io.*;
import java.util.Arrays;
import java.nio.file.Path;

public class FilesContentEditor {

    public static String read(File file) throws IOException  {
        return read(file.getAbsolutePath());
    }

    public static String read(String path) throws IOException  {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String s;
            while ((s = br.readLine()) != null ) {
                if (!sb.isEmpty()) sb.append("\n");
                sb.append(s);
            }
        }
        return sb.toString();
    }

    public static byte[] readByte(File file) throws IOException  {
        return readByte(file.getAbsolutePath());
    }

    public static byte[] readByte(String file) throws IOException  {
        try (BufferedInputStream br = new BufferedInputStream(new FileInputStream(file))) {
            return br.readAllBytes();
        }
    }


    public static void write(String text, File file) throws IOException  {
        write(text, file, false);
    }

    public static void write(String text, String file) throws IOException  {
        write(text, file, false);
    }

    public static void write(byte[] bytes, File file) throws IOException  {
        write(bytes, file, false);
    }

    public static void write(byte[] bytes, String file) throws IOException  {
        write(bytes, file, false);
    }


    public static void append(String text, File file) throws IOException  {
        write(text, file, true);
    }

    public static void append(String text, String file) throws IOException  {
        write(text, file, true);
    }

    public static void append(byte[] bytes, File file) throws IOException  {
        write(bytes, file, true);
    }

    public static void append(byte[] bytes, String file) throws IOException  {
        write(bytes, file, true);
    }


    public static void write(String text, File file, boolean append) throws IOException  {
        write(text, file.getAbsolutePath(), append);
    }

    public static void write(String text, String file, boolean append) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, append))) {
            bw.write(text);
        }
    }

    public static void write(byte[] bytes, File file, boolean append) throws IOException {
        write(bytes, file.getAbsolutePath(), append);
    }

    public static void write(byte[] bytes, String file, boolean append) throws IOException {
        try (BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream(file, append))) {
            bf.write(bytes);
        }
    }


    public static boolean equalsFile(Path f1, Path f2) throws IOException {
        return equalsFile(f1.toFile(), f2.toFile());
    }

    public static boolean equalsFile(File f1, File f2) throws IOException {
        if (f1.length() != f2.length()) return false;
        if (f1.length() <= 1_000_000 && f2.length() <= 1_000_000)
            return equalsOnceFile(f1, f2);
        return equalsGraduallyFile(f1, f2);
    }

    private static boolean equalsGraduallyFile(File f1, File f2) throws IOException {
        try (BufferedInputStream br1 = new BufferedInputStream(new FileInputStream(f1));
             BufferedInputStream br2 = new BufferedInputStream(new FileInputStream(f2))) {
            if (br1.available() != br2.available()) return false;
            int num = br1.available() / 5;
            while (br1.available() > 0 || br2.available() > 0) {
                int skipCurrent = Math.min(br1.available(), num);
                br1.skipNBytes(skipCurrent);
                br2.skipNBytes(skipCurrent);
                int numCurrent = Math.min(num, br1.available());
                if (!Arrays.equals(br1.readNBytes(numCurrent), br2.readNBytes(numCurrent)))
                    return false;
            }
            return true;
        }
    }

    private static boolean equalsOnceFile(File f1, File f2) throws IOException {
        return Arrays.equals(readByte(f1), readByte(f2));
    }

}
