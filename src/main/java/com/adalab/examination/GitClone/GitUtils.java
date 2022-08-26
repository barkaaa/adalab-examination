package com.adalab.examination.GitClone;

import java.io.File;

public class GitUtils {
    /***
     * 删除文件夹
     *
     * @param folderPath folderPath文件夹完整绝对路径
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static  boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
    /**
     * 递归获取某路径下的所有文件，文件夹，并输出
     */
    public static void getFiles(String clientBase) {
        File file = new File(clientBase);
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 如果还是文件夹 递归获取里面的文件 文件夹
                if (files[i].isDirectory()&&!files[i].getName().endsWith(".git")) {

                    System.out.println("目录：" + files[i].getName());
                    //继续读取文件夹里面的所有文件
                    getFiles(files[i].getPath());
                } else {
                    if (!files[i].getName().endsWith(".git")){
                        System.out.println("文件：" + files[i].getName());

                    }

                }
            }
        } else {
            System.out.println("文件：" + file.getPath());
        }
    }
}
