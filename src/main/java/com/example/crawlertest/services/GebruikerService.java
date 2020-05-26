package com.example.crawlertest.services;

import com.example.crawlertest.domein.Gebruiker;
import com.example.crawlertest.domein.WachtwoordDTO;
import com.example.crawlertest.repositories.GebruikerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
public class GebruikerService {

    private GebruikerRepository gebruikerRepository;
    private PasswordEncoder encoder;

    @Autowired
    public GebruikerService(GebruikerRepository gebruikerRepo, PasswordEncoder passwordEncoder) {
        this.gebruikerRepository = gebruikerRepo;
        this.encoder = passwordEncoder;
    }

    public List<Gebruiker> geefAlleGebruikers() {

        return gebruikerRepository.findAll();
    }

    public boolean gebruikerOpslaan(Gebruiker gebruiker) {
        String randomWachtwoord = RandomStringUtils.randomAlphabetic(10);

        if (!gebruikerRepository.findByGebruikersnaam(gebruiker.getVoornaam().substring(0,1) +
                                                               gebruiker.getAchternaam()).isPresent()) {
            gebruiker.setWachtwoord(encoder.encode(randomWachtwoord));
            gebruiker.setGebruikersnaam(gebruiker.getVoornaam().substring(0,1) + gebruiker.getAchternaam());
            gebruikerRepository.save(gebruiker);
        } else {
            gebruiker.setWachtwoord(encoder.encode(randomWachtwoord));
            gebruiker.setGebruikersnaam(gebruiker.getVoornaam().substring(0,1) +
                                        gebruiker.getAchternaam() +
                                        RandomStringUtils.randomNumeric(5));
            gebruikerRepository.save(gebruiker);
        }

        return gebruikerRepository.findById(gebruiker.getId()).isPresent();
    }

    public Optional<Gebruiker> geefGebruikerOpId(Long id) {

        return gebruikerRepository.findById(id);
    }

    public boolean gebruikerWijzigen(Long id, Gebruiker gebruiker) {
        Optional<Gebruiker> teWijzigenGebruiker = gebruikerRepository.findById(id);

        if (teWijzigenGebruiker.isPresent()) {
            gebruikerRepository.save(gebruiker);
            return true;
        }

        return false;
    }

    public boolean gebruikerVerwijderen(Long id) {
        Optional<Gebruiker> teVerwijderenGebruiker = gebruikerRepository.findById(id);

        if (teVerwijderenGebruiker.isPresent()) {
            gebruikerRepository.delete(teVerwijderenGebruiker.get());

            return !gebruikerRepository.findById(id).isPresent();
        }

        return false;
    }

    public boolean wachtwoordWijzigen(Long id, WachtwoordDTO wachtwoordDTO) {
        Optional<Gebruiker> tempGebruiker = gebruikerRepository.findById(id);

        if (tempGebruiker.isPresent() && encoder.matches(wachtwoordDTO.getHuidigWachtwoord(), tempGebruiker.get().getWachtwoord())) {
            Gebruiker teWijzigenGebruiker = tempGebruiker.get();

            if (isGeldig(wachtwoordDTO.getNieuwWachtwoord(), wachtwoordDTO.getHuidigWachtwoord())) {

                teWijzigenGebruiker.setWachtwoord(encoder.encode(wachtwoordDTO.getNieuwWachtwoord()));
                gebruikerRepository.save(teWijzigenGebruiker);
                return true;
            }
        }

        return false;
    }

    private boolean isGeldig(String nieuwWachtwoord, String huidigWachtwoord) {
        final Predicate<String> eisen;

        Predicate<String> regel1 = wachtwoord -> wachtwoord.length() >= 8; //Wachtwoord moet langer dan 8 zijn.
        Predicate<String> regel2a = wachtwoord -> !wachtwoord.equals(wachtwoord.toLowerCase());  //Minimaal 1 kleine letter
        Predicate<String> regel2b = wachtwoord -> !wachtwoord.equals(wachtwoord.toUpperCase()); //Minimaal 1 kapitaal
        Predicate<String> regel2c = wachtwoord -> wachtwoord.codePoints().anyMatch(Character::isDigit); //Minimaal 1 cijfer
        Predicate<String> regel2d = wachtwoord -> wachtwoord.codePoints().anyMatch(i -> !Character.isAlphabetic(i)); //Minimaal 1 speciaal karakter
        Predicate<String> regel2 = wachtwoord -> Stream.of(regel2a, regel2b, regel2c, regel2d)
                .filter(predicate -> predicate.test(wachtwoord))
                .count() >= 3;
        Predicate<String> regel3 = wachtwoord -> !wachtwoord.contains(huidigWachtwoord);

        eisen = regel1.and(regel2).and(regel3);

        return eisen.test(nieuwWachtwoord);
    }
}
