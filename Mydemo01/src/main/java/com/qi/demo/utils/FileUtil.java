package com.qi.demo.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtil {
    /**
     * 创建文件顺便创建父目录
     *
     * @param path 文件字符串路径例如d:/fulld/why/ass/a/asd
     */
    public static void createFile(String path) {
        createFile(new File(path));
    }

    /**
     * 创建文件顺便创建父目录
     *
     * @param file file类
     */
    private static void createFile(File file) {
        if (file.exists() && file.isFile()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        File parentFile = file.getParentFile();
        if (parentFile.exists()) {
            if (parentFile.isFile()) {
                parentFile.delete();
                parentFile.mkdirs();
            }
        } else {
            parentFile.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件夹顺便创建父目录
     *
     * @param path 文件夹的路径字符串例如d:/fulld/why/ass/a/asd/
     * @return 如果本来就存在或者创建成功，那么就返回true
     */
    public static void mkdirs(String path) {
        mkdirs(new File(path));
    }

    /**
     * 创建文件夹顺便创建父目录
     *
     * @param file file类
     */
    public static void mkdirs(File file) {
        if (file.exists() && file.isDirectory()) {
            return;
        }
        if (file.exists()) {
            file.delete();
            file.mkdirs();
        } else {
            file.mkdirs();
        }
    }

    public static String filePath2docPath(String filePath){
        String[] words  = filePath.split("\\\\");
        ArrayList<String> newWords = new ArrayList<>();
        for (int i = 0; i<words.length-1; i++){
            newWords.add(words[i]);
        }

        String docPath = "";
        for (String string: newWords){
            docPath += string;
            docPath += "\\";
        }
        return docPath;
    }
}
