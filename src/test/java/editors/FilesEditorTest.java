package editors;

import com.branow.editors.edit.FilesEditor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class FilesEditorTest {

    @Test
    public void testCreate() throws IOException {
        String root = "src/test/java/editors/file-editor-test/creating";
        String path = root + "/some-dir/text.txt";
        FilesEditor.create(path);
        Assertions.assertTrue(new File(path).exists());
        FilesEditor.deleteIfExist(root);
    }

    @Test
    public void testDelete() throws IOException {
        String root = "src/test/java/editors/file-editor-test/deleting";
        String path = root + "/some-dir/text.txt";
        FilesEditor.createIfNotExist(path);
        Assertions.assertTrue(new File(path).exists());
        FilesEditor.deleteIfExist(root);
        Assertions.assertFalse(new File(path).exists());
        Assertions.assertFalse(new File(root).exists());
    }

    @Test
    public void testCopy() throws IOException {
        String root = "src/test/java/editors/file-editor-test/copying";
        String path = root + "/some-dir/text.txt";
        String copy = "src/test/java/editors/file-editor-test/copying/some-dir";
        String result = "src/test/java/editors/file-editor-test/copying/some-dir-copy/text.txt";

        FilesEditor.createIfNotExist(path);
        Assertions.assertTrue(FilesEditor.exist(root));
        Assertions.assertTrue(FilesEditor.exist(path));
        FilesEditor.copyFull(copy, root);
        Assertions.assertTrue(FilesEditor.exist(result));
    }

    public void testMove() {

    }

    public void testRename() {

    }

}
