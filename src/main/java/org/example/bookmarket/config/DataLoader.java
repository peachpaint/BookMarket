package org.example.bookmarket.config;

import org.example.bookmarket.domain.Book;
import org.example.bookmarket.repository.BookJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//@Component
public class DataLoader implements CommandLineRunner {

  private final BookJpaRepository bookJpaRepository;

  public DataLoader(BookJpaRepository bookJpaRepository) {
    this.bookJpaRepository = bookJpaRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    // only insert initial data when table is empty (idempotent on restarts)
    if (bookJpaRepository.count() > 0) {
      return;
    }

    List<Book> initial = new ArrayList<>();

    Book b1 = new Book();
    b1.setBookId("ISBN1234");
    b1.setName("자바스크립트 입문");
    b1.setUnitPrice(new BigDecimal(30000));
    b1.setAuthor("조현영");
    b1.setDescription("자바스크립트의 기초부터 심화까지 핵심 문법을 학습한 후 12가지 프로그램을 만들며 학습한 내용을 확인할 수 있습니다." +
        " 문법 학습과 실습이 적절히 섞여 있어 프로그램을 만드는 방법을 재미있게 익힐 수 있습니다.");
    b1.setPublisher("길벗");
    b1.setCategory("IT");
    b1.setUnitsInStock(1000);
    b1.setReleaseDate("2024/02/20");
    initial.add(b1);

    Book b2 = new Book();
    b2.setBookId("ISBN5678");
    b2.setName("파이썬의 정석");
    b2.setUnitPrice(new BigDecimal(29800));
    b2.setAuthor("조용주,임좌상");
    b2.setDescription("4차 산업혁명의 핵심인 머신러닝, 사물 인터넷(IoT), " +
        "데이터 분석 등 다양한 분야에 활용되는 직관적이고 간결한 문법의 파이썬 프로그래밍 언어를 " +
        "최신 트렌드에 맞게 예제 중심으로 학습할 수 있습니다.");
    b2.setPublisher("길벗");
    b2.setCategory("IT");
    b2.setUnitsInStock(1000);
    b2.setReleaseDate("2023/01/10");
    initial.add(b2);

    Book b3 = new Book();
    b3.setBookId("ISBN9999");
    b3.setName("안드로이드 프로그래밍");
    b3.setUnitPrice(new BigDecimal(25000));
    b3.setAuthor("송미영");
    b3.setDescription("안드로이드의 기본 개념을 체계적으로 익히고, 이를 실습 예제를 통해 익힙니다. " +
        "기본 개념과 사용법을 스스로 실전에 적용하는 방법을 학습한 다음 실습 예제와 응용 예제를 통해 실전 프로젝트 응용력을 키웁니다.");
    b3.setPublisher("길벗");
    b3.setCategory("IT");
    b3.setUnitsInStock(1000);
    b3.setReleaseDate("2023/06/30");
    initial.add(b3);

    bookJpaRepository.saveAll(initial);
  }
}
