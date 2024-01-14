package com.branow.editors.edit;

import java.io.*;
import java.nio.file.Path;

public class FileText extends FileBytes {

    public FileText(Path path) throws IOException  {
        super(path);
    }

    public FileText(String path) throws IOException  {
        super(path);
    }

    public FileText(File file) throws IOException {
        super(file);
    }


    public String read() {
        try {
            return FilesContentEditor.read(file);
        } catch (IOException e) {
            throw new FileOperationException();
        }
    }

    public void write(String text) {
        try {
            FilesContentEditor.write(text, file, false);
        } catch (IOException e) {
            throw new FileOperationException();
        }
    }


    public void append(String text) {
        try {
            FilesContentEditor.write(text, file, true);
        } catch (IOException e) {
            throw new FileOperationException();
        }
    }

    public boolean isBlank() {
        return read().isBlank();
    }

}
