package com.example.crawlertest.repositories;

import com.example.crawlertest.domein.Gebruiker;
import com.example.crawlertest.domein.GebruikersRol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GebruikerRepository extends CrudRepository<Gebruiker, Long> {

    Optional<Gebruiker> findByEmailadres(String emailadres);

    List<Gebruiker> findByRol(GebruikersRol admin);

    List<Gebruiker> findAll();

    Optional<Gebruiker> findByGebruikersnaam(String username);
}
