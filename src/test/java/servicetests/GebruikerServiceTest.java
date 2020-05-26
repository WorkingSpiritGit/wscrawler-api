package servicetests;

import com.example.crawlertest.domein.Gebruiker;
import com.example.crawlertest.domein.GebruikersRol;
import com.example.crawlertest.domein.WachtwoordDTO;
import com.example.crawlertest.repositories.GebruikerRepository;
import com.example.crawlertest.services.GebruikerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GebruikerServiceTest {

    @Mock
    private GebruikerRepository gebruikerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private GebruikerService gebruikerService;

    @Test
    public void gebruikerOpslaanTest() {
        Gebruiker mockGebruiker = new Gebruiker();
        mockGebruiker.setId(235L);
        mockGebruiker.setVoornaam("Mockie");
        mockGebruiker.setTussenvoegsel("de");
        mockGebruiker.setAchternaam("Mockgebruiker");
        mockGebruiker.setEmailadres("mockmock@mock.nl");
        mockGebruiker.setRol(GebruikersRol.ACCOUNTMANAGER);

        when(gebruikerRepository.findByGebruikersnaam(mockGebruiker.getVoornaam().substring(0,1) +
                mockGebruiker.getAchternaam())).thenReturn(Optional.empty());
        when(gebruikerRepository.findById(mockGebruiker.getId())).thenReturn(Optional.of(mockGebruiker));

        boolean resultaatOpslag = gebruikerService.gebruikerOpslaan(mockGebruiker);

        Assert.assertTrue(resultaatOpslag);
    }

    @Test
    public void wachtwoordWijzigenTest() {
        Gebruiker mockGebruiker = new Gebruiker();
        mockGebruiker.setId(143L);
        mockGebruiker.setVoornaam("Harrie");
        mockGebruiker.setAchternaam("Test");
        mockGebruiker.setGebruikersnaam("HTest");
        mockGebruiker.setEmailadres("h.test@test.net");
        mockGebruiker.setWachtwoord("bamischijf");
        mockGebruiker.setRol(GebruikersRol.ACCOUNTMANAGER);

        WachtwoordDTO wachtwoordDTO = new WachtwoordDTO();
        wachtwoordDTO.setHuidigWachtwoord("bamischijf");
        wachtwoordDTO.setNieuwWachtwoord("H@rrieT3stIsTh3B4$t");

        when(gebruikerRepository.findById(mockGebruiker.getId())).thenReturn(Optional.of(mockGebruiker));
        when(passwordEncoder.matches(wachtwoordDTO.getHuidigWachtwoord(), mockGebruiker.getWachtwoord())).thenReturn(true);

        boolean resultaatWijziging = gebruikerService.wachtwoordWijzigen(mockGebruiker.getId(), wachtwoordDTO);

        Assert.assertTrue(resultaatWijziging);
    }
}
