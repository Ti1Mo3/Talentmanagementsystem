package com.example.talentmanagement.api.controller;

import com.example.talentmanagement.api.dto.WissensbereichDto;
import com.example.talentmanagement.entity.Wissensbereich;
import com.example.talentmanagement.repository.WissensbereichRepository;
import com.example.talentmanagement.repository.WissensgebietRepository;
import com.example.talentmanagement.service.WissensbereichService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/wissensbereich")
public class WissensbereichController {
    private final WissensbereichRepository wissensbereichRepository;
    private final WissensgebietRepository wissensgebietRepository;
    private final WissensbereichService wissensbereichService;
    private final com.example.talentmanagement.repository.WissensbausteinRepository wissensbausteinRepository;

    @Autowired
    public WissensbereichController(WissensbereichRepository wissensbereichRepository, WissensgebietRepository wissensgebietRepository, WissensbereichService wissensbereichService, com.example.talentmanagement.repository.WissensbausteinRepository wissensbausteinRepository) {
        this.wissensbereichRepository = wissensbereichRepository;
        this.wissensgebietRepository = wissensgebietRepository;
        this.wissensbereichService = wissensbereichService;
        this.wissensbausteinRepository = wissensbausteinRepository;
    }

    @Operation(summary = "Liefert alle Wissensbereiche", description = "Gibt eine Liste aller vorhandenen Wissensbereiche zurück. Wissensbausteine werden nicht mitgeliefert.")
    @GetMapping
    public ResponseEntity<List<WissensbereichDto>> getAllWissensbereiche() {
        return ResponseEntity.ok(wissensbereichService.getAllWissensbereiche());
    }
}
