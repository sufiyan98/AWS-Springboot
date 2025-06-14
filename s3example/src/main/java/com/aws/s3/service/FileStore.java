package com.aws.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class FileStore {


    private static final Logger logger = LogManager.getLogger(FileStore.class);

    @Autowired
    private AmazonS3 amazonS3;

    public String createBucket(String bucketName){
        logger.info("Inside method createBucket");
        try{
            if(bucketAlreadyExists(bucketName)){
                logger.error("bucket already exist");
            }
            amazonS3.createBucket(bucketName);
        }catch (AmazonS3Exception e){
            logger.error("unable to create bucket: "+e.getMessage());
        }
        return "Bucket created with name: "+bucketName;
    }


    private boolean bucketAlreadyExists(String bucketName){
        logger.info("Inside method bucketAlreadyExists");
        return amazonS3.doesBucketExistV2(bucketName);
    }

    public void uploadFileToBucket(MultipartFile multipartFile, String bucketName) throws Exception{
        logger.info("Inside method upload");
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(convFile);
        try{
            amazonS3.putObject(bucketName,convFile.getName(), convFile);
        }catch (AmazonS3Exception ex){
            logger.error("Unable to upload file: "+ex.getMessage());
        }
    }

    public void deleteBucket(String bucketName){
        amazonS3.deleteBucket(bucketName);
    }

    public void deleteFile(String bucketName, String fileName){
        amazonS3.deleteObject(bucketName,fileName);
    }

}
