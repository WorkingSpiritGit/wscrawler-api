package com.example.crawlertest.domein;

import javax.persistence.*;

@Entity()
@Table(name = "ZOEKTERM")
public class Zoekterm {

    @Id
    @GeneratedValue(generator = "zoekterm_gen")
    @TableGenerator(name = "zoekterm_gen", table = "ama_sequence", pkColumnValue = "Zoekterm")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAAM", nullable = false)
    private String naam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
}
