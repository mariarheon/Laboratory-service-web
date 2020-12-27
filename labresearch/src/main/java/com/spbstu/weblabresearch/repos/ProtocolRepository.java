package com.spbstu.weblabresearch.repos;

import com.spbstu.weblabresearch.dbo.Analysis;
import com.spbstu.weblabresearch.dbo.Protocol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 *
 */
public interface ProtocolRepository extends JpaRepository<Protocol, Integer> {
    Optional<Protocol> findByAnalysis(Analysis analysis);
}

