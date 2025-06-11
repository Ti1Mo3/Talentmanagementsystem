package com.example.talentmanagement.api;

import com.example.talentmanagement.entity.Wissensbaustein;
import com.example.talentmanagement.repository.WissensbausteinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wissensbaustein")
public class WissensbausteinController {
    private final WissensbausteinRepository wissensbausteinRepository;

    @Autowired
    public WissensbausteinController(WissensbausteinRepository wissensbausteinRepository) {
        this.wissensbausteinRepository = wissensbausteinRepository;
    }

    @PostMapping
    public ResponseEntity<Wissensbaustein> addWissensbaustein(@RequestBody Wissensbaustein wissensbaustein) {
        Wissensbaustein saved = wissensbausteinRepository.save(wissensbaustein);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Wissensbaustein>> getAllWissensbausteine() {
        List<Wissensbaustein> list = wissensbausteinRepository.findAll();
        return ResponseEntity.ok(list);
    }
}
