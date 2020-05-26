package com.example.crawlertest.services;

import com.example.crawlertest.domein.Gebruiker;
import com.example.crawlertest.domein.Vacature;
import com.example.crawlertest.domein.VacatureDTO;
import com.example.crawlertest.domein.Vacaturestatus;
import com.example.crawlertest.repositories.GebruikerRepository;
import com.example.crawlertest.repositories.VacatureRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class VacatureService {

    private final Logger LOGGER = Logger.getLogger(VacatureService.class.getName());
    private VacatureRepository vacatureRepository;
    private GebruikerRepository gebruikerRepository;

    @Autowired
    public VacatureService(VacatureRepository vacatureRepo, GebruikerRepository gebruikerRepo) {
        this.vacatureRepository = vacatureRepo;
        this.gebruikerRepository = gebruikerRepo;
    }

    public boolean vacatureOpslaan(Vacature vacature) {
        if (!vacatureBestaatAl(vacature.getUrl())) {
            vacature.setZichtbaar(true);
            vacature.setStatus(Vacaturestatus.AFWACHTING);
            vacatureRepository.save(vacature);
            LOGGER.info("Nieuwe vacature gevonden: " + vacature.getTitel());
        }

        return vacatureRepository.findByUrl(vacature.getUrl()).isPresent();
    }

    private boolean vacatureBestaatAl(String url) {
        return vacatureRepository.findByUrl(url).isPresent();
    }

    public boolean verwijderVacature(Long id, VacatureDTO vacatureDTO) {
        Optional<Vacature> teVerwijderenVacature = vacatureRepository.findByUrl(vacatureDTO.getUrl());

        if (teVerwijderenVacature.isPresent()) {
            Vacature vacature = teVerwijderenVacature.get();
            vacature.setZichtbaar(false);
            vacature.setGearchiveerd(false);
            vacatureRepository.save(vacature);

            return true;
        }
        return false;
    }

    public List<VacatureDTO> alleVacatures(int page, int size, String sortDir, String sort, List<String> zoekopdrachten){

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(sortDir), sort);
        List<Vacature> vacatures = new ArrayList<>();

        if (zoekopdrachten.size() == 0) {
            vacatures = vacatureRepository.findAllByTekst("", pageable).getContent();
        } else {
            vacatures = filterVacatures(zoekopdrachten, pageable).getContent();
        }

        List<VacatureDTO> vacatureLijst = new ArrayList<>();

        for (Vacature vacature : vacatures) {
                VacatureDTO tempVacatureDTO = new VacatureDTO();
                tempVacatureDTO.setUrl(vacature.getUrl());
                tempVacatureDTO.setTitel(vacature.getTitel());
                tempVacatureDTO.setId(vacature.getId());
                tempVacatureDTO.setDatum(vacature.getDatum());
                tempVacatureDTO.setGezien(vacature.isGezien());
                tempVacatureDTO.setStatus(vacature.getStatus());
                tempVacatureDTO.setManager(vacature.getManager());

                vacatureLijst.add(tempVacatureDTO);
        }
        return vacatureLijst;
    }

    public int aantalVacaturesOphalen (List<String> zoekopdrachten){
        Set<Vacature> vacatures = new HashSet<>();

        if (zoekopdrachten.size() == 0) {
            vacatures.addAll(vacatureRepository.findAllByTekst(""));
        } else {
            for (String zoekopdracht : zoekopdrachten) {
                String filteropdracht = "%"+zoekopdracht+"%";
                vacatures.addAll(vacatureRepository.findAllByTekst(filteropdracht));
            }
        }
        return vacatures.size();
    }

    public List<VacatureDTO> alleNieuweVacatures(int page, int size, String sortDir, String sort, List<String> zoekopdrachten){

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(sortDir), sort);
        List<Vacature> vacatures = new ArrayList<>();
        LocalDate datum = LocalDate.now();

        if (zoekopdrachten.size() == 0) {
            vacatures = vacatureRepository.findAllByDatum(datum, "", pageable).getContent();
        } else {
            vacatures = filterNieuweVacatures(zoekopdrachten, pageable).getContent();
        }

        List<VacatureDTO> lijstNieuweVacatures = new ArrayList<>();

        for (Vacature vacature : vacatures) {
            VacatureDTO tempVacatureDTO = new VacatureDTO();
            tempVacatureDTO.setUrl(vacature.getUrl());
            tempVacatureDTO.setTitel(vacature.getTitel());
            tempVacatureDTO.setId(vacature.getId());
            tempVacatureDTO.setDatum(vacature.getDatum());
            tempVacatureDTO.setGezien(vacature.isGezien());
            tempVacatureDTO.setStatus(vacature.getStatus());
            tempVacatureDTO.setManager(vacature.getManager());

            lijstNieuweVacatures.add(tempVacatureDTO);
        }
        return lijstNieuweVacatures;
    }

    public int aantalNieuweVacatures (List<String> zoekopdrachten){
        Set<Vacature> vacatures = new HashSet<>();
        LocalDate datum = LocalDate.now();

        if (zoekopdrachten.size() == 0) {
            vacatures.addAll(vacatureRepository.findAllByDatum(datum, ""));
        } else {
            for (String zoekopdracht : zoekopdrachten) {
                String filteropdracht = "%"+zoekopdracht+"%";
                vacatures.addAll(vacatureRepository.findAllByDatum(datum, filteropdracht));
            }
        }
        return vacatures.size();
    }

    public List<VacatureDTO> alleVacaturesOpStatus(Vacaturestatus status, int page, int size, String sortDir, String sort,
                                                List<String> zoekopdrachten) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(sortDir), sort);
        List<Vacature> vacatures = new ArrayList<>();

        if (zoekopdrachten.size() == 0) {
            vacatures = vacatureRepository.findAllByStatus(status, pageable).getContent();
        } else {
            vacatures = filterVacaturesOpStatus(status, zoekopdrachten, pageable).getContent();
        }

        List<VacatureDTO> lijstVacaturesOpStatus = new ArrayList<>();

        for (Vacature vacature : vacatures) {
            VacatureDTO tempVacatureDTO = new VacatureDTO();
            tempVacatureDTO.setUrl(vacature.getUrl());
            tempVacatureDTO.setTitel(vacature.getTitel());
            tempVacatureDTO.setId(vacature.getId());
            tempVacatureDTO.setDatum(vacature.getDatum());
            tempVacatureDTO.setGezien(vacature.isGezien());
            tempVacatureDTO.setStatus(vacature.getStatus());
            tempVacatureDTO.setManager(vacature.getManager());

            lijstVacaturesOpStatus.add(tempVacatureDTO);
        }
        return lijstVacaturesOpStatus;
    }

    public int aantalVacaturesOpStatus(Vacaturestatus status, List<String> zoekopdrachten) {
        Set<Vacature> vacatures = new HashSet<>();

        if (zoekopdrachten.size() == 0) {
            vacatures.addAll(vacatureRepository.findAllByStatus(status));
        } else {
            for (String zoekopdracht : zoekopdrachten) {
                String filteropdracht = "%"+zoekopdracht+"%";
                vacatures.addAll(vacatureRepository.findAllByStatus(status, filteropdracht));
            }
        }

        return vacatures.size();
    }

    public List<VacatureDTO> alleGearchiveerdeVacatures(int page, int size, String sortDir, String sort, List<String> zoekopdrachten) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(sortDir), sort);
        List<Vacature> vacatures = new ArrayList<>();

        if (zoekopdrachten.size() == 0) {
            vacatures = vacatureRepository.findAllByGearchiveerd(true, "", pageable).getContent();
        } else {
            vacatures = filterGearchiveerdeVacatures(zoekopdrachten, pageable).getContent();
        }

        List<VacatureDTO> gearchiveerdeVacatures = new ArrayList<>();

        for (Vacature vacature : vacatures) {
            VacatureDTO tempVacatureDTO = new VacatureDTO();
            tempVacatureDTO.setUrl(vacature.getUrl());
            tempVacatureDTO.setTitel(vacature.getTitel());
            tempVacatureDTO.setId(vacature.getId());
            tempVacatureDTO.setDatum(vacature.getDatum());
            tempVacatureDTO.setGezien(vacature.isGezien());
            tempVacatureDTO.setStatus(vacature.getStatus());
            tempVacatureDTO.setManager(vacature.getManager());

            gearchiveerdeVacatures.add(tempVacatureDTO);
        }
        return gearchiveerdeVacatures;
    }

    public int aantalGearchiveerdeVacatures(List<String> zoekopdrachten) {
        Set<Vacature> vacatures = new HashSet<>();

        if (zoekopdrachten.size() == 0) {
            vacatures.addAll(vacatureRepository.findAllByGearchiveerd(true, ""));
        } else {
            for (String zoekopdracht : zoekopdrachten) {
                String filteropdracht = "%"+zoekopdracht+"%";
                vacatures.addAll(vacatureRepository.findAllByGearchiveerd(true, filteropdracht));
            }
        }

        return vacatures.size();
    }

    private PageImpl<Vacature> filterVacatures(List<String> filters, Pageable pageable) {

        switch (filters.size()) {
            case 1: {
                return vacatureRepository.findAllByTekst(filters.get(0), pageable);
            }
            case 2: {
                return vacatureRepository.findAllByTekst(filters.get(0), filters.get(1), pageable);
            }
            case 3: {
                return vacatureRepository.findAllByTekst(filters.get(0), filters.get(1), filters.get(2), pageable);
            }
            case 4: {
                return vacatureRepository.findAllByTekst(filters.get(0), filters.get(1), filters.get(2),
                        filters.get(3), pageable);
            }
            case 5: {
                return vacatureRepository.findAllByTekst(filters.get(0), filters.get(1), filters.get(2),
                        filters.get(3), filters.get(4), pageable);
            }
            case 6: {
                return vacatureRepository.findAllByTekst(filters.get(0), filters.get(1), filters.get(2),
                        filters.get(3), filters.get(4), filters.get(5), pageable);
            }
        }
        return null;
    }

    private PageImpl<Vacature> filterNieuweVacatures(List<String> filters, Pageable pageable) {
        LocalDate datum = LocalDate.now();

        switch (filters.size()) {
            case 1: {
                return vacatureRepository.findAllByDatum(datum, filters.get(0), pageable);
            }
            case 2: {
                return vacatureRepository.findAllByDatum(datum, filters.get(0), filters.get(1), pageable);
            }
            case 3: {
                return vacatureRepository.findAllByDatum(datum, filters.get(0), filters.get(1),
                        filters.get(2), pageable);
            }
            case 4: {
                return vacatureRepository.findAllByDatum(datum, filters.get(0), filters.get(1),
                        filters.get(2), filters.get(3), pageable);
            }
            case 5: {
                return vacatureRepository.findAllByDatum(datum, filters.get(0), filters.get(1),
                        filters.get(2), filters.get(3), filters.get(4), pageable);
            }
            case 6: {
                return vacatureRepository.findAllByDatum(datum, filters.get(0), filters.get(1),
                        filters.get(2), filters.get(3), filters.get(4), filters.get(5), pageable);
            }
        }
        return null;
    }

    private PageImpl<Vacature> filterVacaturesOpStatus(Vacaturestatus status, List<String> filters, Pageable pageable) {

        switch (filters.size()) {
            case 1: {
                return vacatureRepository.findAllByStatus(status, filters.get(0), pageable);
            }
            case 2: {
                return vacatureRepository.findAllByStatus(status, filters.get(0), filters.get(1), pageable);
            }
            case 3: {
                return vacatureRepository.findAllByStatus(status, filters.get(0), filters.get(1),
                        filters.get(2), pageable);
            }
            case 4: {
                return vacatureRepository.findAllByStatus(status, filters.get(0), filters.get(1),
                        filters.get(2), filters.get(3), pageable);
            }
            case 5: {
                return vacatureRepository.findAllByStatus(status, filters.get(0), filters.get(1),
                        filters.get(2), filters.get(3), filters.get(4), pageable);
            }
            case 6: {
                return vacatureRepository.findAllByStatus(status, filters.get(0), filters.get(1),
                        filters.get(2), filters.get(3), filters.get(4), filters.get(5), pageable);
            }
        }
        return null;
    }

    private PageImpl<Vacature> filterGearchiveerdeVacatures(List<String> filters, Pageable pageable) {

        switch (filters.size()) {
            case 1: {
                return vacatureRepository.findAllByGearchiveerd(true, filters.get(0), pageable);
            }
            case 2: {
                return vacatureRepository.findAllByGearchiveerd(true, filters.get(0), filters.get(1), pageable);
            }
            case 3: {
                return vacatureRepository.findAllByGearchiveerd(true, filters.get(0), filters.get(1),
                        filters.get(2), pageable);
            }
            case 4: {
                return vacatureRepository.findAllByGearchiveerd(true, filters.get(0), filters.get(1),
                        filters.get(2), filters.get(3), pageable);
            }
            case 5: {
                return vacatureRepository.findAllByGearchiveerd(true, filters.get(0), filters.get(1),
                        filters.get(2), filters.get(3), filters.get(4), pageable);
            }
            case 6: {
                return vacatureRepository.findAllByGearchiveerd(true, filters.get(0), filters.get(1),
                        filters.get(2), filters.get(3), filters.get(4), filters.get(5), pageable);
            }
        }
        return null;
    }

    public Set<Vacature> verwijderDuplicaten() {
        LOGGER.info("Vergelijken van vacatures is gestart.");

        List<Vacature> vacatures = vacatureRepository.findAll();
        List<Vacature> checks = vacatures.stream().filter(vacature -> vacature.getDatum().toLocalDateTime().getDayOfYear() == LocalDate.now().getDayOfYear()).collect(Collectors.toList());
        List<Vacature> trash = new ArrayList<>();

        Set<Vacature> gecheckteVacatures = new HashSet<>();
        Set<Vacature> duplicaten = new HashSet<>();

        vacatures.forEach(vacature1 -> {
            gecheckteVacatures.add(vacature1);
            checks.forEach(vacature2 -> {
                if (!gecheckteVacatures.contains(vacature2)) {
                    if (compareText(vacature1.getTekst(), vacature2.getTekst())) {
                        duplicaten.add(vacature2);
                        trash.add(vacature2);
                        LOGGER.info("Duplicaat gevonden: ID1 - " + vacature1.getId() + ", ID2 - " + vacature2.getId());
                    }
                }
            });
            checks.removeAll(trash);
            trash.clear();
        });

        // Verwijderen van alle dubbele vacatures
        vacatureRepository.deleteAll(duplicaten);

        LOGGER.info("Vergelijken van vacatures is beeïndigd.");
        LOGGER.info(duplicaten.size() + " duplicaten gevonden");
        return duplicaten;
    }

    private boolean compareText(String string1, String string2) {
        JaroWinklerDistance jwd = new JaroWinklerDistance();

        String shortest;
        String longest;

        if (string1.length() >= string2.length()) {
            longest = string1;
            shortest = string2;
        } else {
            shortest = string1;
            longest = string2;
        }

        double percentage = (double) shortest.length() / (double) longest.length() * 100.0;

        if (percentage > 80.0) {
            Double compare = jwd.apply(shortest, longest);
            return compare >= 0.9D && compare < 1.0D;
        }

        return false;
    }

    public Optional<VacatureDTO> geefVacature(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        Optional<Vacature> tempVacature = vacatureRepository.findById(id);

        if (tempVacature.isPresent()) {
            VacatureDTO vacatureDTO = modelMapper.map(tempVacature.get(), VacatureDTO.class);
            return Optional.of(vacatureDTO);
        }

        return Optional.empty();
    }

    public boolean wijzigGezien(Long id, VacatureDTO vacatureDTO) {
        Optional<Vacature> tempVacature = vacatureRepository.findById(id);

        if (tempVacature.isPresent()) {
            Vacature vacature = tempVacature.get();
            vacature.setGezien(true);
            vacatureRepository.save(vacature);

            return true;
        }

        return false;
    }

    public boolean wijzigStatus(Long vacature_id, Long gebruiker_id, VacatureDTO vacatureDTO) {
        Optional<Vacature> tempVacature = vacatureRepository.findById(vacature_id);
        Optional<Gebruiker> tempGebruiker = gebruikerRepository.findById(gebruiker_id);

        if (tempVacature.isPresent() && tempGebruiker.isPresent()) {
            Vacature vacature = tempVacature.get();
            Gebruiker gebruiker = tempGebruiker.get();
            String displaynaam = StringUtils.isBlank(gebruiker.getTussenvoegsel())
                    ? StringUtils.join(new String[]{gebruiker.getVoornaam(), gebruiker.getAchternaam()}, ' ')
                    : StringUtils.join(new String[]{gebruiker.getVoornaam(), gebruiker.getTussenvoegsel(), gebruiker.getAchternaam()}, ' ');

            if (vacatureDTO.getStatus() != Vacaturestatus.AFWACHTING) {
                vacature.setManager(displaynaam);
            } else {
                vacature.setManager(null);
            }
            vacature.setStatus(vacatureDTO.getStatus());
            vacatureRepository.save(vacature);

            return true;
        }

        return false;
    }

    public boolean wijzigNotities(Long id, VacatureDTO vacatureDTO) {
        Optional<Vacature> tempVacature = vacatureRepository.findById(id);

        if (tempVacature.isPresent()) {
            Vacature vacature = tempVacature.get();
            vacature.setNotities(vacatureDTO.getNotities());
            vacatureRepository.save(vacature);

            return true;
        }

        return false;
    }

    public boolean wijzigArchief(Long id, VacatureDTO vacatureDTO) {
        Optional<Vacature> tempVacature = vacatureRepository.findById(id);

        if (tempVacature.isPresent()) {
            Vacature vacature = tempVacature.get();
            vacature.setGearchiveerd(vacatureDTO.isGearchiveerd());
            vacatureRepository.save(vacature);

            return true;
        }

        return false;
    }

    public List<Vacature> verwijderOudeVacatures() {
        LOGGER.info("Start verwijderen van verouderde vacatures.");
        final LocalDateTime maandGeleden = new Timestamp(System.currentTimeMillis()).toLocalDateTime().minusMonths(1);

        List<Vacature> vacatures = vacatureRepository.findAll();

        List<Vacature> verouderdeVacatures = vacatures.stream()
                .filter(vacature -> vacature.getDatum().toLocalDateTime().isBefore(maandGeleden) && !vacature.isGearchiveerd())
                .collect(Collectors.toList());

        vacatureRepository.deleteAll(verouderdeVacatures);

        LOGGER.info(verouderdeVacatures.size() + " verouderde vacatures gevonden.");
        return verouderdeVacatures;
    }
}
