package com.branow.editors.edit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.branow.outfits.catcher.Catcher;
import com.branow.outfits.checker.ParametersChecker;

public class Directory {

    protected final Path dir;

    public Directory(File dir) throws IOException {
        this(dir.toPath());
    }

    public Directory(String pathDir) throws IOException {
        this(new File(pathDir));
    }

    public Directory(Path pathDir) throws IOException {
        ParametersChecker.isTrueThrow(FilesEditor.isFile(pathDir), "file is not directory");
        FilesEditor.createIfNotExist(pathDir);
        this.dir = pathDir;
    }


    public void rename(String newName) {
        Catcher.interceptAndThrow(() -> FilesEditor.rename(dir, newName), FileOperationException.class);
    }


    public List<FileName> getChildrenNames() {
        return FilesEditor.getChildren(dir).stream().map(FileName::parse).collect(Collectors.toList());
    }

    public List<Path> getChildren() {
        return FilesEditor.getChildren(dir).stream().map(Path::getFileName).collect(Collectors.toList());
    }

    public Path get(FileName name) {
        return getChildren().stream().filter(e -> FileName.parse(e).equals(name)).findAny().orElse(null);
    }

    public Path getDir() {
        return dir;
    }

    public FileName getFileName() {
        return new FileName(dir);
    }

    public String getName() {
        return new FileName(dir).getName();
    }

    public void add(Path path) {
        Catcher.interceptAndThrow(() -> FilesEditor.copyFull(path, dir), FileOperationException.class);
    }

    public void addAll(List<Path> paths) {
        paths.forEach(this::add);
    }


    public void remove(Path path) {
        Optional<Path> find = getChildren().stream().filter(e -> e.toAbsolutePath().equals(path.toAbsolutePath())).findAny();
        find.ifPresent(value -> Catcher.interceptAndThrow(() -> FilesEditor.deleteIfExist(value), FileOperationException.class));
    }

    public void remove(FileName name) {
        Optional<Path> find = getChildren().stream().filter(e -> FileName.parse(e).equals(name)).findAny();
        find.ifPresent(value -> Catcher.interceptAndThrow(() -> FilesEditor.deleteIfExist(value),
                FileOperationException.class));
    }

    public void removeIfContains(FileName name) {
        if (get(name) != null)
            remove(name);
    }

    public void removeAll(List<Path> paths) {
        paths.forEach(this::remove);
    }

    public void removeAll() {
        getChildren().forEach(e -> Catcher.interceptAndThrow(() -> FilesEditor.deleteIfExist(e), FileOperationException.class));
    }

    @Override
    public String toString() {
        return dir.toAbsolutePath().toString();
    }
}
