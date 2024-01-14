package com.branow.editors.managers;

import com.branow.editors.serialization.DTO;
import com.branow.editors.serialization.Serialization;
import com.branow.editors.serialization.SerializationException;

@FunctionalInterface
public interface Serializator<T> {

    void serializate(T object, Serialization<DTO<T>> serialization) throws SerializationException;

}
