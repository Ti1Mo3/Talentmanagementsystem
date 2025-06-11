package com.example.talentmanagement.repository;

import com.example.talentmanagement.entity.Wissensbereich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WissensbereichRepository extends JpaRepository<Wissensbereich, Long> {
}
