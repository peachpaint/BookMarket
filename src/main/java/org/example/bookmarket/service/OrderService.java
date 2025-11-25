package org.example.bookmarket.service;

import org.example.bookmarket.domain.Address;
import org.example.bookmarket.domain.Book;
import org.example.bookmarket.domain.Delivery;
import org.example.bookmarket.domain.Member;
import org.example.bookmarket.domain.Order;
import org.example.bookmarket.domain.OrderItem;
import org.example.bookmarket.repository.BookJpaRepository;
import org.example.bookmarket.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private MemberService memberService;

  @Autowired
  private BookJpaRepository bookJpaRepository;

  private Map<Long, Order> listOfOrders = new HashMap<>();
//  private long nextOrderId = 2000;

  /**
   * 주문 저장 (DB + 메모리 캐시 + 재고 감소)
   * Method 3: 트랜잭션 내에서 재고 감소, Member 영속화, Order 저장을 모두 처리
   */
  public Long saveOrder(Order order) {
    System.out.println("=== Order Save with Stock Decrease ===");
    
    // 1. 재고 확인 및 감소 (먼저 처리)
    for (OrderItem orderItem : order.getOrderItems()) {
      Book book = orderItem.getBook();
      int quantity = orderItem.getQuantity();
      
      // DB에서 Book을 조회하여 영속 상태로 만들기
      Book persistentBook = bookJpaRepository.findById(book.getBookId())
          .orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다: " + book.getBookId()));
      
      System.out.println("Book: " + persistentBook.getName());
      System.out.println("  Before Stock: " + persistentBook.getUnitsInStock());
      
      // 재고 감소 (재고 부족 시 IllegalStateException 발생)
      persistentBook.decreaseStock(quantity);
      
      System.out.println("  After Stock: " + persistentBook.getUnitsInStock());
      
      // OrderItem의 Book을 영속 상태로 교체
      orderItem.setBook(persistentBook);
      
      // Book 저장 (변경 감지로 자동 UPDATE)
      bookJpaRepository.save(persistentBook);
    }
    
    // 2. Member를 트랜잭션 내에서 다시 조회 (영속 상태로 만들기)
    Member member = order.getMember();
    if (member != null && member.getMemberId() != null) {
      // DB에서 다시 조회하여 영속 상태의 Member로 교체
      Member persistentMember = memberService.findByMemberId(member.getMemberId());
      order.setMember(persistentMember);
      
      // Delivery의 Member도 교체
      if (order.getDelivery() != null) {
        Delivery delivery = order.getDelivery();
        delivery.setMember(persistentMember);
        
        // Delivery.deliveryAddress 처리 - persistentMember의 Address 재사용
        if (delivery.getDeliveryAddress() != null) {
          Address deliveryAddress = delivery.getDeliveryAddress();
          
          // persistentMember의 영속 상태 Address를 사용
          Address persistentAddress = persistentMember.getAddress();
          
          if (persistentAddress == null) {
            // 새 Address 생성 (처음 주문하는 회원)
            persistentAddress = new Address();
            persistentAddress.setMember(persistentMember);
            persistentAddress.setMemberId(persistentMember.getMemberId());
            persistentMember.setAddress(persistentAddress);
          }
          
          // 입력받은 주소 정보로 업데이트
          persistentAddress.setCountry(deliveryAddress.getCountry());
          persistentAddress.setZipcode(deliveryAddress.getZipcode());
          persistentAddress.setAddressname(deliveryAddress.getAddressname());
          persistentAddress.setDetailname(deliveryAddress.getDetailname());
          
          // Delivery에 영속 상태의 Address 설정
          delivery.setDeliveryAddress(persistentAddress);
        }
      }
    }
    
    // 3. DB에 주문 저장 (JPA가 자동으로 ID 생성)
    Order savedOrder = orderRepository.save(order);
    
    System.out.println("Order saved with ID: " + savedOrder.getId());
    System.out.println("===========================================");

    // 메모리 캐시에도 저장
    listOfOrders.put(savedOrder.getId(), savedOrder);

    return savedOrder.getId();
  }

//  /**
//   * 메모리 캐시에서 주문 조회 (빠른 조회)
//   */
//  public Order getOrderFromCache(Long orderId) {
//    return listOfOrders.get(orderId);
//  }

  /**
   * DB에서 주문 조회
   */
  public Order getOrder(Long orderId) {
    // 캐시 먼저 확인
    if (listOfOrders.containsKey(orderId)) {
      return listOfOrders.get(orderId);
    }

    // 캐시에 없으면 DB에서 조회
    Optional<Order> order = orderRepository.findById(orderId);
    if (order.isPresent()) {
      // DB에서 조회한 데이터를 캐시에 저장
      listOfOrders.put(orderId, order.get());
      return order.get();
    }

    return null;
  }

  /**
   * 모든 주문 조회 (DB)
   */
  public Iterable<Order> getAllOrders() {
    return orderRepository.findAll();
  }

//  /**
//   * 주문 삭제
//   */
//  public void deleteOrder(Long orderId) {
//    orderRepository.deleteById(orderId);
//    listOfOrders.remove(orderId);  // 캐시에서도 제거
//  }

//  /**
//   * 메모리 캐시 초기화
//   */
//  public void clearCache() {
//    listOfOrders.clear();
//  }

//  /**
//   * 다음 주문 ID 생성 (필요시 사용)
//   * OrderRepositoryImpl.getNextOrderId() 기능 대체
//   */
//  private synchronized long getNextOrderId() {
//    return nextOrderId++;
//  }

//  /**
//   * 캐시 크기 확인
//   */
//  public int getCacheSize() {
//    return listOfOrders.size();
//  }
}
