package org.example.bookmarket.domain;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;


@Getter
@Entity//@Entity 사용시에는 @Data를 잘 사용하지 않는다
public class address {
  @Id
  @GeneratedValue
  private Long id;
  private String country;
  private String zipcode;//우편번호
  private String addressname;//주소
  private String detailname;//세부주소
}
