package com.branow.editors.managers;

import com.branow.editors.edit.FileName;

import java.util.List;
import java.util.Optional;

public interface DirectoriesDataManageable<T> {

    T create(FileName name);

    T create(String name);

    void create(T t);

    T get(FileName name);

    Optional<T> getOptional(FileName name);

    List<T> getAll();

    List<FileName> getAllFileNames();

    T get(String name);

    List<String> getAllNames();

    Optional<T> getOptional(String name);


    void rename(String oldName, String newName);


    void remove(T t);

    void removeIfContains(T t);

    void remove(FileName name);

    void removeIfContains(FileName name);

    void remove(String name);

    void removeIfContains(String name);

}
