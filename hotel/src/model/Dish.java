package model;

/**
 * Représente un plat du menu du restaurant de l'hôtel.
 */
public class Dish {

    public enum Category { STARTER, MAIN_COURSE, DESSERT, DRINK }

    private int id;
    private String name;
    private Category category;
    private double price;

    public Dish() {}

    public Dish(int id, String name, Category category, double price) {
        this.id       = id;
        this.name     = name;
        this.category = category;
        this.price    = price;
    }

    // Getters & Setters
    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    public Category getCategory()               { return category; }
    public void setCategory(Category category)  { this.category = category; }

    public double getPrice()                    { return price; }
    public void setPrice(double price)          { this.price = price; }

    @Override
    public String toString() {
        return name + " (" + category + ") - " + price + "€";
    }
}
