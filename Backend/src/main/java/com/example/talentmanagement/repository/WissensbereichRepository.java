package com.example.talentmanagement.repository;

import com.example.talentmanagement.entity.Wissensbereich;
import com.example.talentmanagement.entity.Wissensgebiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WissensbereichRepository extends JpaRepository<Wissensbereich, Long> {
    boolean existsByNameAndWissensgebiet(String name, Wissensgebiet wissensgebiet);
    boolean existsByNameAndWissensgebietAndIdNot(String name, Wissensgebiet wissensgebiet, Long id);
    List<Wissensbereich> findByWissensgebiet_Id(Long wissensgebietId);
    List<Wissensbereich> findByWissensgebiet_IdOrderByIdDesc(Long wissensgebietId);
    List<Wissensbereich> findAllByOrderByIdDesc();
}
