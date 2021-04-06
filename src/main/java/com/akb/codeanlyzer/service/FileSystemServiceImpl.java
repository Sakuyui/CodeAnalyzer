package com.akb.codeanlyzer.service;

import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FileSystemServiceImpl implements IFileSystemService{

    @Override
    public String readFileAsString(String path){
        File f = new File(path);
        try{
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);

            return  new String(bis.readAllBytes());
        }catch (IOException e){
            return "";
        }

    }
}
