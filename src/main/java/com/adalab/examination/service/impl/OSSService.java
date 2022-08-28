package com.adalab.examination.service.impl;

import com.adalab.examination.config.OSSConfiguration;
import com.adalab.examination.vo.UploadVo;
import com.aliyun.oss.*;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import com.google.common.io.Files;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author wupx
 * @date 2020/07/09
 */
@Component
public class OSSService {

    public static Logger log = LoggerFactory.getLogger(OSSService.class);

    @Autowired
    private OSSConfiguration ossConfiguration;

    @Autowired
    private OSS ossClient;

    /**
     * 上传文件到阿里云 OSS 服务器
     * 链接：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/upload_object.html?spm=5176.docoss/user_guide/upload_object
     *
     * @param file
     * @return
     */
    public UploadVo uploadFile(MultipartFile file) {
        String fileName = "";
        UploadVo uploadVo = new UploadVo();
        try {
            String[] split = file.getOriginalFilename().split("\\.");
            String fileType = split[split.length - 1];
            // 创建一个唯一的文件名，类似于id，就是保存在OSS服务器上文件的文件名
            fileName = UUID.randomUUID().toString() + "." + fileType;
            String newFileName = fileType + "/" + fileName;
            InputStream inputStream = file.getInputStream();
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfiguration.getBucketName(), newFileName, inputStream);

            // 设置对象
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 设置数据流里有多少个字节可以读取
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            objectMetadata.setHeader("Pragma", "no-cache");
            if (fileType.equals("md")) {
                objectMetadata.setContentType("text/markdown;charset=utf-8");
            } else {
                objectMetadata.setContentType(file.getContentType());
            }
            putObjectRequest.setMetadata(objectMetadata);
            // 上传文件
            ossClient.putObject(putObjectRequest);
            uploadVo.setFileName(fileName);
            uploadVo.setUrl("https://" + ossConfiguration.getBucketName() + "." + OSSConfiguration.endpoint + "/" + newFileName);
        } catch (IOException e) {
            log.error("Error occurred: {}", e.getMessage(), e);
        }


        return uploadVo;
    }


    public UploadVo saveMarkdown(String content) {
        UploadVo uploadVo = new UploadVo();
        String fileName = UUID.randomUUID().toString();
        String newFileName = "md/" + fileName;

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfiguration.getBucketName(), newFileName, new ByteArrayInputStream(content.getBytes()));
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("text/markdown;charset=utf-8");
        putObjectRequest.setMetadata(objectMetadata);
        ossClient.putObject(putObjectRequest);
        uploadVo.setFileName(fileName);
        uploadVo.setUrl("https://" + ossConfiguration.getBucketName() + "." + OSSConfiguration.endpoint + "/" + newFileName);
        return uploadVo;
    }

    /**
     * 下载文件
     * 详细请参看“SDK手册 > Java-SDK > 上传文件”
     * 链接：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/download_object.html?spm=5176.docoss/sdk/java-sdk/manage_object
     *
     * @param os
     * @param objectName
     */
    public void exportFile(OutputStream os, String objectName) {
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流
        OSSObject ossObject = ossClient.getObject(ossConfiguration.getBucketName(), objectName);
        // 读取文件内容
        BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
        BufferedOutputStream out = new BufferedOutputStream(os);
        byte[] buffer = new byte[1024];
        int lenght;
        try {
            while ((lenght = in.read(buffer)) != -1) {
                out.write(buffer, 0, lenght);
            }
            out.flush();
        } catch (IOException e) {
            log.error("Error occurred: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取 url
     *
     * @param filename
     * @param expSeconds
     * @return
     */
    public String getSingeNatureUrl(String filename, int expSeconds) {
        Date expiration = new Date(System.currentTimeMillis() + expSeconds * 1000);
        URL url = ossClient.generatePresignedUrl(ossConfiguration.getBucketName(), filename, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * 删除文件
     * 详细请参看“SDK手册 > Java-SDK > 管理文件”
     * 链接：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_object.html?spm=5176.docoss/sdk/java-sdk/manage_bucket
     *
     * @param fileName
     */
    public void deleteFile(String fileName) {
        try {
            ossClient.deleteObject(ossConfiguration.getBucketName(), fileName);
        } catch (Exception e) {
            log.error("Error occurred: {}", e.getMessage(), e);
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    public boolean doesObjectExist(String fileName) {
        try {
            if (Strings.isEmpty(fileName)) {
                log.error("文件名不能为空");
                return false;
            } else {
                return ossClient.doesObjectExist(ossConfiguration.getBucketName(), fileName);
            }
        } catch (OSSException | ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查看 Bucket 中的 Object 列表
     * 详细请参看“SDK手册 > Java-SDK > 管理文件”
     * 链接：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_object.html?spm=5176.docoss/sdk/java-sdk/manage_bucket
     *
     * @return
     */
    public List<String> listObjects() {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ossConfiguration.getBucketName()).withMaxKeys(200);
        ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
        List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        return objectSummaries.stream().map(OSSObjectSummary::getKey).collect(Collectors.toList());
    }

    /**
     * 设置文件访问权限
     * 详细请参看“SDK手册 > Java-SDK > 管理文件”
     * 链接：https://help.aliyun.com/document_detail/84838.html
     *
     * @param fileName
     */
    public void setObjectAcl(String fileName) {
        ossClient.setObjectAcl(ossConfiguration.getBucketName(), fileName, CannedAccessControlList.PublicRead);
    }


}