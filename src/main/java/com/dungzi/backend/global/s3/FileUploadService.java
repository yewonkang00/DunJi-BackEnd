package com.dungzi.backend.global.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
public class FileUploadService {
    @Value("${cloud.aws.s3.bucket}")

    //bucketName
    private static String bucket;
    private static AmazonS3ResourceStorage amazonS3ResourceStorage;
    private static String formatName = "jpg"; // 파일 포맷 jpg로 통일

    public static void uploadRoomFile(String RoomID, List<MultipartFile> file) throws Exception {
        log.info("[S3] Upload File");

        String uploadPath = "/room";
        for(int i = 0; i < file.size(); i++) {

            String fileName;
            if(i == 0) {
                fileName = '/' + RoomID + '/' + "main" + "." + formatName;
            }
            else {
                fileName = '/' + RoomID + '/' + i + "." + formatName;
            }

            amazonS3ResourceStorage.fileUpload(bucket+uploadPath+fileName, file.get(i).getBytes());
            log.info("[S3] Upload File : " + fileName);
        }
    }

    public static String uploadReviewFile(String ReviewID, List<MultipartFile> file) throws Exception {

        String uploadPath = "/review";
        for(int i = 0; i < file.size(); i++) {

            String fileName;
            fileName = '/' + ReviewID + '/' + i + "." + formatName;

            amazonS3ResourceStorage.fileUpload(bucket+uploadPath+fileName, file.get(i).getBytes());
            System.out.println("****file name : " + fileName);

        }

        String message = "Success";

        return message;
    }
}
