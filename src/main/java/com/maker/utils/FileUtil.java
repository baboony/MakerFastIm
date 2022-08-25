package com.maker.utils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author lucky winner
 */
public class FileUtil {
    
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            boolean mkdirs = targetFile.mkdirs();
            if (!mkdirs){
                throw new RuntimeException("创建目录失败");
            }
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

}
