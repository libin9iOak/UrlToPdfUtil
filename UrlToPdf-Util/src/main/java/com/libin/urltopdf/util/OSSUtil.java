 package com.libin.urltopdf.util;


 import com.aliyun.oss.OSS;
 import com.aliyun.oss.OSSClient;
 import com.aliyun.oss.OSSClientBuilder;
 import com.aliyun.oss.model.ObjectMetadata;
 import com.aliyun.oss.model.PutObjectRequest;
 import com.aliyun.oss.model.PutObjectResult;
 import io.micrometer.common.util.StringUtils;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.web.multipart.MultipartFile;

 import java.io.IOException;
 import java.io.InputStream;
 import java.net.URL;
 import java.util.Date;
 import java.util.List;
 import java.util.Random;


 public class OSSUtil {

     public OSSUtil() {
     }

     public String getEndpoint() {
         return endpoint;
     }

     public void setEndpoint(String endpoint) {
         OSSUtil.endpoint = endpoint;
     }

     public String getAccessKeyId() {
         return accessKeyId;
     }

     public void setAccessKeyId(String accessKeyId) {
          accessKeyId = accessKeyId;
     }

     public String getAccessKeySecret() {
         return accessKeySecret;
     }

     public void setAccessKeySecret(String accessKeySecret) {
          accessKeySecret = accessKeySecret;
     }

     public String getBucketName() {
         return bucketName;
     }

     public void setBucketName(String bucketName) {
          bucketName = bucketName;
     }

     public String getFiledir() {
         return filedir;
     }

     public static void setFiledir(String filedir) {
          filedir = filedir;
     }

     protected static final Logger log = LoggerFactory.getLogger(OSSUtil.class);

     // 获取oss的地域节点  生产环境内网节点
     private static String internalEndpoint = "oss-cn-beijing-internal.aliyuncs.com";
     // 获取oss的地域节点  开发环境外网节点
      private static String endpoint = "oss-cn-beijing.aliyuncs.com";
     private static String accessKeyId = "accessKeyId";
     private static String accessKeySecret = "accessKeySecret";

     // 获取oss的Bucket名称
     private static String bucketName = "bucketName";

     //文件存储目录
     private static String filedir = "imageFile/";
     //private String endDir = "/";



         // 允许上传文件(图片)的格式
         private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg",
                 ".jpeg", ".gif", ".png"};

     /**
      *
      * @param instream
      * @param fileName
      * @param orderId
      * @return
      */
     public String uploadIpsIcc(InputStream instream, String fileName, String orderId) {
         setFiledir("imageFile/"+orderId+"/");
         ObjectMetadata meta = new ObjectMetadata();
         meta.setContentType("image/jpg");

         try {
             OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

             OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
             PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filedir + fileName, instream,meta);
             PutObjectResult result = oss.putObject(putObjectRequest);

         } catch (Exception e) {
             log.error(e.getMessage(), e);
         } finally {
             try {
                 if (instream != null) {
                     instream.close();
                 }
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         String key = filedir + fileName;

         //return getUrl(key);
         return key;
     }
     /**
      *
      * 上传ICC处理结束的图片
      * @param file
      * @return
      */
     public  String  uploadImg2Oss(MultipartFile file,Long id) {
         if (file.getSize() > 1024 * 1024 *20) {
             return "图片太大";//RestResultGenerator.createErrorResult(ResponseEnum.PHOTO_TOO_MAX);
         }
         String originalFilename = file.getOriginalFilename();
         String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
         Random random = new Random();
         String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
         try {
             InputStream inputStream = file.getInputStream();
            //  uploadFile2OSS(inputStream, name,id);
            //  uploadIpsIcc(inputStream,name,id.toString());
             return name;//RestResultGenerator.createSuccessResult(name);
         } catch (Exception e) {
             return "上传失败";//RestResultGenerator.createErrorResult(ResponseEnum.PHOTO_UPLOAD);
         }
     }

     /**
      * 上传图片获取fileUrl
      * @param instream
      * @param fileName
      * @return
      */
     private String uploadFile2OSS(InputStream instream, String fileName, Long id) {
         String ret = "";
         setFiledir("imageFile/");
         try {
             //创建上传Object的Metadata
             ObjectMetadata objectMetadata = new ObjectMetadata();
             objectMetadata.setContentLength(instream.available());
             objectMetadata.setCacheControl("no-cache");
             objectMetadata.setHeader("Pragma", "no-cache");
             objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
             objectMetadata.setContentDisposition("inline;filename=" + fileName);
             //上传文件

             OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
             setFiledir( getFiledir() + id + "/");
             PutObjectResult putResult = ossClient.putObject(bucketName,  getFiledir() + fileName, instream, objectMetadata);
             ret = putResult.getETag();
         } catch (IOException e) {
             log.error(e.getMessage(), e);
         } finally {
             try {
                 if (instream != null) {
                     instream.close();
                 }
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         return ret;
     }

     /**
      *
      *
      * @param instream
      * @param fileName
      * @param userId
      * @return
      */
     public static String uploadUrlToPdf(InputStream instream, String fileName, String userId) {
          setFiledir("imageFile/"+userId+"/");
        // ObjectMetadata meta = new ObjectMetadata();
        // meta.setContentType("image/jpg");

         try {
             OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

             OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
             PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filedir + fileName, instream );
             PutObjectResult result = oss.putObject(putObjectRequest);

         } catch (Exception e) {
             log.error(e.getMessage(), e);
         } finally {
             try {
                 if (instream != null) {
                     instream.close();
                 }
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         String key = filedir + fileName;

         return getUrl(key);
         //return key;
     }


     public static String getcontentType(String FilenameExtension) {
         if (FilenameExtension.equalsIgnoreCase(".bmp")) {
             return "image/bmp";
         }
         if (FilenameExtension.equalsIgnoreCase(".gif")) {
             return "image/gif";
         }
         if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                 FilenameExtension.equalsIgnoreCase(".jpg") ||
                 FilenameExtension.equalsIgnoreCase(".png")) {
             return "image/jpeg";
         }
         if (FilenameExtension.equalsIgnoreCase(".html")) {
             return "text/html";
         }
         if (FilenameExtension.equalsIgnoreCase(".txt")) {
             return "text/plain";
         }
         if (FilenameExtension.equalsIgnoreCase(".vsd")) {
             return "application/vnd.visio";
         }
         if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                 FilenameExtension.equalsIgnoreCase(".ppt")) {
             return "application/vnd.ms-powerpoint";
         }
         if (FilenameExtension.equalsIgnoreCase(".docx") ||
                 FilenameExtension.equalsIgnoreCase(".doc")) {
             return "application/msword";
         }
         if (FilenameExtension.equalsIgnoreCase(".xml")) {
             return "text/xml";
         }
         return "image/jpeg";
     }


     /**
      * 获取图片路径
      * @param fileUrl
      * @return
      */
     public String getImgUrl(String fileUrl) {
         if (!StringUtils.isEmpty(fileUrl)) {
             String[] split = fileUrl.split("/");
             String url =  getUrl( getFiledir() + split[split.length - 1]);
             return url;
         }
         return null;
     }

     /**
      * 获得url链接
      *
      * @param key
      * @return
      */
     public static String getUrl(String key) {
         // 设置URL过期时间为30天  3600l* 1000*24*365*10
         Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 30);
         // 生成URL
         OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
         URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
         if (url != null) {
             return url.toString();
         }
         return null;
     }


     /**
      * 多图片上传
      * @param fileList
      * @return
      */
     public String checkList(List<MultipartFile> fileList, Long id) {
         String  fileUrl = "";
         String  str = "";
         String  photoUrl = "";
         for(int i = 0;i< fileList.size();i++){
             fileUrl = uploadImg2Oss(fileList.get(i),id);
             str = getImgUrl(fileUrl);
             if(i == 0){
                 photoUrl = str;
             }else {
                 photoUrl += "," + str;
             }
         }
         return photoUrl.trim();
     }

     /**
      * 单个图片上传
      * @param file
      * @return
      */
     public String checkImage(MultipartFile file,Long id){
         String fileUrl = uploadImg2Oss(file,id);
         String str = getImgUrl(fileUrl);
         return str.trim();
     }



 }

