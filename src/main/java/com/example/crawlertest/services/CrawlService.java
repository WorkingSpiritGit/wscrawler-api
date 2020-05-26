package com.example.crawlertest.services;

import com.example.crawlertest.domein.HtmlCrawler;
import com.example.crawlertest.domein.Website;
import com.example.crawlertest.domein.Zoekterm;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CrawlService {

    private WebsiteService websiteService;
    private VacatureService vacatureService;
    private ZoektermService zoektermService;

    private final Logger LOGGER = Logger.getLogger("CrawlServiceLog");

    @Autowired
    public CrawlService(WebsiteService websiteService, VacatureService vacatureService, ZoektermService zoektermService) {
        this.websiteService = websiteService;
        this.vacatureService = vacatureService;
        this.zoektermService = zoektermService;
    }

    public void crawlWebsites() {
        final int AANTAL_THREADS = 1000;
        final List<Zoekterm> zoektermen = zoektermService.geefAlleZoektermen();
        final List<Website> websites = websiteService.geefAlleWebsites();

        CrawlConfig config = geefCrawlConfig();

        for (Website website : websites) {
            File crawlOpslag = new File("src/main/resources/crawlerOpslag/" + website.getNaam());
            config.setCrawlStorageFolder(crawlOpslag.getAbsolutePath());

            PageFetcher fetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, fetcher);

            try {
                CrawlController crawlManager = new CrawlController(config, fetcher, robotstxtServer);
                crawlManager.addSeed(website.getUrl());     // voegt starturl toe aan crawler

                CrawlController.WebCrawlerFactory<HtmlCrawler> crawlerFactory = () -> new HtmlCrawler(website,
                        vacature -> vacatureService.vacatureOpslaan(vacature), zoektermen);

                crawlManager.startNonBlocking(crawlerFactory, AANTAL_THREADS);
                LOGGER.info("Start crawlen van " + website.getNaam());
            } catch (Exception e) {
                LOGGER.info("Exception tijdens crawlen van website: " + website.getNaam());
                e.printStackTrace();
            }
        }
    }

    private CrawlConfig geefCrawlConfig() {

        CrawlConfig config = new CrawlConfig();

        config.setMaxDepthOfCrawling(2);
        config.setCleanupDelaySeconds(60);
        config.setThreadShutdownDelaySeconds(60);
        config.setThreadMonitoringDelaySeconds(60);
        config.setIncludeHttpsPages(true);
        config.setIncludeBinaryContentInCrawling(false);

        return config;
    }
}
