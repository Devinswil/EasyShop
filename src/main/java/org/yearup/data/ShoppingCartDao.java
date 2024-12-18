package org.yearup.data;

import org.yearup.models.ShoppingCart;

import java.util.List;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
  List<ShoppingCart> getAllProducts(int productId, int quantity);
  ShoppingCart addToCart(int productId, int quantity);
  void updateShoppingCart(int userId, ShoppingCart shoppingCart);
  void deleteItem(int userId);

}
