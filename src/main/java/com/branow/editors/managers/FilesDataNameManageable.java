package com.branow.editors.managers;

import java.util.List;
import java.util.Optional;

public interface FilesDataNameManageable<T> extends FilesDataManageable<T> {

    T get(String name);

    List<String> getAllNames();

    Optional<T> getOptional(String name);

    void remove(String name);

    void removeIfContains(String name);

}
