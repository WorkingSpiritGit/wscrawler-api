package com.example.crawlertest.controller;

import com.example.crawlertest.domein.Gebruiker;
import com.example.crawlertest.domein.WachtwoordDTO;
import com.example.crawlertest.services.GebruikerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "gebruiker")
public class GebruikerController {

    private GebruikerService gebruikerService;

    @Autowired
    public GebruikerController(GebruikerService gebruikerService) {
        this.gebruikerService = gebruikerService;
    }

    @GetMapping("/")
    public List<Gebruiker> geefAlleGebruikers() {
        return gebruikerService.geefAlleGebruikers();
    }

    @PostMapping("/")
    public ResponseEntity gebruikerOpslaan(@RequestBody Gebruiker gebruiker) {

        if (gebruikerService.gebruikerOpslaan(gebruiker)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gebruiker> geefGebruiker(@PathVariable Long id) {

        return gebruikerService.geefGebruikerOpId(id).map(gebruiker -> ResponseEntity.ok(gebruiker))
                                                     .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity gebruikerWijzigen(@PathVariable Long id, @RequestBody Gebruiker gebruiker) {

        if (gebruikerService.gebruikerWijzigen(id, gebruiker)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity verwijderGebruiker(@PathVariable Long id) {

        if (gebruikerService.gebruikerVerwijderen(id)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/wachtwoord")
    public ResponseEntity wachtwoordWijzigen(@PathVariable Long id, @RequestBody WachtwoordDTO wachtwoordDTO) {

        if (gebruikerService.wachtwoordWijzigen(id, wachtwoordDTO)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
