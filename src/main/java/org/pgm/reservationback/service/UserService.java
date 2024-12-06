package org.pgm.reservationback.service;

import org.pgm.reservationback.model.Role;
import org.pgm.reservationback.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User userDTO);
    User findByUsername(String username);
    void changeRole(Role newRole, String username);
    List<User> findAllUsers();
    }

