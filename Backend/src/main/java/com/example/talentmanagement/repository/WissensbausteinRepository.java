package com.example.talentmanagement.repository;

import com.example.talentmanagement.entity.Wissensbaustein;
import com.example.talentmanagement.entity.Wissensbereich;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WissensbausteinRepository extends JpaRepository<Wissensbaustein, Long> {
    List<Wissensbaustein> findByWissensbereich_IdOrderByReihenfolgeAsc(Long wissensbereichId);
    boolean existsByNameAndWissensbereich(String name, Wissensbereich wissensbereich);
}
