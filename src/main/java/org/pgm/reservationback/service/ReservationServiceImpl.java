package org.pgm.reservationback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pgm.reservationback.dto.ReservationDTO;
import org.pgm.reservationback.model.Reservation;
import org.pgm.reservationback.model.RoomAvailability;
import org.pgm.reservationback.model.Rooms;
import org.pgm.reservationback.model.User;
import org.pgm.reservationback.repository.ReservationRepository;
import org.pgm.reservationback.repository.RoomAvailabilityRepository;
import org.pgm.reservationback.repository.RoomRepository;
import org.pgm.reservationback.repository.UserRepository;
import org.pgm.reservationback.repository.projection.ReservationItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomAvailabilityRepository roomAvailabilityRepository;

    @Override
    public Reservation saveReservation(ReservationDTO reservationDTO) {
        Long roomId = reservationDTO.getRoomId();
        LocalDate checkIn = reservationDTO.getCheckIn();
        LocalDate checkOut = reservationDTO.getCheckOut();

        // 예약 가능 여부 확인
        if (!isRoomAvailable(roomId, checkIn, checkOut)) {
            throw new IllegalStateException("선택된 날짜에는 방이 예약 불가능합니다.");
        }

        // 예약 저장
        Reservation reservation = Reservation.builder()
                .totalUser(reservationDTO.getTotalUser())
                .checkIn(checkIn)
                .checkOut(checkOut)
                .build();

        User user = userRepository.findByUsername(reservationDTO.getUsername());
        Rooms room = roomRepository.findById(roomId).orElseThrow();

        reservation.setUser(user);
        reservation.setRooms(room);
        reservation.setCreatedAt(LocalDateTime.now());

        // RoomAvailability 업데이트
        List<RoomAvailability> availabilities = roomAvailabilityRepository.findByRoomIdAndDateRange(roomId, checkIn, checkOut);
        availabilities.forEach(availability -> availability.setAvailable(false));
        roomAvailabilityRepository.saveAll(availabilities);

        return reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationItem> findReservationItemsOfUser(String username) {
        return List.of();
    }

    @Override
    public List<Reservation> findAllReservations() {
        return List.of();
    }

    @Override
    public void deleteReservation(Long id) {

    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        return roomAvailabilityRepository.countUnavailableDates(roomId, checkIn, checkOut) == 0;
    }
}
