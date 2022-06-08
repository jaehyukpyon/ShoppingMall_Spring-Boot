package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Log
@RequiredArgsConstructor
@Transactional
@Service
public class ItemImgService {

    @Value("${itemImgLocation}") // C:/shop/item
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        /*
        * ItemImg로부터, ItemImg에 이미지 대표 여부만 설정 된 ItemImg entity가 넘어온다.
        * */
        log.info("ItemImgService's saveItemImg starts...");

        String oriImgName = itemImgFile.getOriginalFilename(); // pants1.png
        String imgName = "";
        String imgUrl = "";

        // file upload
        if (!StringUtils.isEmpty(oriImgName)) { // null  "" 이 아니라면 실행.
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); // ItemImg entity에 아무런 변경을 하지 않고, 오직 파일을 uuid 값으로 변경하여 폴더에 저장하는 역할만 수행.
            imgUrl = "/images/item/" + imgName; // c:/shop/item/ + imgName(UUID + file (type) extension)
        }

        log.info("MultipartFile's getOriginalFilename() - oriImgName: " + oriImgName);
        log.info("after calling FileService's uploadFile() - imgName: " + imgName);
        log.info("imgUrl combined with imgName: " + imgUrl);

        // 상품 이미지 정보 저장
        // 원래의 이름, uuid로 치환된 이름, html에서 사용될 경로(/images/item/xxxx.png)
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);

        log.info("ItemImgService's saveItemImg ends...\r\n");
    }

    // 상품 이미지 수정 시, 상품 이미지 데이터 수정(영속성 컨텍스트의 변경감지 이용)
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if (!itemImgFile.isEmpty()) { // MultipartFile이 empty가 아니면
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;

            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
       }
    }

}
