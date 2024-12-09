package org.pgm.reservationback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pgm.reservationback.dto.ReservationDTO;
import org.pgm.reservationback.model.Reservation;
import org.pgm.reservationback.model.Rooms;
import org.pgm.reservationback.model.User;
import org.pgm.reservationback.repository.ReservationRepository;
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

    @Override
    public ReservationDTO saveReservation(ReservationDTO reservationDTO) {
        Long roomId = reservationDTO.getRoomId();
        LocalDate checkIn = reservationDTO.getCheckIn();
        LocalDate checkOut = reservationDTO.getCheckOut();

        // 상태가 null이면 "PENDING"으로 설정
        if (reservationDTO.getStatus() == null) {
            reservationDTO.setStatus(Reservation.Status.대기);
        }

        // 예약 저장
        Reservation reservation = Reservation.builder()
                .totalUser(reservationDTO.getTotalUser())
                .checkIn(checkIn)
                .checkOut(checkOut)
                .status(reservationDTO.getStatus()) // status 값 추가
                .build();

        User user = userRepository.findByUsername(reservationDTO.getUsername());
        Rooms room = roomRepository.findById(roomId).orElseThrow();

        reservation.setUser(user);
        reservation.setRooms(room);
        reservation.setCreatedAt(LocalDateTime.now());

        Reservation savedReservation = reservationRepository.save(reservation);

        // 예약 저장 후 Reservation 객체를 ReservationDTO로 변환하여 반환
        return convertToDTO(savedReservation);
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

    // Reservation -> ReservationDTO 변환
    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservation.getRsvId());
        reservationDTO.setRoomId(reservation.getRooms().getId());
        reservationDTO.setCheckIn(reservation.getCheckIn());
        reservationDTO.setCheckOut(reservation.getCheckOut());
        reservationDTO.setTotalUser(reservation.getTotalUser());
        reservationDTO.setUsername(reservation.getUser().getUsername());
        reservationDTO.setStatus(reservation.getStatus()); // status 값도 추가
        return reservationDTO;
    }
}
