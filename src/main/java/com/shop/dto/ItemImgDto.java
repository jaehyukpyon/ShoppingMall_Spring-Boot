package com.shop.dto;

import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        /*
        * ItemImg Entity를 파라미터로 받은 후, ItemImg의 자료형과 멤버변수의 이름이 같을 때,
        * ItemImgDto로 값을 "복사"하여 return*/
        return modelMapper.map(itemImg, ItemImgDto.class);
    }

}
