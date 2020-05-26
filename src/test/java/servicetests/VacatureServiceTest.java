package servicetests;

import com.example.crawlertest.domein.Vacature;
import com.example.crawlertest.domein.VacatureDTO;
import com.example.crawlertest.repositories.VacatureRepository;
import com.example.crawlertest.services.VacatureService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VacatureServiceTest {

    @Mock
    private VacatureRepository vacatureRepository;

    @InjectMocks
    private VacatureService vacatureService;

    @Test
    public void alleVacaturesTest(){

        int mockPage = 0;
        int mockSize = 25;
        String mockSort = "vacature";
        String mockSortDir = "DESC";
        List<String> mockZoekopdrachten = new ArrayList<>();
        Pageable mockPageable = PageRequest.of(mockPage, mockSize, Sort.Direction.valueOf(mockSortDir), mockSort);

        Vacature mockVacature = new Vacature();
        mockVacature.setTitel("testVacature");
        mockVacature.setUrl("www.mockUrl.nl");
        mockVacature.setTekst("java is een eiland");

        List<Vacature> mockVacatureLijst = new ArrayList<>();
        List<VacatureDTO> vacatureDTOList;
        mockVacatureLijst.add(mockVacature);
        PageImpl<Vacature> mockPageImpl = new PageImpl(mockVacatureLijst);

        when(vacatureRepository.findAllByTekst("", mockPageable)).thenReturn(mockPageImpl);

        vacatureDTOList = vacatureService.alleVacatures(mockPage, mockSize, mockSortDir, mockSort, mockZoekopdrachten);

        Assert.assertEquals(vacatureDTOList.size(), mockVacatureLijst.size());
        Assert.assertEquals(vacatureDTOList.get(0).getTitel(), mockVacatureLijst.get(0).getTitel());

    }

    @Test
    public void alleNieuweVacaturesTest(){

        int mockPage = 0;
        int mockSize = 25;
        String mockSort = "vacature";
        String mockSortDir = "DESC";
        LocalDate mockDatum= LocalDate.now();
        List<String> mockZoekOpdrachten = new ArrayList<>();
        Pageable mockPageable = PageRequest.of(mockPage, mockSize, Sort.Direction.valueOf(mockSortDir), mockSort);

        Vacature mockNieuweVacature = new Vacature();
        mockNieuweVacature.setTitel("testVacature");
        mockNieuweVacature.setUrl("www.mockUrl.nl");
        mockNieuweVacature.setTekst("java is een eiland");
        mockNieuweVacature.setDatum(Timestamp.valueOf(LocalDateTime.now()));

        List<Vacature> mockVacatureLijst = new ArrayList<>();
        List<VacatureDTO> vacatureDTOList;
        mockVacatureLijst.add(mockNieuweVacature);
        PageImpl<Vacature> mockPageImpl = new PageImpl(mockVacatureLijst);

        when(vacatureRepository.findAllByDatum(mockDatum, "", mockPageable)).thenReturn(mockPageImpl);

        vacatureDTOList = vacatureService.alleNieuweVacatures(mockPage, mockSize, mockSortDir, mockSort, mockZoekOpdrachten);

        Assert.assertEquals(vacatureDTOList.size(), mockVacatureLijst.size());
        Assert.assertTrue(vacatureDTOList.get(0).getDatum().equals(mockVacatureLijst.get(0).getDatum()));

    }

    @Test
    public void vacatureOpslaanTest() {
        Vacature mockVacature = new Vacature();
        mockVacature.setId(1L);
        mockVacature.setTitel("mockVacature");
        mockVacature.setUrl("www.mockUrl.nl");

        when(vacatureRepository.findByUrl(anyString())).thenReturn(Optional.of(mockVacature));
        boolean opgeslagen = vacatureService.vacatureOpslaan(mockVacature);
        Assert.assertTrue(opgeslagen);
    }

    @Test
    public void vergelijkVacaturesTest() {
        Vacature mockVacature1 = new Vacature();
        mockVacature1.setId(1L);
        mockVacature1.setTitel("Holy moly wat een vacature");
        mockVacature1.setUrl("https://mockurl.nl/mockvacature1");
        mockVacature1.setTekst("Dit is een fantastische vacature.");

        Vacature mockVacature2 = new Vacature();
        mockVacature2.setId(2L);
        mockVacature2.setTitel("Willekeurige java developer vacature - Deventer");
        mockVacature2.setUrl("https://mockurl.nl/mockvacature2");
        mockVacature2.setTekst("Gezocht: Java developer in Deventer." +
                "Ben jij een enthousiaste, jonge werkzoekende en heb je een voorliefde voor het schrijven van code" +
                "en Indonesische talen? Dan ben je bij ons aan het juiste adres! Wacht niet langer en solliciteer" +
                "direct op deze geweldige vacature. Alvast bedankt voor uw aandacht en hopelijk tot snel!");

        Vacature mockVacature3 = new Vacature();
        mockVacature3.setId(3L);
        mockVacature3.setTitel("Holy moly wat een vacature3");
        mockVacature3.setUrl("https://mockurl.nl/mockvacature3");
        mockVacature3.setTekst("Random string lowifajg8fhwafohaofihw" +
                "iofdwaoifh hfoaew ihfoiehw foiawhfo ihwofaihew o ihewoaihfe oihfeowaihf oihda" +
                "iofhwaofihewoaifheo i ofew ihfoeiwha oifhe oifhewoaihfe oiwhfao iehwaf.");

        Vacature mockVacature4 = new Vacature();
        mockVacature4.setId(4L);
        mockVacature4.setTitel("Holy moly wat een vacature4");
        mockVacature4.setUrl("https://mockurl.nl/mockvacature4");
        mockVacature4.setTekst("Random string iofdajoifd ifd aoihfdoiahf oidwahfoiw jfdoi" +
                "pugoewai jojewa8f daoihfewapo8 ufoihfe owo8ehwaf o8ho8ewao 8fewao8fueo a" +
                "9uewa0fu 90ewuaufhd aouhfeowaif hp8dewojdf poiophe afoufoie uapofueoijofie a" +
                "ofeo ufau09fue 09u feauf90 eia9 fe8au0f9e u0 f09u e0a9uf09ufoijewa oif jeowa" +
                "oifajwoifjewa ewofaijfoe aijwoihf eoiahfo eaipifh epoaihfiu hea.");

        Vacature mockVacature5 = new Vacature();
        mockVacature5.setId(5L);
        mockVacature5.setTitel("Willekeurige java developer vacature5 - Deventer");
        mockVacature5.setUrl("https://mockurl.nl/mockvacature5");
        mockVacature5.setTekst("Gezocht: Java developer in Deventer." +
                "Ben jij een enthousiaste, jonge werkzoekende en heb je een voorliefde voor programmeren" +
                "en Indonesische talen? Dan ben je bij ons aan het juiste adres! Wacht niet langer en solliciteer" +
                "direct op deze geweldige vacature. Alvast bedankt voor uw aandacht en hopelijk tot snel!");

        Vacature mockVacature6 = new Vacature();
        mockVacature6.setId(6L);
        mockVacature6.setTitel("Holy moly wat een vacature6");
        mockVacature6.setUrl("https://mockurl.nl/mockvacature6");
        mockVacature6.setTekst("Wat een tekst.");

        List<Vacature> vacatureLijst = new ArrayList<>();
        vacatureLijst.add(mockVacature1);
        vacatureLijst.add(mockVacature2);
        vacatureLijst.add(mockVacature3);
        vacatureLijst.add(mockVacature4);
        vacatureLijst.add(mockVacature5);
        vacatureLijst.add(mockVacature6);

        when(vacatureRepository.findAll()).thenReturn(vacatureLijst);

        Set<Vacature> dubbeleVacatures = vacatureService.verwijderDuplicaten();

        Assert.assertTrue(dubbeleVacatures.size() == 1);
    }
 }
