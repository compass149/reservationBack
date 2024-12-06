package org.pgm.reservationback.repository;

import org.pgm.reservationback.model.Role;
import org.pgm.reservationback.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> { //이 형은 user의 id 형
    // Optional<User> findByUsername(String username);
    User findByUsername(String username);

    @Modifying
    @Query ("update User set role=:role where username=:username")
    void updateUserRole (@Param("username") String username, @Param("role") Role role);
}
