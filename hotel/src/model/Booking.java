package model;

import java.time.LocalDate;

/**
 * Représente une réservation de chambre.
 */
public class Booking {

    public enum BookingStatus { ACTIVE, CHECKED_OUT, CANCELLED }

    private int id;
    private Customer customer;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BookingStatus status;
    private double totalAmount;

    public Booking() {}

    public Booking(Customer customer, Room room, LocalDate checkIn, LocalDate checkOut) {
        this.customer = customer;
        this.room     = room;
        this.checkIn  = checkIn;
        this.checkOut = checkOut;
        this.status   = BookingStatus.ACTIVE;
        this.totalAmount = calculateTotal();
    }

    public double calculateTotal() {
        if (checkIn == null || checkOut == null || room == null) return 0;
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        return nights * room.getPricePerNight();
    }

    // Getters & Setters
    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }

    public Customer getCustomer()                   { return customer; }
    public void setCustomer(Customer customer)      { this.customer = customer; }

    public Room getRoom()                           { return room; }
    public void setRoom(Room room)                  { this.room = room; }

    public LocalDate getCheckIn()                   { return checkIn; }
    public void setCheckIn(LocalDate checkIn)       { this.checkIn = checkIn; }

    public LocalDate getCheckOut()                  { return checkOut; }
    public void setCheckOut(LocalDate checkOut)     { this.checkOut = checkOut; }

    public BookingStatus getStatus()                { return status; }
    public void setStatus(BookingStatus status)     { this.status = status; }

    public double getTotalAmount()                  { return totalAmount; }
    public void setTotalAmount(double totalAmount)  { this.totalAmount = totalAmount; }

    public long getNights() {
        if (checkIn == null || checkOut == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    @Override
    public String toString() {
        return "Booking{id=" + id + ", customer=" + (customer != null ? customer.getFullName() : "?")
                + ", room=" + (room != null ? room.getRoomNumber() : "?")
                + ", " + checkIn + " → " + checkOut + "}";
    }
}
