package com.branow.editors.managers;

import com.branow.editors.edit.Directory;
import com.branow.editors.edit.FileName;
import com.branow.editors.edit.FilesEditor;
import com.branow.outfits.catcher.Catcher;
import com.branow.outfits.checker.ParametersChecker;

import java.io.File;
import java.lang.reflect.Parameter;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class DirectoriesObjectManager<T> implements DirectoriesDataManageable<T> {

    private final Directory dir;

    public DirectoriesObjectManager(Directory dir) {
        this.dir = dir;
    }

    @Override
    public T create(FileName name) {
        return create(name.getName());
    }

    @Override
    public T create(String name) {
        ParametersChecker.isTrueThrow(getOptional(name).isPresent(), "same directory already exist");
        Directory item = Catcher.interceptAndThrow(() -> new Directory(getPath(name)), FileManagerException.class);
        return create(item);
    }

    @Override
    public void create(T t) {
        create(getName(t));
    }

    @Override
    public T get(FileName name) {
        Optional<T> o = getOptional(name);
        ParametersChecker.isTrueThrow(o.isEmpty(), "there is not such file");
        return create(name);
    }

    @Override
    public T get(String name) {
        return get(new FileName(name));
    }

    @Override
    public Optional<T> getOptional(String name) {
        return getOptional(new FileName(name));
    }

    @Override
    public Optional<T> getOptional(FileName name) {
        Path path = dir.get(name);
        if (path == null) return Optional.empty();
        return Optional.of(create(name));
    }


    @Override
    public List<String> getAllNames() {
        return getAllFileNames().stream().map(FileName::getName).collect(Collectors.toList());
    }

    @Override
    public List<T> getAll() {
        return dir.getChildrenNames().stream().map(this::create).collect(Collectors.toList());
    }

    @Override
    public List<FileName> getAllFileNames() {
        return dir.getChildrenNames();
    }

    @Override
    public void rename(String oldName, String newName) {
        T t = getOptional(oldName).orElseThrow();
        Catcher.interceptAndThrow(() -> FilesEditor.rename(getPath(t), newName), FileManagerException.class);
    }

    @Override
    public void remove(T t) {
        remove(getName(t));
    }

    @Override
    public void removeIfContains(T t) {
        removeIfContains(getName(t));
    }

    @Override
    public void remove(FileName name) {
        dir.remove(name);
    }

    @Override
    public void removeIfContains(FileName name) {
        dir.removeIfContains(name);
    }

    @Override
    public void remove(String name) {
        remove(new FileName(name));
    }

    @Override
    public void removeIfContains(String name) {
        removeIfContains(new FileName(name));
    }


    protected abstract FileName getName(T t);

    protected abstract T create(Directory directory);


    protected Path getPath(T t) {
        return Path.of(dir.toString() + File.separator + getName(t));
    }

    protected Path getPath(FileName fileName) {
        return Path.of(dir.toString() + File.separator + fileName);
    }

    protected Path getPath(String name) {
        return getPath(new FileName(name));
    }


}
