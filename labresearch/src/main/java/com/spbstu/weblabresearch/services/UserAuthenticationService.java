package com.spbstu.weblabresearch.services;

import com.spbstu.weblabresearch.dbo.User;
import com.spbstu.weblabresearch.dto.request.RegisterDto;
import com.spbstu.weblabresearch.dto.response.SuccessDto;

import java.util.Optional;

/**
 *
 */
public interface UserAuthenticationService {

    /**
     * Logs in with the given {@code login} and {@code pass}.
     *
     * @param login
     * @param password
     * @return an {@link Optional} token of a user when login succeeds
     */
    Optional<String> login(final String login, final String password);

    SuccessDto register(final RegisterDto registerDto);

    /**
     * Finds a user by its dao-key.
     *
     * @param token user dao key
     * @return
     */
    Optional<User> findByToken(String token);

    /**
     * Logs out the given input {@code user}.
     *
     * @param user the user to logout
     */
    void logout(User user);
}
