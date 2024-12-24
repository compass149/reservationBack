package org.pgm.reservationback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pgm.reservationback.dto.ReservationDTO;
import org.pgm.reservationback.dto.ReservationRequestDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final KakaoPayService kakaoPayService; // KakaoPayService 주입

    @Override
    public ReservationDTO saveReservation(ReservationDTO reservationDTO) {
        Long roomId = Long.valueOf(reservationDTO.getRoomId());
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
        return reservationRepository.findAllReservationsOfUser(username);
    }

    @Override
    public List<Reservation> findAllReservations() {
        return List.of();
    }

    // Reservation -> ReservationDTO 변환
    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setRsvId(reservation.getRsvId());
        // reservation.getRoomId()가 아니라, reservation.getRooms().getId()로 수정
        reservationDTO.setRoomId(reservation.getRooms().getId());
        reservationDTO.setCheckIn(reservation.getCheckIn());
        reservationDTO.setCheckOut(reservation.getCheckOut());
        reservationDTO.setTotalUser(reservation.getTotalUser());
        reservationDTO.setUsername(reservation.getUser().getUsername());
        reservationDTO.setStatus(reservation.getStatus());
        return reservationDTO;
    }

    public void cancelReservation(Long rsvId, String username) {
        Reservation reservation = reservationRepository.findById(rsvId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다."));

        // 예약의 사용자명과 현재 요청한 사용자의 username이 일치하거나,
        // 관리자 권한이면 취소를 허용하는 식의 로직을 추가할 수 있습니다.
        if (!reservation.getUser().getUsername().equals(username)) {
            throw new SecurityException("해당 예약을 취소할 권한이 없습니다.");
        }

        // 취소 로직 예: 상태를 '취소'로 변경하거나, 실제로 DB에서 삭제할 수도 있음
        // 여기서는 상태 변경으로 가정
        reservation.setStatus(Reservation.Status.취소);
        reservationRepository.save(reservation);
    }

    @Override
    public Reservation createReservation(ReservationRequestDTO requestDTO) {
        // ReservationRequestDTO를 Reservation 엔티티로 변환
        Reservation reservation = Reservation.builder()
                .rooms(requestDTO.getRoom()) // Rooms 엔티티
                .user(requestDTO.getUser()) // User 엔티티
                .totalUser(requestDTO.getTotalUser())
                .checkIn(requestDTO.getCheckIn())
                .checkOut(requestDTO.getCheckOut())
                .status(Reservation.Status.대기)
                .build();

        // DB에 저장
        return reservationRepository.save(reservation);
    }

    @Override
    public Map<String, String> preparePayment(Reservation reservation) {
        // 결제 준비 로직
        String rsvId = "rsv_" + reservation.getRsvId();
        String roomName = reservation.getRooms().getRoomName();
        int totalAmount = reservation.getTotalUser() * reservation.getRooms().getPricePerNight().intValue();

        // ▲ 이제 kakaoPayService 의 kakaoPayReady()를 호출
        Map<String, String> paymentInfo = kakaoPayService.kakaoPayReady(
                rsvId,
                reservation.getUser().getUsername(),
                roomName,
                reservation.getTotalUser(),
                totalAmount
        );

        // 필요한 정보 리턴
        Map<String, String> response = new HashMap<>();
        response.put("next_redirect_pc_url", paymentInfo.get("next_redirect_pc_url"));
        response.put("tid", paymentInfo.get("tid"));
        return response;
    }
}
