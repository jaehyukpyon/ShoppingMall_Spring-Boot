package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        // uploadPath : C:/shop/item
        // 파일을 업로드할 경로와, originalFileName(확장자 추출용)을 받아서, uploadPath에 저장만 하고, 실제 DB 및 uploadPath에 UUID로 변경되어 저장 된 파일 이름을 return
        log.info("FileService's uploadFile starts...");

        log.info("parameter - uploadPath: " + uploadPath);
        log.info("parameter - originalFileName: " + originalFileName);



        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        log.info("originalFileName's extension: " + extension);

        String savedFileName = UUID.randomUUID().toString() + extension; // 실제 DB 및 uploadPath에 저장될 파일명

       String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        log.info("extension: " + extension);
        log.info("savedFileName: " + savedFileName);
        log.info("fileUploadFullUrl: " + fileUploadFullUrl);

        log.info("FileService's uploadFile ends...\r\n");

        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("========== 파일을 삭제하였습니다. ========== \r\n");
        } else {
            log.info("========== 파일이 존재하지 않습니다. ========== \r\n");
        }
    }

}
