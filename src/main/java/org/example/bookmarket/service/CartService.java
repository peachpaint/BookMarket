package org.example.bookmarket.service;

import org.example.bookmarket.domain.Cart;

public interface CartService {
  Cart create(Cart cart);
  Cart read(String cartId);
  void update(String cartId, Cart cart);
  void delete(String cartId);
}
