package org.example.bookmarket.domain;

/**
 * 배송 상태를 나타내는 열거형
 */
public enum DeliveryStatus {
    READY,      // 배송 준비
    SHIPPED,    // 발송됨
    DELIVERED,  // 배달 완료
    CANCELLED   // 취소됨
}
