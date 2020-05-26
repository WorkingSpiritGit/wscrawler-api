package com.example.crawlertest.domein;

import java.sql.Timestamp;
import java.util.List;

public class SorteerDTO {

    private int page;
    private int size;
    private String sortDir;
    private String sort;
    private List<String> zoekopdrachten;
    private Timestamp datum;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<String> getZoekopdrachten() {
        return zoekopdrachten;
    }

    public void setZoekopdrachten(List<String> zoekopdrachten) {
        this.zoekopdrachten = zoekopdrachten;
    }

    public Timestamp getDatum() { return datum;  }

    public void setDatum(Timestamp datum) { this.datum = datum; }
}
