package com.example.crawlertest.domein;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.util.List;

@Entity()
@Table(name = "WEBSITE")
public class Website {

    @Id
    @GeneratedValue(generator = "website_gen")
    @TableGenerator(name = "website_gen", table = "ama_sequence", pkColumnValue = "Website")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAAM", nullable = false)
    private String naam;

    @Column(name = "URL", length = 767, nullable = false, unique = true)
    private String url;

    @Column(name = "FILTER", nullable = false)
    private String filter;

    @Column(name="VACATURETEKSTTAG", nullable = false)
    private String vacatureTekstTag;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getVacatureTekstTag() { return vacatureTekstTag; }

    public void setVacatureTekstTag(String vacatureTekstTag) { this.vacatureTekstTag = vacatureTekstTag; }
}
