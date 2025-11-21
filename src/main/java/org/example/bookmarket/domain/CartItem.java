package org.example.bookmarket.domain;

import lombok.Data;
import lombok.ToString;
import java.math.BigDecimal;

@Data
@ToString
public class CartItem {
  private Book book;
  private int quantity;//도서 개수
  private BigDecimal totalPrice;//도서 가격

  public CartItem(Book book) {
    super();//자식 클래스에서 부모(조상) 클래스의 생성자를 호출하는 데 사용되는 자바 문법
    this.book = book;
    this.quantity = 1;
    this.totalPrice = book.getUnitPrice();
  }
  public void setBook(Book book) {
    this.book = book;
    this.updateTotalPrice();
  }
  public void setQuantity(int quantity) {
    this.quantity = quantity;
    this.updateTotalPrice();
  }
  public void updateTotalPrice() {
    totalPrice =this.book.getUnitPrice().multiply(new BigDecimal(this.quantity));
  }
}
