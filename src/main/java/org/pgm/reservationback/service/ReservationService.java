package org.pgm.reservationback.service;

import org.pgm.reservationback.dto.ReservationDTO;
import org.pgm.reservationback.model.Reservation;
import org.pgm.reservationback.repository.projection.ReservationItem;

import java.util.List;

public interface ReservationService {
    Reservation saveReservation(ReservationDTO reservationDTO);
    List<ReservationItem> findReservationItemsOfUser(String username);
    //실제로 데이터를 입력할 때는 username이나 productid만 가져옴.
    //화면에 뿌릴 때 dto에는 상품정보 모두가 필요하므로 reservation로 리턴하도록 함
    List<Reservation> findAllReservations();
    void deleteReservation(Long id);
    }
