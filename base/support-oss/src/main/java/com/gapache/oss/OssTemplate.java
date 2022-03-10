package com.gapache.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.gapache.oss.client.OssClientManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * <link>https://help.aliyun.com/document_detail/145212.html?spm=a2c4g.11186623.6.813.5a254ebcwssZDF</link>
 *
 * @author HuSen
 * @since 2020/8/28 10:30 上午
 */
@Slf4j
public class OssTemplate {

    private final OssClientManager ossClientManager;

    public OssTemplate(OssClientManager ossClientManager) {
        Assert.notNull(ossClientManager, "OssClientManager is required not null!");
        this.ossClientManager = ossClientManager;
    }

    /**
     * 创建存储空间
     *
     * @param bucketName              存储空间的名称
     * @param cannedAccessControlList 存储空间的权限
     * @param storageClass            存储空间的存储类型
     * @return 存储空间信息
     */
    public Bucket createBucket(String bucketName, CannedAccessControlList cannedAccessControlList, StorageClass storageClass) {
        return execute(oss -> {
            CreateBucketRequest request = new CreateBucketRequest(bucketName);
            request.setCannedACL(cannedAccessControlList);
            request.setStorageClass(storageClass);
            return oss.createBucket(request);
        }, "createBucket");
    }

    /**
     * 列举存储空间
     *
     * @return 存储空间列表
     */
    public List<Bucket> listBuckets() {
        return execute(OSS::listBuckets, "listBuckets");
    }

    /**
     * 判断存储空间是否存在
     *
     * @param bucketName 存储空间名称
     * @return 是否存在
     */
    public boolean doesBucketExist(String bucketName) {
        Boolean exist = execute(oss -> oss.doesBucketExist(bucketName), "doesBucketExist");
        return exist != null && exist;
    }

    /**
     * 获取存储空间的信息
     *
     * @param bucketName 存储空间名称
     * @return 存储空间的信息
     */
    public BucketInfo getBucketInfo(String bucketName) {
        return execute(oss -> oss.getBucketInfo(bucketName), "getBucketInfo");
    }

    /**
     * 删除存储空间
     *
     * @param bucketName 存储空间名称
     * @return 删除结果
     */
    public boolean deleteBucket(String bucketName) {
        Boolean result = execute(oss -> {
            oss.deleteBucket(bucketName);
            return true;
        }, "deleteBucket");
        return result != null && result;
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储空间名称
     * @param objectName 全路径文件名
     * @param data       文件数据
     * @return 上传结果
     */
    public boolean uploadFile(String bucketName, String objectName, byte[] data) {
        PutObjectResult result = execute(oss -> oss.putObject(bucketName, objectName,
                new ByteArrayInputStream(data)), "uploadFile");
        return result != null && result.getResponse().isSuccessful();
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储空间名称
     * @param objectName 全路径文件名
     * @return 删除结果
     */
    public boolean delete(String bucketName, String objectName) {
        Boolean result = execute(oss -> {
            oss.deleteObject(bucketName, objectName);
            return true;
        }, "delete");
        return result != null && result;
    }

    /**
     * 使用签名URL临时授权
     *
     * @param bucketName 存储空间名称
     * @param objectName 全路径文件名
     * @param expire     有效时间
     * @return url
     */
    public String generatePresignedUrl(String bucketName, String objectName, long expire) {
        URL url = execute(oss -> oss.generatePresignedUrl(bucketName, objectName, new Date(System
                .currentTimeMillis() + expire)), "generatePresignedUrl");
        return url != null ? url.toString() : "";
    }

    /**
     * 生成公共权限的文件URL
     *
     * @param bucketName 存储空间名称
     * @param objectName 全路径文件名
     * @return url
     */
    public String generatePublicUrl(String bucketName, String objectName) {
        return "https://" + bucketName + "." + ossClientManager.getEndpoint().replace("http://", "") + "/" + objectName;
    }

    private <T> T execute(Function<OSS, T> executor, String reason) {
        OSS oss = ossClientManager.getOss();
        try {
            return executor.apply(oss);
        } catch (Exception e) {
            log.error("execute {} error.", reason, e);
            return null;
        } finally {
            oss.shutdown();
        }
    }
}
