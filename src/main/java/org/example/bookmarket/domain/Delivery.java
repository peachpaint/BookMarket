package org.example.bookmarket.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery")
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 배송 정보 고유 ID

    // **회원**과 연관 관계: 누가 배송을 요청했는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 배송 고객 이름 (예: 수취인 이름, 별도로 입력할 수 있게)
    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;

    // 배송 날짜
    @Column(name = "delivery_date", nullable = false)
    private LocalDateTime deliveryDate;

    // 배송 주소: Address 엔티티 참조하거나, 주소를 직접 저장할 수 있음
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address deliveryAddress;

    // 상태 (예: 배송 준비 중, 배송 완료 등)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    // 기타 정보 (메모, 연락처 등) 있으면 추가 가능
    @Column(name = "note", length = 500)
    private String note;
}
