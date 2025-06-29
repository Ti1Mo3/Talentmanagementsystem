package com.example.talentmanagement.repository;

import com.example.talentmanagement.entity.Wissensgebiet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WissensgebietRepository extends JpaRepository<Wissensgebiet, Long> {
    boolean existsByNameAndIdNot(String name, Long id);
}
