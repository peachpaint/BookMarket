package org.example.bookmarket.domain;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity//@Entity 사용시에는 @Data를 잘 사용하지 않는다
@Table(name = "address")
public class Address {
  @Id
  @Column(name = "member_id", length = 50)
  private String memberId;//// Member의 PK를 FK이자 PK로 사용

  @OneToOne
  @MapsId  // memberId를 PK이자 FK로 사용
  @JoinColumn(name = "member_id")
  private Member member;

  private String country;

  @Column(name = "zipcode")
  private String zipcode;//우편번호

  @Column(name = "address_name")
  private String addressname;//주소

  @Column(name = "detail_name")
  private String detailname;//세부주소
}
