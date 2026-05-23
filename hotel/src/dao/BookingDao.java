package dao;

import config.DatabaseConnection;
import model.Booking;
import model.Booking.BookingStatus;
import model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO pour la gestion des réservations de chambres.
 */
public class BookingDao implements Dao<Booking, Integer> {

    private final CustomerDao customerDao = new CustomerDao();
    private final RoomDao     roomDao     = new RoomDao();

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Booking booking) {
        String sql = "INSERT INTO bookings (customer_id, room_number, check_in, check_out, status, total_amount) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getCustomer().getId());
            ps.setInt(2, booking.getRoom().getRoomNumber());
            ps.setDate(3, Date.valueOf(booking.getCheckIn()));
            ps.setDate(4, Date.valueOf(booking.getCheckOut()));
            ps.setString(5, booking.getStatus().name());
            ps.setDouble(6, booking.getTotalAmount());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) booking.setId(keys.getInt(1));
            }
            // Mettre la chambre en statut OCCUPIED
            booking.getRoom().setStatus(Room.Status.OCCUPIED);
            roomDao.update(booking.getRoom());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la réservation.", e);
        }
    }

    @Override
    public void update(Booking booking) {
        String sql = "UPDATE bookings SET customer_id=?, room_number=?, check_in=?, check_out=?, status=?, total_amount=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, booking.getCustomer().getId());
            ps.setInt(2, booking.getRoom().getRoomNumber());
            ps.setDate(3, Date.valueOf(booking.getCheckIn()));
            ps.setDate(4, Date.valueOf(booking.getCheckOut()));
            ps.setString(5, booking.getStatus().name());
            ps.setDouble(6, booking.getTotalAmount());
            ps.setInt(7, booking.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la réservation.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM bookings WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la réservation.", e);
        }
    }

    @Override
    public Optional<Booking> findById(Integer id) {
        String sql = "SELECT * FROM bookings WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la réservation.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings ORDER BY check_in DESC";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des réservations.", e);
        }
        return list;
    }

    /**
     * Retourne les réservations actives.
     */
    public List<Booking> findActive() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE status='ACTIVE' ORDER BY check_in";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des réservations actives.", e);
        }
        return list;
    }

    /**
     * Effectue le checkout : libère la chambre, met le statut à CHECKED_OUT.
     */
    public void checkout(Booking booking) {
        booking.setStatus(BookingStatus.CHECKED_OUT);
        update(booking);
        booking.getRoom().setStatus(Room.Status.AVAILABLE);
        roomDao.update(booking.getRoom());
    }

    private Booking mapRow(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setCheckIn(rs.getDate("check_in").toLocalDate());
        booking.setCheckOut(rs.getDate("check_out").toLocalDate());
        booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
        booking.setTotalAmount(rs.getDouble("total_amount"));

        customerDao.findById(rs.getInt("customer_id")).ifPresent(booking::setCustomer);
        roomDao.findById(rs.getInt("room_number")).ifPresent(booking::setRoom);

        return booking;
    }
}
