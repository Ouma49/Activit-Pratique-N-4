package com.example.conference.web;

import com.example.conference.domain.Conference;
import com.example.conference.domain.Review;
import com.example.conference.dto.ConferenceDTO;
import com.example.conference.dto.ReviewDTO;
import com.example.conference.mapper.ConferenceMapper;
import com.example.conference.service.ConferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/conferences")
public class ConferenceController {

    private final ConferenceService service;

    public ConferenceController(ConferenceService service) { this.service = service; }

    @GetMapping
    public List<ConferenceDTO> list() {
        return service.findAll().stream().map(ConferenceMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ConferenceDTO> create(@RequestBody ConferenceDTO dto) {
        Conference c = new Conference();
        c.setTitre(dto.getTitre());
        c.setDureeMinutes(dto.getDureeMinutes());
        c.setNbInscrits(dto.getNbInscrits());
        c.setScore(dto.getScore());
        c.setKeynoteId(dto.getKeynoteId());
        Conference saved = service.save(c);
        return ResponseEntity.created(URI.create("/api/conferences/" + saved.getId())).body(ConferenceMapper.toDto(saved));
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<ReviewDTO> addReview(@PathVariable("id") Long id, @RequestBody ReviewDTO dto) {
        Review r = ConferenceMapper.dtoToReview(dto);
        service.addReview(id, r);
        return ResponseEntity.created(URI.create("/api/conferences/" + id + "/reviews/" + r.getId())).body(dto);
    }
}
