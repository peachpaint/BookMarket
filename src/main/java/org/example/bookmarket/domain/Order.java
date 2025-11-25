package org.example.bookmarket.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")  // "order"는 예약어일 수 있으니 orders 등으로 테이블명 지정
@Data
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 세션에서 사용할 임시 Order ID (DB 저장 전)
  @Transient  // DB에 저장하지 않음
  private String tempOrderId;

  // 주문을 한 회원
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  // 주문 생성 시간
  @Column(name = "order_date", nullable = false)
  private LocalDateTime orderDate;

  // 주문 상태 (예: 주문 생성됨, 취소됨 등)
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private OrderStatus status;

  // 주문 항목 (한 주문에 여러 상품이 있을 수 있음)
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> orderItems = new ArrayList<>();

  // 배송 정보: 1:1 관계로 주문 하나에 배송 정보 하나
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

  // == 비즈니스 연관 메서드 ==

  // 주문 항목 추가 (양방향 편의 메서드)
  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  // 배송 설정 (양방향 편의 메서드)
  public void setDeliveryInfo(Delivery delivery) {
    this.delivery = delivery;
    delivery.setMember(this.member);
    // (필요에 따라 delivery 쪽에 주문을 가리키는 필드를 두고 설정 가능)
  }

  // 주문 생성 팩터리 메서드
  public static Order createOrder(Member member, Delivery delivery, List<OrderItem> orderItems) {
    Order order = new Order();
    order.setMember(member);
    order.setOrderDate(LocalDateTime.now());
    order.setStatus(OrderStatus.CREATED);
    order.setDeliveryInfo(delivery);
    for (OrderItem oi : orderItems) {
      order.addOrderItem(oi);
    }
    return order;
  }

  // 총 주문 금액 계산
  public int getTotalPrice() {
    return orderItems.stream()
        .mapToInt(OrderItem::getTotalPrice)
        .sum();
  }

  // 주문 취소 로직 예시
  public void cancel() {
    // 배송이 이미 완료된 경우 취소 불가 (예시)
    if (delivery.getStatus() == DeliveryStatus.DELIVERED) {
      throw new IllegalStateException("이미 배송 완료된 주문은 취소할 수 없습니다.");
    }
    this.status = OrderStatus.CANCELLED;

    // 주문 항목의 로직도 취소 처리
    for (OrderItem oi : orderItems) {
      oi.cancel();  // OrderItem 쪽에 cancel 로직이 있다고 가정
    }
  }
}