package com.spbstu.weblabresearch.repos;

import com.spbstu.weblabresearch.dbo.Schedule;
import com.spbstu.weblabresearch.dbo.User;
import com.spbstu.weblabresearch.dbo.Weekday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByAssistantAndWeekdayOrderByStartTimeAsc(User assistant, Weekday weekday);
}
