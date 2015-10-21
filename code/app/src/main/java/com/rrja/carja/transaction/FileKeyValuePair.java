package com.rrja.carja.transaction;

/**
 * Created by chongge on 15/7/7.
 */
public class FileKeyValuePair {

    private String name;
    private String fileName;
    private String filePath;

    public FileKeyValuePair(String name, String fileName, String filePath) {
        this.name = name;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
