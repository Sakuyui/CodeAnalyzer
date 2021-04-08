package com.akb.codeanlyzer.service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileSystemServiceImpl implements IFileSystemService{

    @Override
    public String readFileAsString(String path){
        File f = new File(path);
        try{
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            String s = new String(bis.readAllBytes());
            bis.close();
            fis.close();
            return  s;
        }catch (IOException e){
            return "";
        }

    }

    @Override
    public void writeStringToFile(String path, String str) {
        File f = new File(path);
        if(f.exists())
            f.delete();

        try{
            f.createNewFile();
            FileOutputStream fis = new FileOutputStream(f);
            BufferedOutputStream bis = new BufferedOutputStream(fis);
            bis.write(str.getBytes());
            bis.flush();
            bis.close();
            fis.close();

        }catch (IOException e){
            e.printStackTrace();
            return;
        }
    }

}
