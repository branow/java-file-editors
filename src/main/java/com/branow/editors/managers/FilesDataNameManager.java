package com.branow.editors.managers;

import com.branow.editors.edit.Directory;
import com.branow.editors.edit.Extension;
import com.branow.editors.edit.FileName;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class FilesDataNameManager<T> extends FilesDataManager<T> implements FilesDataNameManageable<T> {

    public static abstract class FilesObjectsNameManager<T> extends FilesDataNameManager<T> {
        protected FilesObjectsNameManager(Directory directory, Serializator<T> serializator, Extension extension) {
            super(directory, new Writer.ObjectWriter<>(serializator), extension);
        }
    }

    public static abstract class FilesBytesNameManager extends FilesDataNameManager<Byte[]> {
        public FilesBytesNameManager(Directory directory, Extension extension) {
            super(directory, new Writer.BytesWriter(), extension);
        }
    }

    private final Extension extension;

    protected FilesDataNameManager(Directory directory, Writer<T> writer, Extension extension) {
        super(directory, writer);
        this.extension = extension;
    }

    @Override
    public T get(String name) {
        return get(getFileName(name));
    }

    @Override
    public List<String> getAllNames() {
        return getAllFileNames().stream().map(FileName::getName).collect(Collectors.toList());
    }

    @Override
    public Optional<T> getOptional(String name) {
        return getOptional(getFileName(name));
    }

    @Override
    public void remove(String name) {
        remove(getFileName(name));
    }

    @Override
    public void removeIfContains(String name) {
        removeIfContains(getFileName(name));
    }


    @Override
    public FileName getFileName(T t) {
        return new FileName(getName(t), extension);
    }

    public FileName getFileName(String name) {
        return new FileName(name, extension);
    }

    public abstract String getName(T t);
}
