package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /*
    * member entity와 OneToOne 매핑 관계를 맺어줄 때 default option으로
    * @OneToOne(fetch = FetchType.EAGER)
    * entity 조회 시 해당 entity와 매핑된 엔티티도 한 번에 조회 (즉시 로딩)*/
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // mapping할 FK지정
    private Member member;

}
