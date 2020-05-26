package com.example.crawlertest.domein;

import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity()
@Table(name = "Vacature")
public class Vacature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITEL", length = 2000)
    private String titel;

    @Column(name = "TEKST", length = 100000)
    private String tekst;

    @Column(name = "URL", unique = true)
    private String url;

    @Column(name = "ZICHTBAAR", nullable = false)
    private boolean zichtbaar;

    @Column(name = "GEZIEN", nullable = false)
    private boolean gezien;

    @CreationTimestamp
    @Column(name = "DATUM")
    private Timestamp datum;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Vacaturestatus status;

    @Column(name = "MANAGER")
    private String manager;

    @Column(name = "NOTITIES", length = 5000)
    private String notities;

    @Column(name = "GEARCHIVEERD", nullable = false)
    private boolean gearchiveerd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isZichtbaar() {
        return zichtbaar;
    }

    public void setZichtbaar(boolean zichtbaar) {
        this.zichtbaar = zichtbaar;
    }

    public boolean isGezien() {
        return gezien;
    }

    public void setGezien(boolean gezien) {
        this.gezien = gezien;
    }

    public Timestamp getDatum() {
        return datum;
    }

    public void setDatum(Timestamp datum) {
        this.datum = datum;
    }

    public Vacaturestatus getStatus() {
        return status;
    }

    public void setStatus(Vacaturestatus status) {
        this.status = status;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getNotities() {
        return notities;
    }

    public void setNotities(String notities) {
        this.notities = notities;
    }

    public boolean isGearchiveerd() {
        return gearchiveerd;
    }

    public void setGearchiveerd(boolean gearchiveerd) {
        this.gearchiveerd = gearchiveerd;
    }
}
