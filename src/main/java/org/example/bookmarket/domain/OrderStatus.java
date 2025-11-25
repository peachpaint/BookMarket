package org.example.bookmarket.domain;

public enum OrderStatus {
  CREATED,    // 주문이 생성됨
  PAID,       // 결제됨
  CANCELLED,  // 취소됨
  COMPLETED   // 주문 완료 (예: 배송 & 결제 모두 완료)
}
