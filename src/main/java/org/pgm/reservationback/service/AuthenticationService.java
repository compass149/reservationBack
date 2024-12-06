package org.pgm.reservationback.service;

import org.pgm.reservationback.model.User;

public interface AuthenticationService {
    public User signInAndReturnJWT(User signInRequset);
}
