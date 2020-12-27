package com.spbstu.weblabresearch.repos;

import com.spbstu.weblabresearch.dbo.Analysis;
import com.spbstu.weblabresearch.dbo.FormField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormFieldRepository extends JpaRepository<FormField, Integer> {
    List<FormField> findByAnalysisOrderByOrderNumAsc(Analysis analysis);
}
