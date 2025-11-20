package org.example.bookmarket.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.bookmarket.validator.BookId;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
//BigDecimal : 부동 소수점 연산의 정확도 문제를 해결하기 위해 사용되는 클래스
// ex)  화폐 계산, 금융 거래 => 미세한 오차를 방지,매우 큰 숫자를 정밀하게 다루기 위해 필수

@Entity
@Data//@Data = @Getter + @Setter + @NoArgsConstructor 포함
public class Book {
  @Id
  @Column(name = "book_id", length = 64)
  @BookId
  @Pattern(regexp = "ISBN[1-9]+", message = "{Pattern.book.bookId}")
  private String bookId;//도서 Id

  @Column(nullable = false)
  @Size(min=4, max = 50, message = "{Size.book.name}")
  private String name;//도서 제목

  @Column(name = "unit_price", precision = 19, scale = 2)
  @Min(value = 0, message = "{Min.book.unitPrice}")
  @NotNull(message = "{NotNull.book.unitPrice}")
  private BigDecimal unitPrice;//가격

  private String author;//저자

  @Column(columnDefinition = "TEXT")
  private String description;//설명

  private String publisher;//출판사
  private String category;//분류

  @Min(value = 0, message = "{Min.book.unitsInStock}")
  private long unitsInStock;//재고수
  private String releaseDate;//출판일

  @Column(name = "`condition`")
  private String condition;//신규도서 or 중고도서 or 전자책

  private String fileName;//도서 이미지 파일
  @Transient  // JPA가 이 필드를 DB에 매핑하지 않음
  private MultipartFile bookimage;//도서 이미지

  public Book() {
    super();
  }


}
