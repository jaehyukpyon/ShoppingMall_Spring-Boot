package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.transaction.Transactional;

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
        if (!StringUtils.isEmpty(oriImgName)) { // null / ""이 아니라면 실행.
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); // ItemImg entity에 아무런 변경을 하지 않고, 오직 파일을 uuid 값으로 변경하여 폴더에 저장하는 역할만 수행.
            imgUrl = "/images/item/" + imgName; // c:/shop/item/ + imgName(UUID + file (type) extension)
        }

        log.info("MultipartFile's getOriginalFilename() - oriImgName: " + oriImgName);
        log.info("after calling FileService's uploadFile() - imgName: " + imgName);
        log.info("imgUrl combined with imgName: " + imgUrl);

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);

        log.info("ItemImgService's saveItemImg ends...\r\n");
    }

}
