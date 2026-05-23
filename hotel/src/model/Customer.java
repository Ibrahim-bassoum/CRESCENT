package model;

/**
 * Représente un client de l'hôtel.
 */
public class Customer {

    private int id;
    private String fullName;
    private String address;
    private String phone;
    private String email;

    public Customer() {}

    public Customer(int id, String fullName, String address, String phone, String email) {
        this.id       = id;
        this.fullName = fullName;
        this.address  = address;
        this.phone    = phone;
        this.email    = email;
    }

    // Getters & Setters
    public int getId()                        { return id; }
    public void setId(int id)                 { this.id = id; }

    public String getFullName()               { return fullName; }
    public void setFullName(String fullName)  { this.fullName = fullName; }

    public String getAddress()                { return address; }
    public void setAddress(String address)    { this.address = address; }

    public String getPhone()                  { return phone; }
    public void setPhone(String phone)        { this.phone = phone; }

    public String getEmail()                  { return email; }
    public void setEmail(String email)        { this.email = email; }

    @Override
    public String toString() {
        return "Customer{id=" + id + ", name='" + fullName + "', phone='" + phone + "'}";
    }
}
