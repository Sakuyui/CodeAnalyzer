package com.akb.codeanlyzer.service;

public interface IFileSystemService {
    String readFileAsString(String path);
    void writeStringToFile(String path, String str);
}
