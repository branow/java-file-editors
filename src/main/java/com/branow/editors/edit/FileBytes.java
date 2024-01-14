package com.branow.editors.edit;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileBytes {

    protected final File file;

    public FileBytes(File file) throws IOException {
        throwIfDirectory(file);
        FilesEditor.createIfNotExist(file);
        this.file = file;

    }

    public FileBytes(String path) throws IOException {
        this(new File(path));
    }

    public FileBytes(Path path) throws IOException {
        this(path.toFile());
    }

    public void rename(String newSimpleName) {
        try {
            FilesEditor.rename(file, newSimpleName);
        } catch (IOException e) {
            throw new FileOperationException(e);
        }
    }

    public byte[] readBytes() {
        try {
            return FilesContentEditor.readByte(file);
        } catch (IOException e) {
            throw new FileOperationException(e);
        }
    }

    public void writeBytes(byte[] bytes) {
        try {
            FilesContentEditor.write(bytes, file, false);
        } catch (IOException e) {
            throw new FileOperationException(e);
        }
    }

    public void appendBytes(byte[] bytes) {
        try {
            FilesContentEditor.write(bytes, file, true);
        } catch (IOException e) {
            throw new FileOperationException(e);
        }
    }

    public long size() {
        try {
            return Files.size(file.toPath());
        } catch (IOException e) {
            throw new FileOperationException();
        }
    }

    public long length() {
        return file.length();
    }

    public void clean() {
        writeBytes(new byte[0]);
    }

    public boolean isZero() {
        return file.length() == 0;
    }

    public File getFile() {
        return file;
    }

    public Path getPath() {
        return file.toPath();
    }

    public String toAbsolutePath() {
        return file.getAbsolutePath();
    }

    @Override
    public String toString() {
        return toAbsolutePath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileBytes fileBytes = (FileBytes) o;
        return Objects.equals(file, fileBytes.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }

    private void throwIfDirectory (File file) {
        if (file.isDirectory()) throw new IllegalArgumentException("File is Directory");
    }

}
