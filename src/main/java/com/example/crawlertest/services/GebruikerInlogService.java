package com.example.crawlertest.services;

import com.example.crawlertest.domein.Gebruiker;
import com.example.crawlertest.domein.GebruikerInlog;
import com.example.crawlertest.repositories.GebruikerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Optional;


/**
 * Service voor alle end-points voor de gebruiker om informatie uit en in
 * de database te krijgen/wijzigen.
 */
@Service
public class GebruikerInlogService implements UserDetailsService {
    @Autowired
    private GebruikerRepository gebruikerRepository;

    /**
     * Overridden methode voor loadUserByUsername zodat er een exception gethrowd wordt
     * wanneer de username van de gebruiker niet voorkomt.
     *
     * @param username van de gebruiker
     * @return Returnt het volledige gbruiker object
     * @throws UsernameNotFoundException als de opgegeven username niet wordt gevonden.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<Gebruiker> gebruiker = gebruikerRepository.findByGebruikersnaam(username);

        return gebruiker.map(GebruikerInlog::new)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Gebruiker '%s' niet gevonden", username)));
    }
}
