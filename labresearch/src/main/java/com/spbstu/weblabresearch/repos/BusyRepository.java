package com.spbstu.weblabresearch.repos;

import com.spbstu.weblabresearch.dbo.Busy;
import com.spbstu.weblabresearch.dbo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusyRepository extends JpaRepository<Busy, Integer> {
    List<Busy> findByAssistantAndTheDateOrderByStartTimeAsc(User assistant, java.sql.Date theDate);
}
