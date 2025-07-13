package com.example.talentmanagement.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Wissensbereich DTO für API Responses ohne Wissensbausteine")
public class WissensbereichDto {
    @Schema(description = "ID des Wissensbereichs", example = "1")
    public Long id;

    @Schema(description = "Name des Wissensbereichs", example = "SAP Entwicklung")
    public String name;

    @Schema(description = "Einarbeitung erforderlich", example = "true")
    public Boolean einarbeitung;

    @Schema(description = "ID des zugehörigen Wissensgebiets", example = "2")
    public Long wissensgebietId;
}
