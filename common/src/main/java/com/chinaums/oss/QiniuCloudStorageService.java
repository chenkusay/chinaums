package com.chinaums.oss;

import com.chinaums.utils.RRException;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云存储
 * @author rachel.li
 */
public class QiniuCloudStorageService extends CloudStorageService{

    private UploadManager uploadManager;
    private BucketManager bucketManager;
    private String token;
    private Auth auth;

    public QiniuCloudStorageService(CloudStorageConfig config){
        this.config = config;

        //初始化
        init();
    }

    private void init(){
        auth = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey());
        Configuration c = new Configuration(Zone.autoZone());
        uploadManager = new UploadManager(c);
        bucketManager = new BucketManager(auth, c);
        token = auth.uploadToken(config.getQiniuBucketName());
    }

    @Override
    public String upload(String pic) throws IOException {
        if(!StringUtils.isBlank(pic) && !pic.contains("http:") && !pic.contains("https:") && !pic.contains("upload")){
            if (pic.indexOf("data:image/jpeg;base64") > -1 || pic.indexOf("data:image/png;base64") > -1 || pic.indexOf("data:image/gif;base64") > -1){
                pic = pic.substring(pic.indexOf(",") * 1 + 1, pic.length());
            }
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] data = decoder.decodeBuffer(pic);
            for(int i=0;i<data.length;++i){
                if(data[i]<0){ data[i]+=256; }
            }
//            InputStream input = new ByteArrayInputStream(data);
//            OutputStream output = new ByteArrayOutputStream();
//            //图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
//            Thumbnails.of(input).scale(1f).outputQuality(0.25f).toOutputStream(output);
            return upload(data);
        }else{
            return pic;
        }
    }

    @Override
    public String upload(byte[] data, String path) {
        try {
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res.toString());
            }
        } catch (Exception e) {
            throw new RRException("上传文件失败，请核对七牛配置信息", e);
        }

        return config.getQiniuDomain() + "/" + path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new RRException("上传文件失败", e);
        }
    }

    @Override
    public String upload(byte[] data) {
        return upload(data, getPath(config.getQiniuPrefix()));
    }

    @Override
    public String upload(InputStream inputStream) {
        return upload(inputStream, getPath(config.getQiniuPrefix()));
    }

    @Override
    public void delete(String path){
        if (path.contains(config.getQiniuDomain())){
            String key = path.replaceAll(config.getQiniuDomain()+"/", "");
            try {
                //调用delete方法移动文件
                bucketManager.delete(config.getQiniuBucketName(), key);
            } catch (QiniuException e) {
                //捕获异常信息
                Response r = e.response;
                System.out.println(r.toString());
            }
        }
    }

    @Override
    public String download(String url) {
        // 调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
        String downloadRUL = auth.privateDownloadUrl(url,3600);
        System.out.println(downloadRUL);
        return downloadRUL;
    }

}
