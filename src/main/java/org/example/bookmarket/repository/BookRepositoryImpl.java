package org.example.bookmarket.repository;

import org.example.bookmarket.domain.Book;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//사용 X
@Deprecated
public class BookRepositoryImpl{
  private List<Book> listOfBooks = new ArrayList<Book>();
  public BookRepositoryImpl(){

  }
  public List<Book> getAllBookList(){
    return listOfBooks;
  }//저장된 모든 도서 목록을 가져오는 메서드

  public Book getBookById(String bookId){
    Book bookInfo = null;
    for(int i=0;i< listOfBooks.size();i++){
      Book book = listOfBooks.get(i);
      if(book!=null && book.getBookId() !=null && book.getBookId().equals(bookId)) {
        bookInfo = book;
        break;
      }
    }
    if(bookInfo == null){
      throw new IllegalArgumentException("도서ID가"+bookId+"인 해당 도서를 찾을 수 없습니다");
    }
    return bookInfo;
  }//상세페이지 도서정보를 가져오는 메서드

  public List<Book> getBookListByCategory(String category){
    List<Book> booksByCategory = new ArrayList<Book>();
    for(int i=0;i< listOfBooks.size();i++){
      Book book = listOfBooks.get(i);
      if(category.equalsIgnoreCase(book.getCategory()))
        booksByCategory.add(book);
    }
    return booksByCategory;
  }

  public void setNewBook(Book book){
    listOfBooks.add(book);
  }
}
