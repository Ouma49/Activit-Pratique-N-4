package com.example.keynote;

import com.example.keynote.dto.KeynoteDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KeynoteIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createAndGetKeynote() {
        KeynoteDTO dto = new KeynoteDTO();
        dto.setNom("Dupont");
        dto.setPrenom("Jean");
        dto.setEmail("jean.dupont@example.com");
        dto.setFonction("Professor");

        ResponseEntity<KeynoteDTO> post = restTemplate.postForEntity("/api/keynotes", dto, KeynoteDTO.class);
        assertThat(post.getStatusCode().is2xxSuccessful()).isTrue();
        KeynoteDTO created = post.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();

        ResponseEntity<KeynoteDTO> get = restTemplate.getForEntity("/api/keynotes/" + created.getId(), KeynoteDTO.class);
        assertThat(get.getStatusCode().is2xxSuccessful()).isTrue();
        KeynoteDTO got = get.getBody();
        assertThat(got).isNotNull();
        assertThat(got.getNom()).isEqualTo("Dupont");
    }
}
