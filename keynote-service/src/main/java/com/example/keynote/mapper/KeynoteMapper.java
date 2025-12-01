package com.example.keynote.mapper;

import com.example.keynote.domain.Keynote;
import com.example.keynote.dto.KeynoteDTO;

public class KeynoteMapper {
    public static KeynoteDTO toDto(Keynote entity) {
        if (entity == null) return null;
        KeynoteDTO dto = new KeynoteDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setEmail(entity.getEmail());
        dto.setFonction(entity.getFonction());
        return dto;
    }

    public static Keynote toEntity(KeynoteDTO dto) {
        if (dto == null) return null;
        Keynote entity = new Keynote();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setEmail(dto.getEmail());
        entity.setFonction(dto.getFonction());
        return entity;
    }
}
