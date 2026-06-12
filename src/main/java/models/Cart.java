package models;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import dao.ProductDAO;

public class Cart {
    private int cartId;
    private int userId;
    private Map<Integer, CartItem> items;

    public Cart(int cartId, int userId) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = new ConcurrentHashMap<>();
    }

    public synchronized void addItem(int productId, int quantity) {
        ProductDAO dao = new ProductDAO();
        Product product = dao.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (quantity <= 0 || quantity > product.getStock()) {
            throw new IllegalArgumentException("Invalid quantity. Stock available: " + product.getStock());
        }
        CartItem item = new CartItem(product, quantity);
        items.put(productId, item);
    }

    public synchronized void updateQuantity(int productId, int quantity) {
        CartItem item = items.get(productId);
        if (item == null) {
            throw new IllegalArgumentException("Product not found in the cart.");
        }
        Product product = item.getProduct();
        if (quantity <= 0 || quantity > product.getStock()) {
            throw new IllegalArgumentException("Invalid quantity. Stock available: " + product.getStock());
        }
        item.setQuantity(quantity);
    }

    public synchronized void removeItem(int productId) {
        items.remove(productId);
    }

    public double getTotalPrice() {
        return items.values().stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    public int getCartId() { return cartId; }
    public int getUserId() { return userId; }
    public void setCartId(int cartId) { this.cartId = cartId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Map<Integer, CartItem> getItems() { return items; }
    public void clearCart() { items.clear(); }
    public boolean containsProduct(int productId) { return items.containsKey(productId); }
    public int getId() { return cartId; }
    public Product lookup(int productId) { CartItem item = items.get(productId); return item != null ? item.getProduct() : null; }
    public int getQuantity(int productId) { CartItem item = items.get(productId); return item != null ? item.getQuantity() : 0; }
}