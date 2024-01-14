package com.branow.editors.edit;

import com.branow.outfits.checker.ParametersChecker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

import static java.io.File.separator;

public class FilesEditor {

    private static final String COPY_SUFFIX = "-copy";
    private static final Pattern filePattern = Pattern.compile(".+\\..+");

    public static void create(String path) throws IOException {
        create(Path.of(path));
    }

    public static void create(File file) throws IOException {
        if (!file.createNewFile())
            throw new IOException("cannot create new file");
    }

    public static void create(Path path) throws IOException {
        Files.createDirectories(path.getParent());
        if (filePattern.matcher(path.getFileName().toString()).matches())
            Files.createFile(path);
        else
            Files.createDirectory(path);
    }


    public static void createIfNotExist(String path) throws IOException {
        createIfNotExist(Path.of(path));
    }

    public static void createIfNotExist(File file) throws IOException {
        if (!file.exists() && !file.createNewFile())
            throw new IOException("cannot create new file");
    }

    public static void createIfNotExist(Path path) throws IOException {
        if (!Files.exists(path)) create(path);
    }


    public static void delete(String path) throws IOException {
        delete(Path.of(path));
    }

    public static void delete(File file) throws IOException {
        delete(file.toPath());
    }

    public static void delete(Path path) throws IOException {
        if (!Files.isDirectory(path)) {
            Files.delete(path);
            return;
        }
        for (Path p: getChildren(path))
            delete(p);
        Files.delete(path);
    }


    public static void deleteIfExist(String path) throws IOException {
        deleteIfExist(Path.of(path));
    }

    public static void deleteIfExist(File file) throws IOException {
        deleteIfExist(file.toPath());
    }

    public static void deleteIfExist(Path path) throws IOException {
        if (Files.exists(path)) delete(path);
    }


    public static void move(String pathScr, String pathNewRoot) throws IOException {
        move(Path.of(pathScr), Path.of(pathNewRoot));
    }

    public static void move(File scr, File newRoot) throws IOException {
        move(scr.toPath(), newRoot.toPath());
    }

    public static void move(Path scr, Path newRoot) throws IOException {
        if (!Files.isDirectory(newRoot)) throw new IOException("newRoot is not directory");
        Path dest = Path.of(newRoot.toAbsolutePath().toString(), scr.getFileName().toString());
        Files.move(scr, dest);
    }


    public static void rename(Path path, String newSimpleName) throws IOException {
        rename(path.toFile(), newSimpleName);
    }

    public static void rename(File file, String newSimpleName) throws IOException {
        String path = file.getAbsolutePath();
        int s = path.lastIndexOf(separator);
        int f = path.lastIndexOf(".");
        String newPath = path.substring(0, s+1) + newSimpleName + path.substring(f);
        if (!file.renameTo(new File(newPath)))
            throw new IOException("Cannot rename file - " + file.getAbsolutePath() + " to - " + newPath);
    }

    /**Copying file or directory (without content) to target directory.
     * If there is file with same name, copy file name will be change
     * by adding  suffix '-copy'*/
    public static Path copy(String pathSrc, String pathTargetDir) throws IOException {
        return copy(Path.of(pathSrc), Path.of(pathTargetDir));
    }

    public static Path copy(File src, File targetDir) throws IOException {
        return copy(src.toPath(), targetDir.toPath());
    }

    public static Path copy(Path src, Path targetDir) throws IOException {
        ParametersChecker.isTrueThrow(targetDir, FilesEditor::isFile, "target is not directory" + targetDir);
        Path target = Path.of(targetDir.toAbsolutePath().toString(), src.getFileName().toString());
        if (Files.exists(target)) {
            if (FilesEditor.isDirectory(target)) {
                target = Path.of(targetDir.toAbsolutePath().toString(), target.getFileName() + COPY_SUFFIX);
            } else {
                String[] name = src.getFileName().toString().split("\\.");
                ParametersChecker.isTrueThrow(name.length != 2, "srс file is incorrect " + src);
                target = Path.of(targetDir.toAbsolutePath().toString(), name[0] + COPY_SUFFIX + "." + name[1]);
            }
        }
        Files.copy(src, target);
        return target;
    }

    /**Copying file or directory (with all content) to target directory.
     * If there is file with same name, copy file name will be change
     * by adding  suffix '-copy'*/
    public static Path copyFull(String src, String targetDir) throws IOException {
        return copyFull(Path.of(src), Path.of(targetDir));
    }

    public static Path copyFull(File src, File targetDir) throws IOException {
        return copyFull(src.toPath(), targetDir.toPath());
    }

