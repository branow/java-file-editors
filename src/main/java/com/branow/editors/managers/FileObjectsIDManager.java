package com.branow.editors.managers;

import com.branow.editors.edit.FileObject;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class FileObjectsIDManager<I, T extends ContainerID<I>> extends FileObjectsManager<T>{


    public FileObjectsIDManager(FileObject<List<T>> file) {
        super(file, (e1, e2) -> Objects.equals(e1.getId(), e2.getId()));
    }


    public T create(I id) {
        T t = createObject(id);
        add(t);
        return t;
    }

    public T createIfNotContains(I id) {
        T t = createObject(id);
        addIfNotContains(t);
        return t;
    }

    public T getOfId(I id) {
        return get(createObject(id));
    }

    public List<I> getAllId() {
        return getAll().stream().map(ContainerID::getId).collect(Collectors.toList());
    }

    public Optional<T> getOptionalOfId(I id) {
        return getOptional(createObject(id));
    }


    public void removeOfId(I id) {
        remove(createObject(id));
    }

    public void removeOfIdIfContains(I id) {
        removeIfContains(createObject(id));
    }


    protected abstract T createObject(I id);

}
