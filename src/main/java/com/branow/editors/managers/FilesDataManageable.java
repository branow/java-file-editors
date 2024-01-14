package com.branow.editors.managers;

import com.branow.editors.edit.FileName;

import java.util.List;
import java.util.Optional;

public interface FilesDataManageable<T> extends ObjectManageable<T> {

    T get(FileName name);

    List<FileName> getAllFileNames();

    Optional<T> getOptional(FileName name);

    void remove(FileName name);

    void removeIfContains(FileName name);

}