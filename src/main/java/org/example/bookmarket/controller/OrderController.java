package org.example.bookmarket.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.bookmarket.domain.*;
import org.example.bookmarket.service.CartService;
import org.example.bookmarket.service.OrderService;
import org.example.bookmarket.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @Autowired
  private CartService cartService;
  
  @Autowired
  private MemberService memberService;

  //장바구니 주문 생성
  @GetMapping("/{cartId}")
  public String requestCartList(@PathVariable(value = "cartId") String cartId, Principal principal, HttpSession session) {
    Cart cart = cartService.read(cartId);
    Order order = new Order();
    
    // 임시 Order ID 자동 생성 (세션 ID + 타임스탬프 기반)
    String tempOrderId = "ORD-" + session.getId() + "-" + System.currentTimeMillis();
    order.setTempOrderId(tempOrderId);
    
    // Order 필수 필드 초기화
    order.setOrderDate(java.time.LocalDateTime.now());
    order.setStatus(OrderStatus.CREATED);
    
    System.out.println("===========================================");
    System.out.println("=== Order Created ===");
    System.out.println("Temp Order ID Generated: " + tempOrderId);
    System.out.println("Order Date: " + order.getOrderDate());
    System.out.println("Order Status: " + order.getStatus());
    System.out.println("Session ID: " + session.getId());
    System.out.println("Timestamp: " + System.currentTimeMillis());
    System.out.println("Order.getTempOrderId(): " + order.getTempOrderId());
    System.out.println("===========================================");
    
    List<Book> listOfBooks = new ArrayList<>();

    for (CartItem item : cart.getCartItems().values()) {
      Book book = item.getBook();
      int quantity = item.getQuantity();
      int orderPrice = book.getUnitPrice().intValue();
      
      // OrderItem 생성 (재고 감소는 아직 하지 않음)
      OrderItem orderItem = new OrderItem();
      orderItem.setBook(book);
      orderItem.setQuantity(quantity);
      orderItem.setOrderPrice(orderPrice);
      
      listOfBooks.add(book);
      order.addOrderItem(orderItem);
      
      System.out.println("Processing cart item: " + book.getName());
      System.out.println("  - Quantity: " + quantity);
      System.out.println("  - Price: " + orderPrice);
      System.out.println("  - Current Stock: " + book.getUnitsInStock());
    }
    
    // Option 3: 로그인된 사용자 정보로 초기화 (로그인되어 있는 경우)
    if (principal != null) {
      try {
        Member loggedInMember = memberService.findByMemberId(principal.getName());
        order.setMember(loggedInMember);
      } catch (IllegalArgumentException e) {
        // 회원 정보를 찾을 수 없는 경우 빈 Member 객체 생성
        order.setMember(new Member());
      }
    } else {
      // 로그인하지 않은 경우 빈 Member 객체 생성 (수동 입력 가능)
      order.setMember(new Member());
    }
    
    order.setDelivery(new Delivery());
    
    // 세션에 주문 정보 저장
    session.setAttribute("currentOrder", order);
    session.setAttribute("bookList", listOfBooks);
    
    return "redirect:/orders/orderCustomerInfo" ;
  }

  // 2. 고객 정보 입력 폼
  @GetMapping("/orderCustomerInfo")
  public String requestCustomerInfoForm(Principal principal, HttpSession session, Model model) {
    Order order = (Order) session.getAttribute("currentOrder");
    
    // 세션에 주문 정보가 없으면 장바구니로 리다이렉트
    if (order == null) {
      return "redirect:/cart";
    }
    
    // Option 1: 이미 order에 member가 설정되어 있지 않거나 비어있는 경우, Principal에서 다시 가져오기
    if (principal != null && (order.getMember() == null || order.getMember().getMemberId() == null)) {
      try {
        Member loggedInMember = memberService.findByMemberId(principal.getName());
        order.setMember(loggedInMember);
      } catch (IllegalArgumentException e) {
        // 회원 정보를 찾을 수 없는 경우 빈 Member 객체 유지
      }
    }
    
    // member 정보가 여전히 없으면 빈 객체 생성
    if (order.getMember() == null) {
      order.setMember(new Member());
    }
    
    // 세션에 업데이트된 order 저장
    session.setAttribute("currentOrder", order);
    
    model.addAttribute("member", order.getMember());
    model.addAttribute("isLoggedIn", principal != null);
    return "orderCustomerInfo";
  }

  // 3. 고객 정보 저장
  @PostMapping("/orderCustomerInfo")
  public String requestCustomerInfo(@RequestParam String memberId,
                                    @RequestParam String name,
                                    @RequestParam String phone,
                                    @RequestParam(required = false) String city,
                                    Principal principal,
                                    HttpSession session) {
    Order order = (Order) session.getAttribute("currentOrder");
    
    if (order == null) {
      return "redirect:/cart";
    }
    
    Member member;
    
    // 로그인된 사용자면 DB에서 영속 상태 Member 조회
    if (principal != null && principal.getName().equals(memberId)) {
      try {
        // DB에서 조회 (영속 상태) - 새로 저장되지 않고 참조만 됨
        member = memberService.findByMemberId(memberId);
        
        // 일부 정보만 업데이트 (업데이트는 트랜잭션 내에서 자동 반영)
        member.setName(name);
        member.setPhone(phone);
        member.setCity(city);
        
        System.out.println("=== Logged-in Member ===");
        System.out.println("MemberId: " + member.getMemberId());
        System.out.println("Persistent: true");
        
      } catch (Exception e) {
        System.out.println("Failed to load member from DB: " + e.getMessage());
        return "redirect:/login";
      }
    } else {
      // 비로그인 게스트 주문 - 공통 게스트 Member 사용
      member = getOrCreateGuestMember();
      
      // 게스트 주문 시 주문자 정보는 Delivery에만 저장
      // Member는 공통 GUEST 계정 사용
      System.out.println("=== Guest Order ===");
      System.out.println("Using common GUEST member");
      System.out.println("Customer Name: " + name);
    }
    
    order.setMember(member);
    
    // 게스트인 경우 실제 주문자 정보를 세션에 임시 저장 (Delivery에서 사용)
    if ("GUEST".equals(member.getRole())) {
      session.setAttribute("guestName", name);
      session.setAttribute("guestPhone", phone);
      session.setAttribute("guestCity", city);
    }
    
    session.setAttribute("currentOrder", order);
    
    System.out.println("Role: " + member.getRole());
    System.out.println("===========================");
    
    return "redirect:/orders/orderDeliveryInfo";
  }
  
  // 게스트 전용 Member 가져오기 또는 생성
  private Member getOrCreateGuestMember() {
    try {
      return memberService.findByMemberId("GUEST");
    } catch (Exception e) {
      // GUEST Member가 없으면 생성
      Member guest = new Member();
      guest.setMemberId("GUEST");
      guest.setPasswd("GUEST_PASSWORD");
      guest.setName("Guest");
      guest.setPhone("000-0000-0000");
      guest.setCity("-");
      guest.setRole("GUEST");
      return memberService.saveMember(guest);
    }
  }

  // 4. 배송 정보 입력 폼
  @GetMapping("/orderDeliveryInfo")
  public String requestDeliveryInfoForm(HttpSession session, Model model) {
    Order order = (Order) session.getAttribute("currentOrder");
    
    if (order == null) {
      return "redirect:/cart";
    }
    
    // Address 정보도 함께 전달
    Address address = new Address();
    model.addAttribute("delivery", order.getDelivery());
    model.addAttribute("address", address);
    return "orderDeliveryInfo";
  }

  // 5. 배송 정보 저장
  @PostMapping("/orderDeliveryInfo")
  public String requestDeliveryInfo(@Valid @ModelAttribute Delivery delivery,
                                    @ModelAttribute Address addressInput,
                                    BindingResult bindingResult,
                                    HttpSession session, Model model) {
    Order order = (Order) session.getAttribute("currentOrder");
    
    if (order == null) {
      return "redirect:/cart";
    }
    
    if (bindingResult.hasErrors()) {
      return "orderDeliveryInfo";
    }
    
    Member member = order.getMember();
    
    // Member가 null인지 확인
    if (member == null || member.getMemberId() == null) {
      model.addAttribute("error", "주문자 정보가 없습니다. 다시 시도해주세요.");
      return "redirect:/orders/orderCustomerInfo";
    }
    
    // Member를 DB에서 다시 조회 (영속 상태로 만들기)
    Member persistentMember = memberService.findByMemberId(member.getMemberId());
    order.setMember(persistentMember);
    member = persistentMember;
    
    // 기존 Address 재사용 또는 새로 생성
    Address address = member.getAddress();
    boolean isNewAddress = false;
    
    if (address == null) {
      // 새 Address 생성
      address = new Address();
      address.setMember(member);
      address.setMemberId(member.getMemberId());
      member.setAddress(address);  // 양방향 관계 설정
      isNewAddress = true;
      System.out.println("Creating new Address for member: " + member.getMemberId());
    } else {
      System.out.println("Reusing existing Address for member: " + member.getMemberId());
    }
    
    // 입력받은 주소 정보로 업데이트
    address.setCountry(addressInput.getCountry());
    address.setZipcode(addressInput.getZipcode());
    address.setAddressname(addressInput.getAddressname());
    address.setDetailname(addressInput.getDetailname());
    
    // Delivery 설정
    delivery.setDeliveryAddress(address);
    delivery.setMember(member);
    
    // 게스트인 경우 실제 주문자 정보를 recipientName에 설정
    if ("GUEST".equals(member.getRole())) {
      String guestName = (String) session.getAttribute("guestName");
      if (guestName != null && !guestName.isEmpty()) {
        delivery.setRecipientName(guestName);
      }
    }
    
    delivery.setDeliveryDate(java.time.LocalDateTime.now().plusDays(3));
    delivery.setStatus(DeliveryStatus.READY);
    
    order.setDelivery(delivery);
    session.setAttribute("currentOrder", order);
    
    System.out.println("=== Delivery Info Saved ===");
    System.out.println("Member: " + member.getMemberId());
    System.out.println("Address is new: " + isNewAddress);
    System.out.println("Recipient: " + delivery.getRecipientName());
    System.out.println("Address: " + address.getAddressname());
    System.out.println("===========================");
    
    return "redirect:/orders/orderConfirmation";
  }

  // 6. 주문 확인
  @GetMapping("/orderConfirmation")
  @SuppressWarnings("unchecked")
  public String requestConfirmation(HttpSession session, Model model) {
    Order order = (Order) session.getAttribute("currentOrder");
    List<Book> listOfBooks = (List<Book>) session.getAttribute("bookList");
    
    // 세션에 주문 정보가 없으면 장바구니로 리다이렉트
    if (order == null) {
      return "redirect:/cart";
    }
    
    model.addAttribute("bookList", listOfBooks);
    System.out.println("listOfBooks ==>" + listOfBooks);
    model.addAttribute("order", order);
    System.out.println("order ==>" + order);
    return "orderConfirmation";
  }

  // 7. 주문 확정
  @PostMapping("/orderConfirmation")
  public String requestConfirmationFinished(HttpSession session, Model model) {
    Order order = (Order) session.getAttribute("currentOrder");
    
    if (order == null) {
      return "redirect:/cart";
    }
    
    try {
      // DB에 주문 저장 (재고 감소 포함)
      Long orderId = orderService.saveOrder(order);
      order.setId(orderId);
      session.setAttribute("currentOrder", order);
      model.addAttribute("order", order);
      return "redirect:/orders/orderFinished";
    } catch (IllegalStateException e) {
      // 재고 부족 등의 오류 처리
      model.addAttribute("error", e.getMessage());
      return "orderConfirmation";
    } catch (IllegalArgumentException e) {
      // 책을 찾을 수 없는 경우
      model.addAttribute("error", e.getMessage());
      return "orderConfirmation";
    }
  }

  // 8. 주문 완료
  @GetMapping("/orderFinished")
  public String requestFinished(HttpSession session, Model model) {
    Order order = (Order) session.getAttribute("currentOrder");
    
    if (order == null) {
      return "redirect:/cart";
    }
    
    model.addAttribute("order", order);
    
    // 주문 완료 후 세션의 주문 정보만 삭제
    session.removeAttribute("currentOrder");
    session.removeAttribute("bookList");
    
    return "orderFinished";
  }

  // 9. 주문 취소
  @GetMapping("/orderCancelled")
  public String requestCancelled(HttpSession session) {
    // 주문 취소 시 세션의 주문 정보 삭제
    session.removeAttribute("currentOrder");
    session.removeAttribute("bookList");
    return "orderCancelled";
  }
}
