package com.branow.editors.managers;

import java.util.List;
import java.util.Optional;

public interface ObjectManageable<T> {

    void add(T t);

    void addIfNotContains(T t);

    void addOrReplace(T t);

    T get(T t);

    List<T> getAll();

    Optional<T> getOptional(T t);

    void update(T t);

    void updateIfContains(T t);

    void remove(T t);

    void removeIfContains(T t);

}
