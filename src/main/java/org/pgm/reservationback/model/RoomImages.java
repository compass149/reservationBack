package org.pgm.reservationback.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RoomImages implements Comparable<RoomImages> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;
    private String fileName;
    private int ord;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rooms rooms;

    public void setRooms(Rooms rooms) {
        this.rooms = rooms;
    }

    @Override
    public int compareTo(RoomImages other) {
        return Integer.compare(this.ord, other.ord);
    }
}
