package com.sparta.sorisam.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.sorisam.global.error.exception.BusinessException;
import com.sparta.sorisam.global.error.exception.ErrorCode;
import com.sparta.sorisam.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3AudioService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름


    //오디오파일 업로드
    @Transactional
    public String uploadAudio(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        if (multipartFile.getContentType().equals("multipart/form-data")
                && getFileExtension(multipartFile.getOriginalFilename()).equals(".mp3")) {
            objectMetadata.setContentType("audio/mp3");
        }
        if (multipartFile.getContentType().equals("multipart/form-data")
                && getFileExtension(multipartFile.getOriginalFilename()).equals(".wav")) {
            objectMetadata.setContentType("audio/basic");
        }
        if (multipartFile.getContentType().equals("multipart/form-data")
                && getFileExtension(multipartFile.getOriginalFilename()).equals(".m4a")) {
            objectMetadata.setContentType("audio/basic");
        }

        //objectMetaData 에 파라미터로 들어온 파일의 타입 , 크기를 할당.
        objectMetadata.setContentLength(multipartFile.getSize());

        //fileName 에 파라미터로 들어온 파일의 이름을 할당.
        String fileName = multipartFile.getOriginalFilename();
        fileName = UUID.randomUUID().toString().concat(getFileExtension(fileName));


        try {
            //amazonS3Client 객체의 putObject 메서드로 db에 저장
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.UPLOAD_FAILED);
        }

        return String.valueOf(amazonS3Client.getUrl(bucket, fileName));
    }

    //음원 확장자 확인
    private String getFileExtension(String fileName) { // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".wav");
        fileValidate.add(".mp3");
        fileValidate.add(".m4a");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            System.out.println("idxFileName = " + idxFileName);
            throw new InvalidValueException(ErrorCode.INVALID_FILE_EXTENSION);
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }



    public void deleteObject(String sourceKey) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, sourceKey));

    }

    public void deleteObjectByFilePath(String filePath) {
        String sourceKey = filePath.split("/")[filePath.split("/").length - 1];
        amazonS3Client.deleteObject(bucket, sourceKey);
    }

}
