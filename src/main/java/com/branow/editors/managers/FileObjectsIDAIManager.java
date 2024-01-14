package com.branow.editors.managers;

import com.branow.editors.edit.FileObject;

import java.util.List;

public abstract class FileObjectsIDAIManager<T extends ContainerID<Integer>> extends FileObjectsIDManager<Integer, T>{

    public FileObjectsIDAIManager(FileObject<List<T>> file) {
        super(file);
    }

    public T create() {
        int max = getAllId().stream().mapToInt(e -> e).max().orElse(0);
        return create(++max);
    }
}
