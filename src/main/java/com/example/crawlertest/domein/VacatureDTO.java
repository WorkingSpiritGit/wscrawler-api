package com.example.crawlertest.domein;

import java.sql.Timestamp;

public class VacatureDTO {

    private Long id;
    private String titel;
    private String url;
    private boolean gezien;
    private Timestamp datum;
    private Vacaturestatus status;
    private String manager;
    private String notities;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
