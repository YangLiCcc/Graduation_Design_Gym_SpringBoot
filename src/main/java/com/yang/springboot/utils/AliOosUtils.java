package com.yang.springboot.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
public class AliOosUtils {

    @Value("${aliyun.oos.file.Endpoint}")
    private String Endpoint;

    @Value("${aliyun.oos.file.AccessKeyId}")
    private String AccessKeyId;

    @Value("${aliyun.oos.file.AccessKeySecret}")
    private String AccessKeySecret;

    @Value("${aliyun.oos.file.BucketName}")
    private String BucketName;

    // 文件上传
    public String upload(String type, MultipartFile file) {

        // 文件上传地址
        String uploadUrl = null;
        try {
            // 判断oos实例是否存在 不存在则创建 存在则获取
            OSS ossClient = new OSSClientBuilder().build(Endpoint, AccessKeyId, AccessKeySecret);
            if (!ossClient.doesBucketExist(BucketName)) {
                // Bucket不存在 创建Bucket
                ossClient.createBucket(BucketName);
                // 设置Bucket的访问权限为 公共读写
                ossClient.setBucketAcl(BucketName, CannedAccessControlList.PublicReadWrite);
            }

            // 获取文件上传流
            InputStream inputStream = file.getInputStream();
            // 构建文件上传路径为 日期路径 /fileType/yyyyMMdd/file
            String filePath = new DateTime().toString("yyyyMMdd");
            // 构建文件名为 uuid.fileType
            String origin = file.getOriginalFilename();
            String fileName = IdUtil.fastSimpleUUID();
            assert origin != null;
            String fileType = origin.substring(origin.lastIndexOf("."));
            String uploadFileName = fileName + fileType;
            // 上传到Bucket后的路径格式 如avatar/20220612/uuid.jpg
            String objectName = type + "/" + filePath + "/" + uploadFileName;

            // 上传文件至阿里云OOS Bucket
            ossClient.putObject(BucketName, objectName, inputStream);
            // 关闭OSSClient
            ossClient.shutdown();
            // 获取上传地址
            uploadUrl = "https://" + BucketName + "." + Endpoint + "/" + objectName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uploadUrl;
    }

}
