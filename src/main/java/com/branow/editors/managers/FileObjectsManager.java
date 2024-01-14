package com.branow.editors.managers;

import com.branow.editors.edit.FileObject;
import com.branow.editors.serialization.DTO;
import com.branow.editors.serialization.Serialization;
import com.branow.editors.serialization.SerializationException;
import com.branow.editors.serialization.TegSerialization;
import com.branow.outfits.checker.ParametersChecker;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

public class FileObjectsManager<T> implements ObjectManageable<T> {

    private final FileObject<List<T>> file;
    private BiPredicate<T, T> equals;

    public FileObjectsManager(FileObject<List<T>> file) {
        this(file, Objects::equals);
    }

    public FileObjectsManager(FileObject<List<T>> file, BiPredicate<T, T> equals) {
        this.file = file;
        this.equals = equals;
    }


    public BiPredicate<T, T> getEquals() {
        return equals;
    }

    public void setEquals(BiPredicate<T, T> equals) {
        this.equals = equals;
    }

    public FileObject<List<T>> getFile() {
        return file;
    }

    @Override
    public void add(T t) {
        ParametersChecker.isTrueThrow(getOptional(t).isPresent(), "The object is already contained");
        addObject(t);
    }

    @Override
    public void addIfNotContains(T t) {
        if (getOptional(t).isPresent()) return;
        addObject(t);
    }

    @Override
    public void addOrReplace(T t) {
        if (getOptional(t).isPresent())
            update(t);
        else
            addObject(t);
    }

    @Override
    public T get(T t) {
        return getOptional(t).orElseThrow();
    }

    @Override
    public List<T> getAll() {
        return file.readObject();
    }

    @Override
    public Optional<T> getOptional(T t) {
        return getOptional(t, file.readObject());
    }

    @Override
    public void update(T t) {
        List<T> list = file.readObject();
        Optional<T> optional = getOptional(t, list);
        ParametersChecker.isTrueThrow(optional.isEmpty(), "Object isn't contained");
        list.remove(optional.get());
        list.add(t);
        file.writeObject(list);
    }

    @Override
    public void updateIfContains(T t) {
        List<T> list = file.readObject();
        Optional<T> optional = getOptional(t, list);
        if (optional.isEmpty()) return;
        list.remove(optional.get());
        list.add(t);
        file.writeObject(list);
    }

    @Override
    public void remove(T t) {
        List<T> list = file.readObject();
        Optional<T> optional = getOptional(t, list);
        ParametersChecker.isTrueThrow(optional.isEmpty(), "Object isn't contained");
        list.remove(optional.get());
        file.writeObject(list);
    }

    @Override
    public void removeIfContains(T t) {
        List<T> list = file.readObject();
        Optional<T> optional = getOptional(t, list);
        if (optional.isEmpty()) return;
        list.remove(optional.get());
        file.writeObject(list);
    }


    private void serializate(List<T> list) {
        file.writeObject(list);
    }

    private List<T> deserializate() {
        return file.readObject();
    }


    private void addObject(T t) {
        List<T> list = file.readObject();
        list.add(t);
        file.writeObject(list);
    }

    private Optional<T> getOptional(T t, List<T> list) {
        return list.stream().filter(e -> equals.test(e, t)).findAny();
    }


}
