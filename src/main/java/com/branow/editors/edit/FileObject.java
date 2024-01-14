package com.branow.editors.edit;

import com.branow.editors.managers.FileManagerException;
import com.branow.editors.serialization.DTO;
import com.branow.editors.serialization.Serialization;
import com.branow.editors.serialization.SerializationException;
import com.branow.editors.serialization.TegSerialization;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public abstract class FileObject<T> extends FileText {

    protected final Serialization<DTO<T>> serialization;

    public FileObject(Path path) throws IOException {
        super(path);
        serialization = new TegSerialization<>(file);
    }

    public FileObject(String path) throws IOException {
        super(path);
        serialization = new TegSerialization<>(file);
    }

    public FileObject(File file) throws IOException {
        super(file);
        serialization = new TegSerialization<>(file);
    }


    public T readObject() {
        try {
            return deserializate();
        } catch (SerializationException e) {
            throw new FileManagerException(e);
        }
    }

    public void writeObject(T object)
    {
        try {
            serializate(object);
        } catch (SerializationException e) {
            throw new FileManagerException(e);
        }
    }

    protected abstract void serializate(T object) throws SerializationException;

    protected T deserializate() throws SerializationException {
        return serialization.deserializate().form();
    }

}
