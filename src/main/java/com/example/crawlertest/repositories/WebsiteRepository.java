package com.example.crawlertest.repositories;

import com.example.crawlertest.domein.Website;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WebsiteRepository extends CrudRepository<Website, Long> {

    List<Website> findAll();
}
