package com.example.crawlertest.controller;

import com.example.crawlertest.domein.Website;
import com.example.crawlertest.services.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "website")
public class WebsiteController {

    private WebsiteService websiteService;

    @Autowired
    public WebsiteController(WebsiteService websiteService) {
        this.websiteService = websiteService;
    }

    @PostMapping("/")
    public ResponseEntity websiteOpslaan(@RequestBody Website website) {

        if (websiteService.websiteOpslaan(website)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/")
    public List<Website> geefAlleWebsites() {
        return websiteService.geefAlleWebsites();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Website> geefWebsite(@PathVariable Long id) {

        return websiteService.geefWebsiteOpId(id).map(website -> ResponseEntity.ok(website))
                                                 .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity wijzigWebsite(@PathVariable Long id, @RequestBody Website website) {

        if (websiteService.wijzigWebsite(id, website)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity verwijderWebsite(@PathVariable Long id) {

        if (websiteService.verwijderWebsite(id)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
