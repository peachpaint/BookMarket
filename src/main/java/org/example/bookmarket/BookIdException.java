package org.example.bookmarket;

@SuppressWarnings("serial")
public class BookIdException extends RuntimeException {
  private final String bookId;  // final로 불변성 보장

  public BookIdException(String bookId) {
    super("도서 ID '" + bookId + "'를 찾을 수 없습니다.");  // 부모 클래스에 메시지 전달
    this.bookId = bookId;
  }

  public String getBookId() {
    return bookId;
  }
}
