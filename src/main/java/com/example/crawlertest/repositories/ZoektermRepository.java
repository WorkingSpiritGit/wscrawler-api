package com.example.crawlertest.repositories;

import com.example.crawlertest.domein.Zoekterm;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ZoektermRepository extends CrudRepository<Zoekterm, Long> {

    List<Zoekterm> findAll();
}
