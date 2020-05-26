package com.example.crawlertest.domein;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class HtmlCrawler extends WebCrawler {

    private final static Pattern UITZONDERINGEN = Pattern.compile(
                    ".*(\\.(css|js|ts|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
                    "|rm|smil|wmv|swf|wma|zip|rar|gz|txt|svg|woff2|ttf|php|ni|design))$");

    private final Logger LOGGER = Logger.getLogger(HtmlCrawler.class.getName());

    private Website website;
    private CallBack callBack;
    private List<Zoekterm> zoektermen;

    public HtmlCrawler(Website website, CallBack callBack, List<Zoekterm> zoektermen) {

        this.website = website;
        this.callBack = callBack;
        this.zoektermen = zoektermen;
    }

    @Override
    public boolean shouldVisit(Page referentiePagina, WebURL url) {
        String urlString = url.getURL().toLowerCase();
        return !UITZONDERINGEN.matcher(urlString).matches();
//                && urlString.startsWith(urlString);
    }

    @Override
    public void visit(Page webpagina) {
        String url = webpagina.getWebURL().getURL();

        Vacature vacature = new Vacature();

        if (webpagina.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) webpagina.getParseData();
            String html = htmlParseData.getHtml();

            Document document = Jsoup.parse(html);

        try {
            String content = !document.getElementsByAttributeValueStarting("ID", website.getVacatureTekstTag()).text().isEmpty() ?
                             document.getElementsByAttributeValueStarting("ID", website.getVacatureTekstTag()).text() :
                             document.getElementsByAttributeValueStarting("CLASS", website.getVacatureTekstTag()).text();
            String titel = htmlParseData.getTitle();

                if (zoektermen.stream().anyMatch(zoekterm -> content.toLowerCase().contains(zoekterm.getNaam().toLowerCase())) &&
                Pattern.compile(website.getFilter()).matcher(url).find()) {

                    vacature.setTitel(titel);
                    vacature.setTekst(content);
                    vacature.setUrl(url);

                    callBack.verwerkVacature(vacature);
                }
            } catch (Exception e){
                LOGGER.info("Er is geen vacaturetekst gevonden.");
            }
        }
    }
}
