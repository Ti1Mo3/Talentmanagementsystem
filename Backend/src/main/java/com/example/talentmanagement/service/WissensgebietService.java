package com.example.talentmanagement.service;

import com.example.talentmanagement.entity.Wissensgebiet;
import com.example.talentmanagement.repository.WissensgebietRepository;
import com.example.talentmanagement.repository.WissensbereichRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

@Service
public class WissensgebietService {
    private final WissensgebietRepository wissensgebietRepository;
    private final WissensbereichRepository wissensbereichRepository;

    @Autowired
    public WissensgebietService(WissensgebietRepository wissensgebietRepository, WissensbereichRepository wissensbereichRepository) {
        this.wissensgebietRepository = wissensgebietRepository;
        this.wissensbereichRepository = wissensbereichRepository;
    }

    private ResponseEntity<String> badRequest(String message) {
        return ResponseEntity.badRequest().body(message);
    }

    public ResponseEntity<?> addWissensgebiet(Wissensgebiet wissensgebiet) {
        if (wissensgebietRepository.findAll().stream().anyMatch(wg -> wg.getName().equalsIgnoreCase(wissensgebiet.getName()))) {
            return badRequest("Ein Wissensgebiet mit diesem Namen existiert bereits.");
        }
        Wissensgebiet saved = wissensgebietRepository.save(wissensgebiet);
        return ResponseEntity.ok(saved);
    }

    public List<Wissensgebiet> getAllWissensgebiete() {
        return wissensgebietRepository.findAllByOrderByNameAsc();
    }

    public ResponseEntity<?> updateWissensgebiet(Long id, Wissensgebiet wissensgebiet) {
        Optional<Wissensgebiet> existingOpt = wissensgebietRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Das Wissensgebiet mit der angegebenen ID wurde nicht gefunden.");
        }
        if (wissensgebietRepository.existsByNameAndIdNot(wissensgebiet.getName(), id)) {
            return badRequest("Ein Wissensgebiet mit diesem Namen existiert bereits.");
        }
        Wissensgebiet existing = existingOpt.get();
        boolean einarbeitungAlt = existing.getEinarbeitung() != null ? existing.getEinarbeitung() : false;
        boolean einarbeitungNeu = wissensgebiet.getEinarbeitung() != null ? wissensgebiet.getEinarbeitung() : false;
        existing.setName(wissensgebiet.getName());
        existing.setEinarbeitung(wissensgebiet.getEinarbeitung());
        Wissensgebiet updated = wissensgebietRepository.save(existing);

        // Wenn Einarbeitung von true auf false geändert wurde, alle zugehörigen Wissensbereiche ebenfalls auf false setzen
        if (einarbeitungAlt && !einarbeitungNeu) {
            List<com.example.talentmanagement.entity.Wissensbereich> bereiche = wissensbereichRepository.findByWissensgebiet_Id(id);
            for (com.example.talentmanagement.entity.Wissensbereich bereich : bereiche) {
                bereich.setEinarbeitung(false);
            }
            wissensbereichRepository.saveAll(bereiche);
        }
        return ResponseEntity.ok(updated);
    }

    public ResponseEntity<?> deleteWissensgebiet(Long id) {
        if (!wissensgebietRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Das Wissensgebiet mit der angegebenen ID wurde nicht gefunden.");
        }
        try {
            wissensgebietRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return badRequest("Um das Wissensgebiet zu löschen, müssen zuerst alle zugehörigen Wissensbereiche gelöscht werden.");
        }
    }
}
