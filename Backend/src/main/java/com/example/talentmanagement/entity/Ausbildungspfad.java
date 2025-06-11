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

    private String organisation;
    private String artDerAusbildung;
    private String geschaetzteDauer;
    private String arbeitsauftrag;
    private String verortung;
    private String pfad;
    private String dateiname;
    private String ansprechpartner;

    @ManyToMany(mappedBy = "ausbildungspfade")
    private List<Wissensbaustein> wissensbausteine;

    // Getter, Setter, Constructor
}
