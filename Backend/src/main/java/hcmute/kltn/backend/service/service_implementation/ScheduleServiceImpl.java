package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl {
    private final CrawlerService crawlerService;

    @Scheduled(fixedRate = 300000) // schedule per 5 minute
    public void crawlerArticle() {
        crawlerService.crawlVnExpress();
    }
}
