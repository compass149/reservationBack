package org.pgm.reservationback.repository;

import org.pgm.reservationback.model.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {

    /**
     * 선택된 날짜 범위에서 예약 가능한 상태를 확인합니다.
     *
     * @param roomId  방 ID
     * @param checkIn 체크인 날짜
     * @param checkOut 체크아웃 날짜
     * @return 예약 불가능한 날짜의 개수
     */
    @Query("SELECT COUNT(r) FROM RoomAvailability r WHERE r.rooms.rId = :roomId AND r.date BETWEEN :checkIn AND :checkOut AND r.isAvailable = false")
    int countUnavailableDates(@Param("roomId") Long roomId, @Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
    /**
     * 선택된 날짜 범위의 RoomAvailability 목록 조회.
     *
     * @param roomId  방 ID
     * @param checkIn 체크인 날짜
     * @param checkOut 체크아웃 날짜
     * @return RoomAvailability 목록
     */
    @Query("SELECT r FROM RoomAvailability r WHERE r.rooms.rId = :roomId AND r.date BETWEEN :checkIn AND :checkOut")
    List<RoomAvailability> findByRoomIdAndDateRange(@Param("roomId") Long roomId, @Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
}
