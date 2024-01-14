package editors;

import com.branow.editors.edit.FilesEditor;
import com.branow.outfits.timer.Timer;
import com.branow.outfits.timer.TimerResult;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeWalkingFilesTest {

    public static final Path f1 = Path.of("D:\\java-project");

    public static void main(String[] args) throws InterruptedException {
        Timer timer = Timer.timer(List.of(() -> al(1), () -> al(2)));
        TimerResult result = timer.start(10, 60, TimeUnit.SECONDS);
        System.out.println(result.toString(TimerResult.TimeScale.MICROSECONDS));
    }

    public static void al(int n) {
        switch (n) {
            case 1 -> FilesEditor.goInDeep(f1);
            case 2 -> FilesEditor.goInWidth(f1).size();
        }
    }



}
