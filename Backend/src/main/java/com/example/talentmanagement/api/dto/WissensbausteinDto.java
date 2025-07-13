package com.example.talentmanagement.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Wissensbaustein DTO für API Requests und Responses")
public class WissensbausteinDto {
    @Schema(description = "Name des Wissensbausteins", example = "Java Grundlagen")
    public String name;

    @Schema(description = "Level des Wissensbausteins", example = "GRUNDWISSEN", allowableValues = {"GRUNDWISSEN", "BERATERWISSEN", "EXPERTENWISSEN"})
    public String level;

    @Schema(description = "Einarbeitung erforderlich", example = "true")
    public Boolean einarbeitung;

    @Schema(description = "Reihenfolge als Zahl (1-10)", example = "1", minimum = "1", maximum = "10")
    public Integer reihenfolge;

    @Schema(description = "ID des zugehörigen Wissensbereichs", example = "5")
    public Long wissensbereichId;

    @Schema(description = "ID des Wissensbausteins", example = "1")
    public Long id;
}
