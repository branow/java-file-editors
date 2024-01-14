package com.branow.editors.managers;

import com.branow.editors.edit.Directory;
import com.branow.editors.edit.Extension;

import java.util.Arrays;

public class FilesBytesNameHashManager extends FilesDataNameManager.FilesBytesNameManager {

    protected FilesBytesNameHashManager(Directory root, Extension fileExtension) {
        super(root, fileExtension);
    }

    @Override
    public String getName(Byte[] bytes) {
        return Arrays.hashCode(bytes) + "";
    }
}
