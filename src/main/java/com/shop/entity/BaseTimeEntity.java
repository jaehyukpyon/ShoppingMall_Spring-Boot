package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@EntityListeners(value = {AuditingEntityListener.class}) // Auditing 적용 위함.
@MappedSuperclass // 공통 매핑 정보가 필요할 때 사용하는 annotation. 부모 클래스를 상속 받는 자식 클래스에게 매핑 정보만 제공함.
public abstract class BaseTimeEntity {

    @CreatedDate // entity 가 생성되어 저장될 때 시간을 자동으로 저장
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate // entity의 값을 변경할 때 시간을 자동으로 저장
    private LocalDateTime updateTime;

}
