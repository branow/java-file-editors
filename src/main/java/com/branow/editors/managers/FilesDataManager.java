package com.branow.editors.managers;

import com.branow.editors.edit.Directory;
import com.branow.editors.edit.FileName;
import com.branow.editors.edit.FilesEditor;
import com.branow.outfits.checker.ParametersChecker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class FilesDataManager<T> implements FilesDataManageable<T> {


    public static abstract class FilesObjectsManager<T> extends FilesDataManager<T> {
        protected FilesObjectsManager(Directory directory, Serializator<T> serializator) {
            super(directory, new Writer.ObjectWriter<>(serializator));
        }
    }

    public static abstract class FilesBytesManager extends FilesDataManager<Byte[]> {
        public FilesBytesManager(Directory directory) {
            super(directory, new Writer.BytesWriter());
        }
    }

    private final Directory directory;
    private final Writer<T> writer;

    public FilesDataManager(Directory directory, Writer<T> writer) {
        this.directory = directory;
        this.writer = writer;
    }

    @Override
    public void add(T t) {
        ParametersChecker.isTrueThrow(getOptional(getFileName(t)).isPresent(), "The object is already contained");
        writer.write(getPath(t), t);
    }

    @Override
    public void addIfNotContains(T t) {
        if (FilesEditor.exist(getPath(t))) return;
        writer.write(getPath(t), t);
    }

    @Override
    public void addOrReplace(T t) {
        writer.write(getPath(t), t);
    }

    @Override
    public T get(T t) {
        return get(getFileName(t));
    }

    @Override
    public T get(FileName name) {
        return getOptional(name).orElseThrow();
    }

    @Override
    public List<T> getAll() {
        return getAllFileNames().stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<FileName> getAllFileNames() {
        return directory.getChildrenNames();
    }

    @Override
    public Optional<T> getOptional(T t) {
        return getOptional(getFileName(t));
    }

    @Override
    public Optional<T> getOptional(FileName name) {
        if (!FilesEditor.exist(getPath(name)))
            return Optional.empty();
        return Optional.of(writer.read(getPath(name)));
    }

    @Override
    public void update(T t) {
        ParametersChecker.isTrueThrow(!FilesEditor.exist(getPath(t)), "Object isn't contained");
        writer.write(getPath(t), t);
    }

    @Override
    public void updateIfContains(T t) {
        if (!FilesEditor.exist(getPath(t))) return;
        writer.write(getPath(t), t);
    }

    @Override
    public void remove(T t) {
        remove(getFileName(t));
    }

    @Override
    public void removeIfContains(T t) {
        removeIfContains(getFileName(t));
    }

    @Override
    public void remove(FileName name) {
        try {
            FilesEditor.delete(getPath(name));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void removeIfContains(FileName name) {
        try {
            FilesEditor.deleteIfExist(getPath(name));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public abstract FileName getFileName(T t);

    public Path getPath(T t) {
        return Path.of(directory + File.separator + getFileName(t));
    }

    public Path getPath(FileName name) {
        return Path.of(directory + File.separator + name);
    }

}