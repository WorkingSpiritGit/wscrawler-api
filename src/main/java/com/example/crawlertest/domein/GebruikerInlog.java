package com.example.crawlertest.domein;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class GebruikerInlog extends User {

    private final String voornaam;
    private final String tussenvoegsel;
    private final String achternaam;
    private final String displaynaam;
    private final Long gebruiker_id;

    public GebruikerInlog(Gebruiker gebruiker) {

        super(gebruiker.getGebruikersnaam(), gebruiker.getWachtwoord(), Collections.singleton(
                        (GrantedAuthority) () -> gebruiker.getRol().name()));

        voornaam = gebruiker.getVoornaam();
        tussenvoegsel = gebruiker.getTussenvoegsel();
        achternaam = gebruiker.getAchternaam();
        displaynaam = StringUtils.isBlank(tussenvoegsel)
                ? StringUtils.join(new String[]{voornaam, achternaam}, ' ')
                : StringUtils.join(new String[]{voornaam, tussenvoegsel, achternaam}, ' ');
        gebruiker_id = gebruiker.getId();
    }

    public String getVoornaam() {

        return voornaam;
    }

    public String getTussenvoegsel() {

        return tussenvoegsel;
    }

    public String getAchternaam() {

        return achternaam;
    }

    public String getDisplaynaam() {

        return displaynaam;
    }

    public Long getGebruiker_id() {

        return gebruiker_id;
    }
}
