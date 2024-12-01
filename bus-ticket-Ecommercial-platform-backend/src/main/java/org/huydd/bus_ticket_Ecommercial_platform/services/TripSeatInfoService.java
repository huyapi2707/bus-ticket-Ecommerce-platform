package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.SeatDTO;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.IdNotFoundException;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.SeatNotAvailableException;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.TripSeatInfo;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.TripSeatInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripSeatInfoService {
    private final TripSeatInfoRepository tripSeatInfoRepository;

    public TripSeatInfo getByTripId(Long tripId, Boolean isActive) {
        var optionalTripSeatInfo = tripSeatInfoRepository.findTripSeatInfoByTripIdAndIsActive(tripId, isActive);
        if (optionalTripSeatInfo.isPresent()) {
            return optionalTripSeatInfo.get();
        } else throw new IdNotFoundException("No trip id found");
    }

    public TripSeatInfo createTripSeatInfo(Long tripId, Boolean isActive, List<SeatDTO> availableSeats, List<SeatDTO> unavailableSeats) {
        TripSeatInfo tripSeatInfo = new TripSeatInfo(tripId, isActive, availableSeats, unavailableSeats);
        return tripSeatInfoRepository.save(tripSeatInfo);
    }

    public TripSeatInfo updateUnAvailableSeats(Long tripId, List<SeatDTO> seats) {
        var optionalTripSeatInfo = tripSeatInfoRepository.findTripSeatInfoByTripIdAndIsActive(tripId, true);
        if (optionalTripSeatInfo.isEmpty()) throw new IdNotFoundException("Trip id is not exist");
        TripSeatInfo tripSeatInfo = optionalTripSeatInfo.get();
        List<SeatDTO> availableSeats = tripSeatInfo.getAvailableSeats();
        List<SeatDTO> unAvailableSeats = tripSeatInfo.getUnAvailableSeats();

        seats.stream().forEach(x -> availableSeats.remove(x));
        unAvailableSeats.addAll(seats);

        tripSeatInfo.setAvailableSeats(availableSeats);
        tripSeatInfo.setUnAvailableSeats(unAvailableSeats);

        return tripSeatInfoRepository.save(tripSeatInfo);
    }

    public TripSeatInfo UpdateAvailableSeats(Long tripId, List<SeatDTO> seats) {
        var optionalTripSeatInfo = tripSeatInfoRepository.findTripSeatInfoByTripIdAndIsActive(tripId, true);
        if (optionalTripSeatInfo.isEmpty()) throw new IdNotFoundException("Trip id is not exist");
        TripSeatInfo tripSeatInfo = optionalTripSeatInfo.get();
        List<SeatDTO> availableSeats = tripSeatInfo.getAvailableSeats();
        List<SeatDTO> unAvailableSeats = tripSeatInfo.getUnAvailableSeats();

        seats.stream().forEach(x -> unAvailableSeats.remove(x));
        availableSeats.addAll(seats);

        tripSeatInfo.setAvailableSeats(availableSeats);
        tripSeatInfo.setUnAvailableSeats(unAvailableSeats);

        return tripSeatInfoRepository.save(tripSeatInfo);
    }

    public SeatDTO handleNewTicket(Long tripId, Long id) {
        TripSeatInfo tripSeatInfo = tripSeatInfoRepository.findTripSeatInfoByTripIdAndIsActive(tripId, true).get();
        List<SeatDTO> availableSeats = tripSeatInfo.getAvailableSeats();
        List<SeatDTO> unAvailableSeats = tripSeatInfo.getUnAvailableSeats();

        int index = -1;
        for (int i = 0; i < availableSeats.size(); i++) {
            SeatDTO seat = availableSeats.get(i);
            if (seat.getId().equals(id)) {
                index = i;
                break;
            }
        }
        if (index < 0) throw new SeatNotAvailableException("Seat is not available");
        SeatDTO removedSeat = availableSeats.remove(index);
        unAvailableSeats.add(SeatDTO.builder()
                .code(removedSeat.getCode())
                .id(removedSeat.getId())
                .build());
        tripSeatInfo.setUnAvailableSeats(unAvailableSeats);
        tripSeatInfo.setAvailableSeats(availableSeats);
        tripSeatInfoRepository.save(tripSeatInfo);

        return removedSeat;
    }

    public void handleRemoveTicket(Long tripId, String seatCode) {
        TripSeatInfo tripSeatInfo = tripSeatInfoRepository.findTripSeatInfoByTripIdAndIsActive(tripId, true).get();
        List<SeatDTO> availableSeats = tripSeatInfo.getAvailableSeats();
        List<SeatDTO> unAvailableSeats = tripSeatInfo.getUnAvailableSeats();
        int index = -1;
        for (int i = 0; i < unAvailableSeats.size(); i++) {
            SeatDTO seat = unAvailableSeats.get(i);
            if (seat.getCode().equals(seatCode)) {
                index = i;
                break;
            }
        }
        if (index > 0) {
            SeatDTO removedSeat =  unAvailableSeats.remove(index);
            availableSeats.add(removedSeat);
            tripSeatInfo.setAvailableSeats(availableSeats);
            tripSeatInfo.setUnAvailableSeats(unAvailableSeats);
            tripSeatInfoRepository.save(tripSeatInfo);
        }

    }
}
