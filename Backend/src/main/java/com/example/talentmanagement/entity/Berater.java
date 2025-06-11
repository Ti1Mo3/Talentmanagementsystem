package com.example.talentmanagement.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Berater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String benutzername;

    @Column(nullable = false)
    private String passwort;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
        name = "berater_wissensbaustein",
        joinColumns = @JoinColumn(name = "berater_id"),
        inverseJoinColumns = @JoinColumn(name = "wissensbaustein_id")
    )
    private List<Wissensbaustein> wissensbausteine;

    // Getter, Setter, Constructor
}
