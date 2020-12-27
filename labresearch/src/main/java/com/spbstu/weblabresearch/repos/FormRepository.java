package com.spbstu.weblabresearch.repos;

import com.spbstu.weblabresearch.dbo.Form;
import com.spbstu.weblabresearch.dbo.FormStatus;
import com.spbstu.weblabresearch.dbo.Request;
import com.spbstu.weblabresearch.dbo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface FormRepository extends JpaRepository<Form, Integer> {
    List<Form> findByAssistantAndStatus(User assistant, FormStatus status);
    List<Form> findByRequest(Request request);
}
