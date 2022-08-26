package com.adalab.examination.GitClone;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
public class DirectoryUtils {
    public static HashMap<String, Object> traverseDir(String path){
        HashMap<String, Object> map = Maps.newHashMap();
        File file = new File(path);
        if (file.isDirectory()){
            File[] files = file.listFiles();
            List<Object> list = Lists.newArrayList();
            if (Objects.nonNull(files)){
                for (File value : files) {
                    if (!value.isDirectory()) {
                        list.add(value.getName());
                        //把傻嗨/.git踢出去
                    } else if (!value.getName().endsWith(".git")){
                        list.add(traverseDir(value.getAbsolutePath()));
                    }
                }
                map.put(file.getName(), list);
            }
        }
        return map;
    }



}
