package com.adalab.examination.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.adalab.examination.GitClone.GitUtils.delAllFile;

@Service
public class GitService {

    private final String path = System.getProperty("user.dir");
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //在resources文件夹下根据学员id生成文件夹 clone学员的代码
    //需要传过来学生代码仓库url 学生id
    public void gitClone(String studentID,String url,String step) throws GitAPIException {
        String time = "/"+System.currentTimeMillis();
        String localPath = path+"/src/main/resources/studentCode"+studentID+step+time;
        File file = new File(localPath);
        if (file.exists()){
            logger.info("删除文件结果:"+delAllFile(localPath));
        }

        try {
            Git git = Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(new File(localPath))
                    .call();
            logger.info("clone成功!"+git.toString());
        } catch (GitAPIException e) {
            logger.error("clone失败!");
            throw e;
        }
    }
}
