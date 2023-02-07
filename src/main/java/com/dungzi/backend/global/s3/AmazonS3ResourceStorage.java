package com.dungzi.backend.global.s3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AmazonS3ResourceStorage {

    //bucketName
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    // 폴더 생성 (폴더는 파일명 뒤에 "/"를 붙여야한다.)
    public void createFolder(String bucketName, String folderName) {
        amazonS3Client.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
    }

    // 파일 업로드
    public void fileUpload(String fileName, byte[] fileData) throws FileNotFoundException {
        String filePath = (fileName).replace(File.separatorChar, '/');
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(fileData.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData);

        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, filePath, byteArrayInputStream, metaData));
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    // 파일 삭제
    public void fileDelete(String fileName) {

        System.out.println("fileName : " + fileName);
        String imgName = (fileName).replace(File.separatorChar, '/');

        try {
            amazonS3Client.deleteObject(bucket, imgName);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        //System.out.println("삭제성공");
    }

}