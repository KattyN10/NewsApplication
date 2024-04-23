package hcmute.kltn.backend.schedule;

import hcmute.kltn.backend.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final CrawlerService crawlerService;

    @Scheduled(fixedRate = 60000) // schedule per 1 minute
    public void crawlerArticleVnExpress() {
        System.out.println("----------Crawl in VnExpress----------");
        crawlerService.crawlVnExpress();
        System.out.println("-----End function crawl VnExpress-----");
        System.out.println("--------------------------------------\r");
        System.out.println("------------Crawl in Dan Tri----------");
        crawlerService.crawlDanTri();
        System.out.println("------End function crawl Dan Tri------");
        System.out.println("-------------------------------------- \r");
    }

}
