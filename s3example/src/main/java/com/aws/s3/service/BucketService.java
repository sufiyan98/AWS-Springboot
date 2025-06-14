package com.aws.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class BucketService {
    @Autowired
    private FileStore fileStore;

    private Logger logger = LogManager.getLogger(this.getClass().getName());

    public void downloadFile(String fileName, AmazonS3 amazonS3, String bucketName){
        try{
            logger.info("File to be fetched from s3 {}"+fileName);
            S3Object s3Object = amazonS3.getObject(bucketName, fileName);

            InputStream objectContent = s3Object.getObjectContent();

            String content = IOUtils.toString(objectContent);
            logger.info("Content {}"+content);
        }catch (IOException ioException){
            logger.error("Error in reading file content {}"+ioException.getMessage());
        }
        catch (AmazonS3Exception amazonS3Exception){
            logger.error("Some error occured: "+amazonS3Exception.getMessage());
        }
    }



    public String createBucket(String bucketName){
        return fileStore.createBucket(bucketName);
    }

    public String uploadFile(MultipartFile multipartFile, String bucketName){
        if(multipartFile.isEmpty()){
            throw new IllegalStateException("cannot upload empty file");
        }
        try{
            fileStore.uploadFileToBucket(multipartFile,bucketName);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to upload file",e);
        }
        return "File uploaded successfully";
    }


    public String deleteBucket(String bucketName){
        fileStore.deleteBucket(bucketName);
        return "Bucket deleted successfully...";
    }

    public String deleteFile(String bucketName, String fileName){
        fileStore.deleteFile(bucketName,fileName);
        return "File deleted successfully...";
    }
}
