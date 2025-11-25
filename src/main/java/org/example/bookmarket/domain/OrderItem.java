package org.example.bookmarket.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 어떤 상품인지 (예: Book 엔티티가 있을 것이라 가정)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  // 이 항목이 속한 주문
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  // 주문 시점의 가격 (책 가격 + 할인 등 고려)
  @Column(name = "order_price", nullable = false)
  private int orderPrice;

  // 주문 수량
  @Column(name = "quantity", nullable = false)
  private int quantity;

  // == 비즈니스 메서드 ==

  // 총 가격 계산
  public int getTotalPrice() {
    return orderPrice * quantity;
  }

  // 취소 시 수량 반환 등 비즈니스 로직이 있을 수 있음
  public void cancel() {
    // 예: 재고 수량 다시 늘리기
    book.increaseStock(quantity);
  }

  // 생성 메서드
  public static OrderItem createOrderItem(Book book, int orderPrice, int quantity) {
    OrderItem oi = new OrderItem();
    oi.setBook(book);
    oi.setOrderPrice(orderPrice);
    oi.setQuantity(quantity);
    book.decreaseStock(quantity);  // 재고 줄이기
    return oi;
  }
}
