package com.spbstu.weblabresearch.repos;

import com.spbstu.weblabresearch.dbo.Request;
import com.spbstu.weblabresearch.dbo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 */
public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByClient(User client);
}
