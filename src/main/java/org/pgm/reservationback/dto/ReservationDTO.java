package org.pgm.reservationback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {  //여기에 있는 것을 react랑 연결할 때 구매한 것을 여기에 있는 것으로 사용할 예정
    private Long id;
    private String username;
    private Long roomId; //상품 선택할 때 자동으로 roomId가 들어가게 할 예정임
    /**인원 수**/
    private Integer totalUser;
    private LocalDateTime reserveTime; //입출력 시 하나는 enttity, 하나는 dto 사용해도 됨
//form에서 입력받는 것과 db에 저장되는 것이 다르면 반드시 dto가 필요함

}
