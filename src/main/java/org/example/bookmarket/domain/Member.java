package org.example.bookmarket.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "member")
@Data
public class Member {
  @Id
  @Column(name = "member_id", length = 50)
  @MemberId//사용자 정의 에너테이션
  @NotEmpty(message = "아이디를 입력해주세요")
  private String memberId;

  @Column(nullable = false)
  @NotEmpty(message = "비밀번호를 입력해주세요")
  @Size(min=4,max=12, message="4자~12자 이내로 입력해 주세요")
  private String passwd;

  @Column(nullable = false)
  @NotEmpty(message = "이름을 입력해주세요")
  private  String name;

  @Column(nullable = false)
  @NotEmpty(message = "전화번호를 입력해주세요")
  private String phone;

  @Column(length = 50)
  private String city;

  @Column(length = 10)
  private String sex;

  @Column(length = 20)
  private String role = "USER";  // 기본 역할

  // 주소 정보 (양방향 관계)
  @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private Address address;

  // Member.deliveries 컬렉션 제거:
  // - 비즈니스 로직에서 사용되지 않음
  // - LazyInitializationException 원인
  // - Delivery는 Member를 참조하지만, Member는 Delivery 컬렉션 불필요
}
