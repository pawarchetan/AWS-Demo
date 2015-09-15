package com.lendbox.processor;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;

//facade design pattern
public class FileUploadProcessor {

    private static Logger logger = Logger.getLogger(FileUploadProcessor.class);

    public boolean isBucketAlreadyExist(String bucketName, AmazonS3 s3client) {
        for (Bucket bucket : s3client.listBuckets()) {
            if (bucket.getName().equalsIgnoreCase(bucketName)) {
                return true;
            }
        }
        return false;
    }

    public String addFileToS3Bucket(File fileObject, String fileToUpload, String bucketName) {
        String response = "failed";

        AWSCredentials myCredentials = new BasicAWSCredentials(
                S3Config.getMyAccessId(), S3Config.getMySecretId());
        AmazonS3 s3 = new AmazonS3Client(myCredentials);

        logger.info("==========================================================");
        logger.info("      Getting Started with File Upload to Amazon S3       ");
        logger.info("==========================================================\n");

        try {
            logger.info("Verifying the bucket Name.........");

            if (isBucketAlreadyExist(bucketName, s3)) {
                logger.info("Bucket" + bucketName + "already exist.");
            } else {
                logger.info("Creating the bucket.........");
                s3.createBucket(bucketName);
            }

            logger.info("Uploading a new object to S3...");
            logger.info("Name of the uploading object is: " + fileToUpload);
            s3.putObject(new PutObjectRequest(bucketName, fileToUpload, fileObject));
            logger.info("File uploaded on S3 - location: " + bucketName + " -> " + fileToUpload);
            response = "success";

        } catch (AmazonServiceException ase) {
            logger.error("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");
            logger.error("HTTP Status Code: " + ase.getStatusCode());
            logger.error("AWS Error Code:   " + ase.getErrorCode());
            logger.error("Error Type:       " + ase.getErrorType());
            logger.error("Request ID:       " + ase.getRequestId());
            logger.error("Error Message:    " + ase.getMessage());
        } catch (AmazonClientException ace) {
            logger.error("Caught an AmazonClientException, which means the client encountered a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            logger.error("Error Message: " + ace.getMessage());
        }
        return response;
    }

    public String getUrlForDocument(String fileName, String bucketName) {
        AWSCredentials myCredentials = new BasicAWSCredentials(
                S3Config.getMyAccessId(), S3Config.getMySecretId());
        AmazonS3 s3 = new AmazonS3Client(myCredentials);
        logger.info("File name - " + fileName);

        String fileLink = "";
        try {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileName);
            URL url = s3.generatePresignedUrl(request);
            fileLink = url.toString();
            logger.info("File link - " + fileLink);

        } catch (AmazonServiceException exception) {
            logger.info("Caught an AmazonServiceException ,which means your request made it " +
                    "to Amazon S3, but was rejected with an error response for some reason.");
            logger.info("Error Message: " + exception.getMessage());
            logger.info("HTTP  Code: " + exception.getStatusCode());
            logger.info("AWS Error Code:" + exception.getErrorCode());
            logger.info("Error Type:    " + exception.getErrorType());
            logger.info("Request ID:    " + exception.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException, which means the client encountered " +
                    "an internal error while trying to communicate  with S3, such as not being able to access the network.");
            logger.info("Error Message: " + ace.getMessage());
        }
        return fileLink;

    }

    public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        File file = new File(multipart.getOriginalFilename());
        multipart.transferTo(file);
        return file;
    }
}
