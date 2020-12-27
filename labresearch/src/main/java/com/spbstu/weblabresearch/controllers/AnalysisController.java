package com.spbstu.weblabresearch.controllers;

import com.spbstu.weblabresearch.dbo.Analysis;
import com.spbstu.weblabresearch.dto.common.RequestDto;
import com.spbstu.weblabresearch.repos.AnalysisRepository;
import com.spbstu.weblabresearch.services.RequestService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@RequestMapping("/analysis")
public class AnalysisController {
    @NonNull
    AnalysisRepository analysisRepository;

    @GetMapping("/get_all")
    List<Analysis> getAll() {
        return analysisRepository.findAll();
    }
}
