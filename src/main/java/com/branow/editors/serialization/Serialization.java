package com.branow.editors.serialization;

import java.io.File;
import java.io.IOException;

import com.branow.editors.edit.FileText;

public interface Serialization<T> {

    static<T> Serialization<T> serializationXML(String name) throws IOException {
        return new TegSerialization<>(new FileText(name + ".xml"));
    }

    static<T> Serialization<T> serializationXML(String path, String name) throws IOException {
        return new TegSerialization<>(new FileText(path + File.separator + name + ".xml"));
    }

    void serializate(T object) throws SerializationException;

    T deserializate() throws SerializationException;

}
