package com.spbstu.weblabresearch.repos;

import com.spbstu.weblabresearch.dbo.Role;
import com.spbstu.weblabresearch.dbo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 *
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLogin(String login);
    List<User> findByRole(Role role);
}
