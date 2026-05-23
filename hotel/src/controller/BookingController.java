package controller;

import dao.BookingDao;
import model.Booking;
import model.Customer;
import model.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur gérant la logique métier des réservations.
 */
public class BookingController {

    private final BookingDao bookingDao = new BookingDao();

    public Booking createBooking(Customer customer, Room room, LocalDate checkIn, LocalDate checkOut) {
        validateDates(checkIn, checkOut);

        if (!room.isAvailable()) {
            throw new IllegalStateException("La chambre " + room.getRoomNumber() + " n'est pas disponible.");
        }

        Booking booking = new Booking(customer, room, checkIn, checkOut);
        bookingDao.save(booking);
        return booking;
    }

    public void checkout(int bookingId) {
        Optional<Booking> opt = bookingDao.findById(bookingId);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Réservation introuvable.");
        }
        Booking booking = opt.get();
        if (booking.getStatus() != Booking.BookingStatus.ACTIVE) {
            throw new IllegalStateException("Cette réservation n'est plus active.");
        }
        bookingDao.checkout(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingDao.findAll();
    }

    public List<Booking> getActiveBookings() {
        return bookingDao.findActive();
    }

    public Optional<Booking> getBooking(int id) {
        return bookingDao.findById(id);
    }

    public void cancelBooking(int id) {
        Optional<Booking> opt = bookingDao.findById(id);
        if (opt.isEmpty()) throw new IllegalArgumentException("Réservation introuvable.");
        Booking booking = opt.get();
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.getRoom().setStatus(Room.Status.AVAILABLE);
        bookingDao.update(booking);
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Les dates sont obligatoires.");
        }
        if (!checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("La date de départ doit être après la date d'arrivée.");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date d'arrivée ne peut pas être dans le passé.");
        }
    }
}
