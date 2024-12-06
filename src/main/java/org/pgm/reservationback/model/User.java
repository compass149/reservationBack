package org.pgm.reservationback.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Gender gender;

    @Column(length = 20)
    private String mobile;

    @Column(length = 200)
    private String email;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createTime;


    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;
    @PrePersist
    public void prePersist() {
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now(); // 현재 날짜와 시간으로 설정
        }
    }

    public enum Gender {
        남, 여    }

    @Transient
    private String token;
}