    public static Path copyFull(Path src, Path targetDir) throws IOException {
        if (FilesEditor.isFile(src)) return copy(src, targetDir);
        ParametersChecker.isTrueThrow(targetDir, FilesEditor::isFile, "target is not directory" + targetDir);
        Path target = copy(src, targetDir);
        for (Path path: getChildren(src)) {
            copyFull(path, target);
        }
        return target;
    }

    /**Copying file or directory (without content) to target directory.
     * If there is file with same name but with other content,
     * that file will be replaced*/
    public static Path copyWithReplacement(String pathSrc, String pathTargetDir) throws IOException {
        return copyWithReplacement(Path.of(pathSrc), Path.of(pathTargetDir));
    }

    public static Path copyWithReplacement(File src, File targetDir) throws IOException {
        return copyWithReplacement(src.toPath(), targetDir.toPath());
    }

    public static Path copyWithReplacement(Path src, Path targetDir) throws IOException {
        ParametersChecker.isTrueThrow(targetDir, FilesEditor::isFile, "target is not directory" + targetDir);
        Path target = Path.of(targetDir.toAbsolutePath().toString(), src.getFileName().toString());
        if (exist(target)) {
            if (FilesEditor.isFile(target) && !FilesContentEditor.equalsFile(src, target)) {
                FilesEditor.delete(target);
                Files.copy(src, target);
            }
        } else {
            Files.copy(src, target);
        }
        return target;
    }

    /**Copying file or directory (without all content) to target directory.
     * If there is file with same name but with other content,
     * that file will be replaced*/
    public static Path copyFullWithReplacement(String src, String targetDir) throws IOException {
        return copyFullWithReplacement(Path.of(src), Path.of(targetDir));
    }

    public static Path copyFullWithReplacement(File src, File targetDir) throws IOException {
        return copyFullWithReplacement(src.toPath(), targetDir.toPath());
    }

    public static Path copyFullWithReplacement(Path src, Path targetDir) throws IOException {
        Path target = copyWithReplacement(src, targetDir);
        if (FilesEditor.isFile(src)) return target;
        deleteExcessiveFiles(getChildren(src), getChildren(target));
        for (Path path: getChildren(src)) {
            copyFullWithReplacement(path, target);
        }
        return target;
    }


    public static void deleteExcessiveFiles(List<Path> src, List<Path> targets) {
        targets.forEach(target -> {
            if (src.stream().noneMatch(e -> e.getFileName().equals(target.getFileName()))) {
                try {
                    delete(target);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static List<Path> goInWidth(Path root) {
        return goInWidth(new LinkedList<>(List.of(root)));
    }

    private static List<Path> goInWidth(LinkedList<Path> next) {
        List<Path> bypassed = new ArrayList<>();
        if (next.isEmpty()) return bypassed;
        while (!next.isEmpty()) {
            Path temp = next.pollFirst();
            bypassed.add(temp);
            next.addAll(getChildren(temp));
        }
        return bypassed;
    }

    public static List<Path> goInDeep(Path root) {
        return goInDeep(root, new ArrayList<>());
    }

    private static List<Path> goInDeep(Path root, List<Path> bypassed) {
        bypassed.add(root);
        for (Path path: getChildren(root)) {
            goInDeep(path, bypassed);
        }
        return bypassed;
    }


    public static List<Path> getChildren(String path) {
        return getChildren(new File(path));
    }

    public static List<Path> getChildren(Path path) {
        return getChildren(path.toFile());
    }

    public static List<Path> getChildren(File file) {
        File[] files = file.listFiles();
        return files != null ? Arrays.stream(files).map(File::toPath).collect(Collectors.toList()) : new ArrayList<>();
    }


    public static String getFileNameWithoutExtension(Path path) {
        String name = path.getFileName().toString();
        List<String> names = Arrays.stream(name.split("\\.")).collect(Collectors.toList());
        names = names.subList(0, names.size() - 1);
        return String.join("", names);
    }


    public static boolean exist(String path) {
        return exist(Path.of(path));
    }

    public static boolean exist(File path) {
        return path.exists();
    }

    public static boolean exist(Path path) {
        return Files.exists(path);
    }


    public static boolean isDirectory(String path) {
        return !filePattern.matcher(path).matches();
    }

    public static boolean isDirectory(File path) {
        return isDirectory(path.getName());
    }

    public static boolean isDirectory(Path path) {
        return isDirectory(path.getFileName().toString());
    }


    public static boolean isFile(String path) {
        return filePattern.matcher(path).matches();
    }

    public static boolean isFile(File path) {
        return isFile(path.getName());
    }

    public static boolean isFile(Path path) {
        return isFile(path.getFileName().toString());
    }


}
