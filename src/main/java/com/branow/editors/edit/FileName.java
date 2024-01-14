package com.branow.editors.edit;

import java.io.File;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

public class FileName {

    public static FileName parse(String path) {
        String[] parts = path.split(File.separator);
        return parseFileName(parts[parts.length - 1]);
    }

    public static FileName parse(Path path) {
        return parseFileName(path.getFileName().toString());
    }

    private static FileName parseFileName(String filename) {
        if (FilesEditor.isFile(filename)) {
            String[] data = filename.split("//.");
            return new FileName(data[0], Extension.valueOf(data[1].toUpperCase(Locale.ROOT)));
        } else {
            return new FileName(filename, null);
        }
    }

    private final String name;
    private final Extension extension;

    public FileName(String name, Extension extension) {
        this.name = name;
        this.extension = extension;
    }

    public FileName(String path) {
        FileName file = parse(path);
        this.name = file.name;
        this.extension = file.getExtension();
    }

    public FileName(Path path) {
        FileName file = parse(path);
        this.name = file.name;
        this.extension = file.getExtension();
    }

    public boolean isDirectory() {
        return extension == null;
    }

    public boolean isFile() {
        return extension != null;
    }

    public String getName() {
        return name;
    }

    public Extension getExtension() {
        return extension;
    }

    @Override
    public String toString() {
        return extension != null ? name + "." + extension : name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileName fileName = (FileName) o;
        return Objects.equals(name, fileName.name) && extension == fileName.extension;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, extension);
    }
}
