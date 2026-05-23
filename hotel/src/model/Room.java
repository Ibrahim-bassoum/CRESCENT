package model;

/**
 * Représente une chambre de l'hôtel.
 */
public class Room {

    public enum Status { AVAILABLE, OCCUPIED }
    public enum RoomType { SINGLE, DOUBLE, SUITE }
    public enum BedType { SINGLE_BED, DOUBLE_BED, KING }

    private int roomNumber;
    private RoomType roomType;
    private BedType bedType;
    private double pricePerNight;
    private Status status;

    public Room() {}

    public Room(int roomNumber, RoomType roomType, BedType bedType, double pricePerNight) {
        this.roomNumber    = roomNumber;
        this.roomType      = roomType;
        this.bedType       = bedType;
        this.pricePerNight = pricePerNight;
        this.status        = Status.AVAILABLE;
    }

    // Getters & Setters
    public int getRoomNumber()                          { return roomNumber; }
    public void setRoomNumber(int roomNumber)           { this.roomNumber = roomNumber; }

    public RoomType getRoomType()                       { return roomType; }
    public void setRoomType(RoomType roomType)          { this.roomType = roomType; }

    public BedType getBedType()                         { return bedType; }
    public void setBedType(BedType bedType)             { this.bedType = bedType; }

    public double getPricePerNight()                    { return pricePerNight; }
    public void setPricePerNight(double pricePerNight)  { this.pricePerNight = pricePerNight; }

    public Status getStatus()                           { return status; }
    public void setStatus(Status status)                { this.status = status; }

    public boolean isAvailable() {
        return this.status == Status.AVAILABLE;
    }

    @Override
    public String toString() {
        return "Chambre #" + roomNumber + " [" + roomType + " / " + bedType + "] - " + pricePerNight + "€/nuit - " + status;
    }
}
