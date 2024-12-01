package org.huydd.bus_ticket_Ecommercial_platform.pojo;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.SeatDTO;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document("tripSeatInfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripSeatInfo implements Serializable {

    @Id
    private ObjectId _id;
    private Long tripId;

    private Boolean isActive;
    private List<SeatDTO> availableSeats;
    private List<SeatDTO> unAvailableSeats;

    public TripSeatInfo(Long tripId, Boolean isActive, List<SeatDTO> availableSeats, List<SeatDTO> unAvailableSeats) {
        this.tripId = tripId;
        this.isActive = isActive;
        this.availableSeats = availableSeats;
        this.unAvailableSeats = unAvailableSeats;
    }
}
