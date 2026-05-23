package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une commande restaurant d'un client.
 */
public class Order {

    public enum OrderStatus { PENDING, SERVED, PAID }

    private int id;
    private Customer customer;
    private List<OrderItem> items;
    private LocalDateTime orderTime;
    private OrderStatus status;

    public Order() {
        this.items     = new ArrayList<>();
        this.orderTime = LocalDateTime.now();
        this.status    = OrderStatus.PENDING;
    }

    public Order(Customer customer) {
        this();
        this.customer = customer;
    }

    public void addItem(Dish dish, int quantity) {
        // Si le plat existe déjà, incrémenter la quantité
        for (OrderItem item : items) {
            if (item.getDish().getId() == dish.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new OrderItem(dish, quantity));
    }

    public double getTotal() {
        return items.stream()
                .mapToDouble(item -> item.getDish().getPrice() * item.getQuantity())
                .sum();
    }

    // Getters & Setters
    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }

    public Customer getCustomer()                   { return customer; }
    public void setCustomer(Customer customer)      { this.customer = customer; }

    public List<OrderItem> getItems()               { return items; }
    public void setItems(List<OrderItem> items)     { this.items = items; }

    public LocalDateTime getOrderTime()             { return orderTime; }
    public void setOrderTime(LocalDateTime t)       { this.orderTime = t; }

    public OrderStatus getStatus()                  { return status; }
    public void setStatus(OrderStatus status)       { this.status = status; }

    // Classe interne
    public static class OrderItem {
        private Dish dish;
        private int quantity;

        public OrderItem(Dish dish, int quantity) {
            this.dish     = dish;
            this.quantity = quantity;
        }

        public Dish getDish()                    { return dish; }
        public void setDish(Dish dish)           { this.dish = dish; }
        public int getQuantity()                 { return quantity; }
        public void setQuantity(int quantity)    { this.quantity = quantity; }
        public double getSubtotal()              { return dish.getPrice() * quantity; }
    }
}
