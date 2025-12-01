package com.example.conference.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "keynote-service", url = "http://localhost:8082")
public interface KeynoteClient {
    @GetMapping("/api/keynotes/{id}")
    Object getKeynote(@PathVariable("id") Long id);
}
