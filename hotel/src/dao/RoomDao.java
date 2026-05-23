package dao;

import config.DatabaseConnection;
import model.Room;
import model.Room.BedType;
import model.Room.RoomType;
import model.Room.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO pour la gestion des chambres.
 */
public class RoomDao implements Dao<Room, Integer> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Room room) {
        String sql = "INSERT INTO rooms (room_number, room_type, bed_type, price_per_night, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType().name());
            ps.setString(3, room.getBedType().name());
            ps.setDouble(4, room.getPricePerNight());
            ps.setString(5, room.getStatus().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la chambre.", e);
        }
    }

    @Override
    public void update(Room room) {
        String sql = "UPDATE rooms SET room_type=?, bed_type=?, price_per_night=?, status=? WHERE room_number=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, room.getRoomType().name());
            ps.setString(2, room.getBedType().name());
            ps.setDouble(3, room.getPricePerNight());
            ps.setString(4, room.getStatus().name());
            ps.setInt(5, room.getRoomNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la chambre.", e);
        }
    }

    @Override
    public void delete(Integer roomNumber) {
        String sql = "DELETE FROM rooms WHERE room_number=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la chambre.", e);
        }
    }

    @Override
    public Optional<Room> findById(Integer roomNumber) {
        String sql = "SELECT * FROM rooms WHERE room_number=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la chambre.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY room_number";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) rooms.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des chambres.", e);
        }
        return rooms;
    }

    /**
     * Retourne uniquement les chambres disponibles.
     */
    public List<Room> findAvailable() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status='AVAILABLE' ORDER BY room_number";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) rooms.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des chambres disponibles.", e);
        }
        return rooms;
    }

    private Room mapRow(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomNumber(rs.getInt("room_number"));
        room.setRoomType(RoomType.valueOf(rs.getString("room_type")));
        room.setBedType(BedType.valueOf(rs.getString("bed_type")));
        room.setPricePerNight(rs.getDouble("price_per_night"));
        room.setStatus(Status.valueOf(rs.getString("status")));
        return room;
    }
}
