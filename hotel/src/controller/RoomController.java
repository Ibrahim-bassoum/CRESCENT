package controller;

import dao.RoomDao;
import model.Room;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur gérant la logique métier des chambres.
 */
public class RoomController {

    private final RoomDao roomDao = new RoomDao();

    public void addRoom(Room room) {
        validate(room);
        if (roomDao.findById(room.getRoomNumber()).isPresent()) {
            throw new IllegalArgumentException("Une chambre avec ce numéro existe déjà.");
        }
        roomDao.save(room);
    }

    public void updateRoom(Room room) {
        validate(room);
        roomDao.update(room);
    }

    public void deleteRoom(int roomNumber) {
        roomDao.delete(roomNumber);
    }

    public Optional<Room> getRoom(int roomNumber) {
        return roomDao.findById(roomNumber);
    }

    public List<Room> getAllRooms() {
        return roomDao.findAll();
    }

    public List<Room> getAvailableRooms() {
        return roomDao.findAvailable();
    }

    private void validate(Room room) {
        if (room.getRoomNumber() <= 0) {
            throw new IllegalArgumentException("Le numéro de chambre doit être positif.");
        }
        if (room.getPricePerNight() <= 0) {
            throw new IllegalArgumentException("Le prix par nuit doit être positif.");
        }
        if (room.getRoomType() == null) {
            throw new IllegalArgumentException("Le type de chambre est obligatoire.");
        }
        if (room.getBedType() == null) {
            throw new IllegalArgumentException("Le type de lit est obligatoire.");
        }
    }
}
