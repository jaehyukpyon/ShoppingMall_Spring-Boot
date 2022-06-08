package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();

        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");

        return Member.createMember(memberFormDto, passwordEncoder);
    }

    //@Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest() {
        System.out.println("테스트 시작");

        Member member = createMember();
        System.out.println("DB에 member 저장 시작...\r\n");
        memberRepository.save(member);
        System.out.println("DB에 member 저장 완료...\r\n");

        Cart cart = new Cart();
        cart.setMember(member);

        System.out.println("DB에 cart 저장 시작...\r\n");
        cartRepository.save(cart);
        System.out.println("DB에 cart 저장 완료...\r\n");

        Cart savedCart = cartRepository.findById(cart.getId())
                                        .orElseThrow(new Supplier<EntityNotFoundException>() {
                                            @Override
                                            public EntityNotFoundException get() {
                                                return new EntityNotFoundException();
                                            }
                                        });
        System.out.println("check id: " + savedCart.getMember().getId());
    }

}