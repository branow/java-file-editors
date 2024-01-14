package com.branow.editors.managers;

import com.branow.editors.edit.FileBytes;
import com.branow.editors.edit.FileObject;
import com.branow.editors.serialization.SerializationException;
import com.branow.outfits.catcher.Catcher;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Path;

public interface Writer<T> {

    void write(Path path, T t);

    T read(Path path);

    class ObjectWriter<T> implements Writer<T> {

        private final Serializator<T> serializate;

        public ObjectWriter(Serializator<T> serializate) {
            this.serializate = serializate;
        }

        @Override
        public void write(Path path, T t) {
            getFileObject(path).writeObject(t);
        }

        @Override
        public T read(Path path) {
            return getFileObject(path).readObject();
        }


        public FileObject<T> getFileObject(Path path) {
            try {
                return new FileObject<T>(path) {
                    @Override
                    protected void serializate(T object) throws SerializationException {
                        serializate.serializate(object, serialization);
                    }
                };
            } catch (IOException e) {
                throw new FileManagerException(e);
            }
        }

    }

    class BytesWriter implements Writer<Byte[]> {

        @Override
        public void write(Path path, Byte[] bytes) {
            getFileBytes(path).writeBytes(ArrayUtils.toPrimitive(bytes));
        }

        @Override
        public Byte[] read(Path path) {
            return ArrayUtils.toObject(getFileBytes(path).readBytes());
        }

        private FileBytes getFileBytes(Path path) {
            return Catcher.interceptAndThrow(() -> new FileBytes(path), FileManagerException.class);
        }


    }

}

