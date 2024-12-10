package org.pgm.reservationback.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
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
