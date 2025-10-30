package com.example.talentmanagement.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class WissensbausteinDto {
    public String name;

    public String level;

    public Boolean einarbeitung;

    public Integer reihenfolge;

    public Long wissensbereichId;

    public Long id;

    public Long wissensgebietId;
}
