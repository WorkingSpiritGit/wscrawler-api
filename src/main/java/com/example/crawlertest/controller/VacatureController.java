package com.example.crawlertest.controller;

import com.example.crawlertest.domein.SorteerDTO;
import com.example.crawlertest.domein.VacatureDTO;
import com.example.crawlertest.domein.Vacaturestatus;
import com.example.crawlertest.services.VacatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "vacature")
public class VacatureController {

    private VacatureService vacatureService;

    @Autowired
    public VacatureController(VacatureService vacatureService){this.vacatureService = vacatureService;}

    @PostMapping("/")
    public ResponseEntity haalAlleVacaturesOp(@RequestBody SorteerDTO sorteerDTO) {
        List<VacatureDTO> vacatureLijst = vacatureService.alleVacatures(sorteerDTO.getPage(),
                sorteerDTO.getSize(), sorteerDTO.getSortDir(), sorteerDTO.getSort(), sorteerDTO.getZoekopdrachten());

        return ResponseEntity.ok(vacatureLijst);
    }

    @PostMapping("/zoekopdracht")
    public ResponseEntity krijgAantalVacatures(@RequestBody SorteerDTO sorteerDTO){
        return ResponseEntity.ok(vacatureService.aantalVacaturesOphalen(sorteerDTO.getZoekopdrachten()));
    }

    @PostMapping("/nieuweVacatures")
    public ResponseEntity haalAlleNieuweVacatures(@RequestBody SorteerDTO sorteerDTO){
        return ResponseEntity.ok(vacatureService.alleNieuweVacatures(sorteerDTO.getPage(), sorteerDTO.getSize(),
                sorteerDTO.getSortDir(), sorteerDTO.getSort(), sorteerDTO.getZoekopdrachten()));
    }

    @PostMapping("/datum")
    public ResponseEntity aantalNieuweVacatures(@RequestBody SorteerDTO sorteerDTO){
        return ResponseEntity.ok(vacatureService.aantalNieuweVacatures(sorteerDTO.getZoekopdrachten()));
    }

    @PostMapping("/actief")
    public ResponseEntity geefVacaturesInBehandeling(@RequestBody SorteerDTO sorteerDTO) {
        return ResponseEntity.ok(vacatureService.alleVacaturesOpStatus(Vacaturestatus.ACTIEF, sorteerDTO.getPage(),
                sorteerDTO.getSize(), sorteerDTO.getSortDir(), sorteerDTO.getSort(), sorteerDTO.getZoekopdrachten()));
    }

    @PostMapping("/aantalActief")
    public ResponseEntity aantalVacaturesInBehandeling(@RequestBody SorteerDTO sorteerDTO) {
        return ResponseEntity.ok(vacatureService.aantalVacaturesOpStatus(Vacaturestatus.ACTIEF, sorteerDTO.getZoekopdrachten()));
    }

    @PostMapping("/archief")
    public ResponseEntity geefGearchiveerdeVacatures(@RequestBody SorteerDTO sorteerDTO) {
        return ResponseEntity.ok(vacatureService.alleGearchiveerdeVacatures(sorteerDTO.getPage(), sorteerDTO.getSize(),
                sorteerDTO.getSortDir(), sorteerDTO.getSort(), sorteerDTO.getZoekopdrachten()));
    }

    @PostMapping("/aantalArchief")
    public ResponseEntity aantalGearchiveerdeVacature(@RequestBody SorteerDTO sorteerDTO) {
        return ResponseEntity.ok(vacatureService.aantalGearchiveerdeVacatures(sorteerDTO.getZoekopdrachten()));
    }

    @PutMapping("/{id}/verwijder")
    public ResponseEntity verwijderVacature(@PathVariable Long id, @RequestBody VacatureDTO vacatureDTO) {

        if (vacatureService.verwijderVacature(id, vacatureDTO)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacatureDTO> geefVacature(@PathVariable Long id) {

        return vacatureService.geefVacature(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/gezien")
    public ResponseEntity wijzigGezien(@PathVariable Long id, @RequestBody VacatureDTO vacatureDTO) {

        if (vacatureService.wijzigGezien(id, vacatureDTO)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{vacature_id}/{gebruiker_id}/status")
    public ResponseEntity wijzigStatus(@PathVariable Long vacature_id, @PathVariable Long gebruiker_id, @RequestBody VacatureDTO vacatureDTO) {

        if (vacatureService.wijzigStatus(vacature_id, gebruiker_id, vacatureDTO)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/notities")
    public ResponseEntity wijzigNotities(@PathVariable Long id, @RequestBody VacatureDTO vacatureDTO) {

        if (vacatureService.wijzigNotities(id, vacatureDTO)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/archief")
    public ResponseEntity wijzigArchief(@PathVariable Long id, @RequestBody VacatureDTO vacatureDTO) {

        if (vacatureService.wijzigArchief(id, vacatureDTO)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
