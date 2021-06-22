package de.pd.crawler.htmlUnit;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import de.pd.crawler.entities.DomNodeEntity;
import de.pd.crawler.entities.DomNodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ScraperService {

    private final DomNodeRepository domNodeRepository;

    public ScraperService(DomNodeRepository domNodeRepository) {
        this.domNodeRepository = domNodeRepository;
    }

    public void scrapeByXpath(String xpath) {
        HtmlPage page;
        try (final WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
            WebClientOptions options = webClient.getOptions();
            options.setCssEnabled(true);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            options.setTimeout(50000);
            webClient.addRequestHeader("Access-Control-Allow-Origin", "*");

            page = webClient.getPage("http://quotes.toscrape.com/js/");
            List<Object> byXPath = page.getByXPath(xpath);
        } catch (Exception e) {
            log.error("Failed scraping", e);
        }
    }

    public void scrape() {
        HtmlPage page;
        try (final WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
            WebClientOptions options = webClient.getOptions();
            options.setCssEnabled(true);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            options.setTimeout(50000);
            webClient.addRequestHeader("Access-Control-Allow-Origin", "*");

            page = webClient.getPage("http://quotes.toscrape.com/js/");

            // Waittime for headless browsers to render js
            webClient.setJavaScriptTimeout(3000);
            webClient.waitForBackgroundJavaScript(3000);
            //just wait
            Thread.sleep(3000);
            log.info("Deleting current data");
            domNodeRepository.deleteAll();
            log.info("Start traversal");
            domNodeRepository.save(traverse(page));

            log.info("Done scraping");
        } catch (Exception e) {
            log.error("Failed scraping", e);
        }
    }

    public DomNodeEntity traverse(DomNode domNode) {
        DomNodeEntity domNodeEntity = new DomNodeEntity();
        domNodeEntity.setName(domNode.getLocalName());
        domNodeEntity.setValue(domNode.getNodeValue());
        try {
            domNodeEntity.setXPath(domNode.getCanonicalXPath());
        } catch (RuntimeException e) {
            log.debug("Node " + domNode + " does not have xpath");
        }
        if (domNode instanceof DomElement) {
            DomElement domElement = (DomElement) domNode;
            domElement.getAttributesMap().entrySet()
                    .forEach(entry -> domNodeEntity.getAttributes()
                            .put(entry.getKey(), entry.getValue().getValue()));
        }
        List<DomNodeEntity> children = new ArrayList<>();
        if (domNode.hasChildNodes()) {
            domNode.getChildren().forEach(child -> children.add(this.traverse(child)));
        }
        domNodeEntity.setChildren(children);
        return domNodeEntity;
    }

}

