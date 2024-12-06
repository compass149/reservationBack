package org.pgm.reservationback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pgm.reservationback.dto.ReservationDTO;
import org.pgm.reservationback.dto.RoomDTO;
import org.pgm.reservationback.model.Reservation;
import org.pgm.reservationback.model.Rooms;
import org.pgm.reservationback.model.User;
import org.pgm.reservationback.repository.ReservationRepository;
import org.pgm.reservationback.repository.RoomRepository;
import org.pgm.reservationback.repository.UserRepository;
import org.pgm.reservationback.repository.projection.ReservationItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Reservation saveReservation(ReservationDTO reservationDTO) {
        Reservation reservation = Reservation.builder()
                .totalUser(reservationDTO.getTotalUser())
                .build();
        User user = userRepository.findByUsername(reservationDTO.getUsername());
        Rooms room = roomRepository.findById(reservationDTO.getRoomId()).orElseThrow();

        reservation.setUser(user);
        reservation.setRooms(room);
        reservation.setCreatedAt(LocalDateTime.now()); //구매일자 저장

        Reservation savedReservation = reservationRepository.save(reservation);
        return savedReservation;
    }

    @Override
    public List<ReservationItem> findReservationItemsOfUser(String username) {
        User user = userRepository.findByUsername(username);
        List<ReservationItem> reservationItems=reservationRepository.findAllReservationsOfUser(username);
        reservationItems.forEach(reservationItem->{
            log.info(reservationItem.getQuantity());
            log.info(reservationItem.getReserveTime());
        });
        return reservationRepository.findAllReservationsOfUser(username);
    }

    @Override
    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
