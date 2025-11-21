package org.example.bookmarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.bookmarket.BookIdException;
import org.example.bookmarket.domain.Book;
import org.example.bookmarket.domain.Cart;
import org.example.bookmarket.domain.CartItem;
import org.example.bookmarket.service.BookService;
import org.example.bookmarket.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/cart")
public class CartController {
  @Autowired
  private CartService cartService;
  @Autowired
  private BookService bookService;

  @GetMapping
  public String requestCartId(HttpServletRequest request){
    System.out.println("aaaa");
    String sessionid = request.getSession().getId();
    return "redirect:/cart/"+sessionid;
  }
  @PostMapping
  public @ResponseBody Cart create(@RequestBody Cart cart){
    System.out.println("bbb");
    return cartService.create(cart);
  }
  @GetMapping("/{cartId}")
  public String requestCartList(@PathVariable(value = "cartId") String cartId, Model model){
    System.out.println("cccc");
    Cart cart = cartService.read(cartId);
    if(cart == null) {
      cart = new Cart(cartId);  // 빈 장바구니 생성
    }
    model.addAttribute("cart",cart);
    return "cart";
  }
  @PutMapping("/{cartId}")
  public @ResponseBody Cart read(@PathVariable(value = "cartId")String cartId){
    System.out.println("dddd");
    return cartService.read(cartId);
  }
  @PutMapping("/book/{bookId}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void addCartByNewItem(@PathVariable("bookId")String bookId, HttpServletRequest request){
    String sessionId = request.getSession().getId();
    Cart cart = cartService.read(sessionId);
    if(cart == null)
      cart = cartService.create(new Cart(sessionId));
    Book book = bookService.getBookById(bookId);
    if (book == null)
      throw new IllegalArgumentException(new BookIdException(bookId));
    cart.addCartItem(new CartItem(book));
    cartService.update(sessionId, cart);
  }
  @DeleteMapping("/book/{bookId}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void removeCartByItem(@PathVariable("bookId")String bookId, HttpServletRequest request){
    String sessionId = request.getSession(true).getId();//장바구니 ID를 가져옴
    Cart cart = cartService.read(sessionId);//장바구니 내 모든 정보를 가져옴
    if (cart == null)
      cart = cartService.create(new Cart(sessionId));
    Book book = bookService.getBookById(bookId);//bookId 정보를 가져옴
    if (book == null)
      throw new IllegalArgumentException(new BookIdException(bookId));
    cart.removeCartItem(new CartItem(book));//장바구니 내에 bookId 도서 삭제
    cartService.update(sessionId, cart);//정바구니를 새로 갱신
  }
  @DeleteMapping("/{cartId}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void deleteCartList(@PathVariable("cartId")String cartId){
    cartService.delete(cartId);
  }
}
