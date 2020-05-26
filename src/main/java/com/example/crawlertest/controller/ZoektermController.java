package com.example.crawlertest.controller;

import com.example.crawlertest.domein.Zoekterm;
import com.example.crawlertest.services.ZoektermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "zoekterm")
public class ZoektermController {

    private ZoektermService zoektermService;

    @Autowired
    public ZoektermController(ZoektermService zoektermService) {
        this.zoektermService = zoektermService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Zoekterm>> geefAlleZoektermen() {

        return ResponseEntity.ok(zoektermService.geefAlleZoektermen());
    }

    @PostMapping("/")
    public ResponseEntity zoektermOpslaan(@RequestBody Zoekterm zoekterm) {

        if (zoektermService.zoektermOpslaan(zoekterm)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Zoekterm> geefZoekterm(@PathVariable Long id) {

        return zoektermService.geefZoektermOpId(id).map(zoekterm -> ResponseEntity.ok(zoekterm))
                                                   .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity zoektermWijzigen(@PathVariable Long id, @RequestBody Zoekterm zoekterm) {

        if (zoektermService.wijzigZoekterm(id, zoekterm)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity verwijderZoekterm(@PathVariable Long id) {

        if (zoektermService.verwijderZoekterm(id)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
