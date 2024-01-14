package editors;

import com.branow.editors.edit.FilesContentEditor;
import com.branow.outfits.timer.Timer;
import com.branow.outfits.timer.TimerResult;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeEqualingFilesTest {

    public static final File f1 = new File("D:\\films\\1.mp4"),
            f2 = new File("D:\\films\\11.mp4"),
            f3 = new File("D:\\java-project\\java-file-editors\\src\\test\\java\\editors\\FilesEditorTest.java"),
            f4 = new File("D:\\Book\\pdr_2014.pdf");

    public static void main(String[] args) throws InterruptedException {
        Timer timer = Timer.timer(List.of(() -> al(1), () -> al(2)));
        TimerResult result = timer.start(3, 360, TimeUnit.SECONDS);
        System.out.println(result.toString(TimerResult.TimeScale.MICROSECONDS));
    }

    public static void al(int n) {
        try {
            switch (n) {
                case 1 -> equalsFile1(f1, f1);
                case 2 -> equalsFile2(f1, f1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean equalsFile1(File f1, File f2) throws IOException {
        return equalsFile(f1, f2, 1_000_000, 5, 2);
    }

    public static boolean equalsFile2(File f1, File f2) throws IOException {
        return equalsFile(f1, f2, 1_000_000, 5, 1);
    }



    public static boolean equalsFile(File f1, File f2, long sep, int num, double skip) throws IOException {
        if (f1.length() <= sep && f2.length() <= sep)
            return equalsOnceFile(f1, f2);
        return equalsGraduallyFile(f1, f2, num, skip);
    }

    private static boolean equalsGraduallyFile(File f1, File f2, int num, double skip) throws IOException {
        try (BufferedInputStream br1 = new BufferedInputStream(new FileInputStream(f1));
             BufferedInputStream br2 = new BufferedInputStream(new FileInputStream(f2))) {
            if (br1.available() != br2.available()) return false;
            num = br1.available()/num;
            skip = num/skip;
            while (br1.available() > 0 || br2.available() > 0) {
                int skipCurrent = Math.min(br1.available(), (int) skip);
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
        return Arrays.equals(FilesContentEditor.readByte(f1), FilesContentEditor.readByte(f2));
    }


}
