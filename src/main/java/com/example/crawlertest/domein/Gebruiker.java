package com.example.crawlertest.domein;

import javax.persistence.*;

@Entity()
@Table(name = "GEBRUIKER")
public class Gebruiker {

    @Id
    @GeneratedValue(generator = "gebruiker_gen")
    @TableGenerator(name = "gebruiker_gen", table = "ama_sequence", pkColumnValue = "Gebruiker")
    @Column(name = "ID")
    private Long id;

    @Column(name = "EMAILADRES", length = 256, nullable = false, unique = true)
    private String emailadres;

    @Column(name = "WACHTWOORD", length = 256, nullable = false)
    private String wachtwoord;

    @Column(name = "ROL", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private GebruikersRol rol;

    @Column(name = "VOORNAAM", length = 256, nullable = false)
    private String voornaam;

    @Column(name = "TUSSENVOEGSEL", length = 256)
    private String tussenvoegsel;

    @Column(name = "ACHTERNAAM", length = 256, nullable = false)
    private String achternaam;

    @Column(name = "GEBRUIKERSNAAM", length = 256, nullable = false, unique = true)
    private String gebruikersnaam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailadres() {
        return emailadres;
    }

    public void setEmailadres(String emailadres) {
        this.emailadres = emailadres;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public GebruikersRol getRol() {
        return rol;
    }

    public void setRol(GebruikersRol rol) {
        this.rol = rol;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public String getGebruikersnaam() { return gebruikersnaam;}

    public void setGebruikersnaam(String gebruikersnaam) { this.gebruikersnaam = gebruikersnaam; }
}
