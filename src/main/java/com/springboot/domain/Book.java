package com.springboot.domain;

import java.math.BigDecimal;
//BigDecimal : 부동 소수점 연산의 정확도 문제를 해결하기 위해 사용되는 클래스
// ex)  화폐 계산, 금융 거래 => 미세한 오차를 방지,매우 큰 숫자를 정밀하게 다루기 위해 필수

public class Book {
  private String bookId;//도서 Id
  private String name;//도서 제목
  private BigDecimal unitPrice;//가격
  private String author;//저자
  private String description;//설명
  private String publisher;//출판사
  private String category;//분류
  private long unitsInStock;//재고수
  private String releaseDate;//출판일
  private String condition;//신규도서 or 중고도서 or 전자책

  public Book() {
    super();
  }

  //Getter, Setter 단축키 => Alt + Ins
  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    bookId = bookId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(BigDecimal price) {
    this.unitPrice = unitPrice;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public long getUnitsInStock() {
    return unitsInStock;
  }

  public void setUnitsInStock(long unitsInStock) {
    this.unitsInStock = unitsInStock;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }
}
