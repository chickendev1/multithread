package com.checkendev.file;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

public class JsonFile implements IJsonFile {
    /**
     * Retrive a file with specified name
     */
    public Function<String, File> getFile = filename -> {
        ClassLoader classLoader = JsonFile.class
                .getClass()
                .getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());

        return file;
    };


    @Override
    public String parseJsonFile(String filename) {
        File file = getFile.apply(filename);
        return String.valueOf(file);
    }
}
