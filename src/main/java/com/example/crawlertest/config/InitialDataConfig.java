package com.example.crawlertest.config;

import com.example.crawlertest.domein.Gebruiker;
import com.example.crawlertest.domein.GebruikersRol;
import com.example.crawlertest.repositories.GebruikerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class InitialDataConfig implements CommandLineRunner {

    private static Logger LOGGER = Logger.getLogger(InitialDataConfig.class.getName());

    private final GebruikerRepository gebruikerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitialDataConfig(GebruikerRepository gebruikerRepository, PasswordEncoder passwordEncoder) {

        this.gebruikerRepository = gebruikerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        final List<Gebruiker> bestaandeAdmin = this.gebruikerRepository.findByRol(GebruikersRol.ADMIN);
        List<Gebruiker> alleGebruikers = this.gebruikerRepository.findAll();

        if (bestaandeAdmin.isEmpty()) {
            LOGGER.info("Standaard admin account wordt aangemaakt....");

            final String wachtwoord = "geheim";

            final Gebruiker gebruiker = new Gebruiker();
            gebruiker.setVoornaam("Ad");
            gebruiker.setEmailadres("admin@workingspirit.nl");
            gebruiker.setWachtwoord(passwordEncoder.encode(wachtwoord));
            gebruiker.setRol(GebruikersRol.ADMIN);
            gebruiker.setAchternaam("Administrator");
            gebruiker.setGebruikersnaam("AdminA");

            LOGGER.log(Level.INFO, "Standaard admin account aangemaakt met wachtwoord {0}", wachtwoord);

            gebruikerRepository.save(gebruiker);
        }

        if (alleGebruikers.isEmpty()) {
            LOGGER.info("Standaard accountmanager account met stabiel wachtwoord wordt aangemaakt....");

            final String wachtwoordGeheim = passwordEncoder.encode("geheim");

            final Gebruiker accountmanager = new Gebruiker();
            accountmanager.setVoornaam("Dirk");
            accountmanager.setEmailadres("accountmanager@workingspirit.nl");
            accountmanager.setWachtwoord(wachtwoordGeheim);
            accountmanager.setRol(GebruikersRol.ACCOUNTMANAGER);
            accountmanager.setAchternaam("Accountmanager");
            accountmanager.setGebruikersnaam("AccMngr");

            LOGGER.log(Level.INFO, "Standaard accountmanager account aangemaakt met stabiel wachtwoord");

            gebruikerRepository.save(accountmanager);
        }
    }
}