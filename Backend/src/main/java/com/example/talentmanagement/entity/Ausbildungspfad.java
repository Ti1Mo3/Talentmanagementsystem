package com.example.talentmanagement.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Ausbildungspfad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String inhalt;

    @ManyToMany(mappedBy = "ausbildungspfade")
    private List<Wissensbaustein> wissensbausteine;

    // Getter, Setter, Constructor
}
