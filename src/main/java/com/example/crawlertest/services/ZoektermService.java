package com.example.crawlertest.services;

import com.example.crawlertest.domein.Zoekterm;
import com.example.crawlertest.repositories.ZoektermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ZoektermService {

    private ZoektermRepository zoektermRepository;

    @Autowired
    public ZoektermService(ZoektermRepository zoektermRepo) {
        this.zoektermRepository = zoektermRepo;
    }

    public List<Zoekterm> geefAlleZoektermen() {

        return zoektermRepository.findAll();
    }

    public boolean zoektermOpslaan(Zoekterm zoekterm) {
        zoektermRepository.save(zoekterm);

        if (zoektermRepository.findById(zoekterm.getId()).isPresent()) {
            return true;
        }

        return false;
    }

    public Optional<Zoekterm> geefZoektermOpId(Long id) {

        return zoektermRepository.findById(id);
    }

    public boolean wijzigZoekterm(Long id, Zoekterm zoekterm) {
        Optional<Zoekterm> teWijzigenZoekterm = zoektermRepository.findById(id);

        if (teWijzigenZoekterm.isPresent()) {
            zoektermRepository.save(zoekterm);
            return true;
        }

        return false;
    }

    public boolean verwijderZoekterm(Long id) {
        Optional<Zoekterm> teVerwijderenZoekterm = zoektermRepository.findById(id);

        if (teVerwijderenZoekterm.isPresent()) {
            zoektermRepository.delete(teVerwijderenZoekterm.get());

            return !zoektermRepository.findById(id).isPresent();
        }

        return false;
    }
}
