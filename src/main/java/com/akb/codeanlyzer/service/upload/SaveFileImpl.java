package com.akb.codeanlyzer.service.upload;

import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaveFileImpl implements SaveFileI{
    @Override
    public Map<String, Object> docPath(String fileName, String userName, String projName) {
        HashMap<String, Object> map = new HashMap<>();
        //文件保存地址
        String path = "D:\\storage\\upload\\tmp\\" + userName +"\\" + projName;
        //创建文件
        File dest = new File(path + "\\" + fileName);
        //如果文件已经存在就先删除掉
        if (dest.getParentFile().exists()) {
            dest.delete();
        }
        map.put("dest", dest);
        map.put("path", path+"/" + fileName);
        return map;
    }

    @Override
    public boolean saveFileFromBytes(byte[] b, Map<String, Object> map) {
        //创建文件流对象
        FileOutputStream fstream = null;
        BufferedOutputStream bos = null;
        //从map中获取file对象
        File file = (File) map.get("dest");
        System.out.println("尝试写入块到" + map.get("path"));
        //判断路径是否存在，不存在就创建
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            fstream = new FileOutputStream(file, true);
            bos = new BufferedOutputStream(fstream);
            bos.write(b);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fstream != null) {
                try {
                    fstream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return true;
    }

}
