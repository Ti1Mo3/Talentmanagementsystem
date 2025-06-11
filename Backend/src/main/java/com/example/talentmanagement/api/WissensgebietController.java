package com.example.talentmanagement.api;

import com.example.talentmanagement.entity.Wissensgebiet;
import com.example.talentmanagement.repository.WissensgebietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wissensgebiet")
public class WissensgebietController {
    private final WissensgebietRepository wissensgebietRepository;

    @Autowired
    public WissensgebietController(WissensgebietRepository wissensgebietRepository) {
        this.wissensgebietRepository = wissensgebietRepository;
    }

    @PostMapping
    public ResponseEntity<Wissensgebiet> addWissensgebiet(@RequestBody Wissensgebiet wissensgebiet) {
        Wissensgebiet saved = wissensgebietRepository.save(wissensgebiet);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Wissensgebiet>> getAllWissensgebiete() {
        List<Wissensgebiet> list = wissensgebietRepository.findAll();
        return ResponseEntity.ok(list);
    }
}
