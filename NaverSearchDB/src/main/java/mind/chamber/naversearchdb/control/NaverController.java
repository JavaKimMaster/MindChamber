package mind.chamber.naversearchdb.control;

import mind.chamber.naversearchdb.data.NaverBlogItem;
import mind.chamber.naversearchdb.dto.NaverTrendRequest;
import mind.chamber.naversearchdb.dto.NaverTrendResponse;
import mind.chamber.naversearchdb.service.NaverSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/naver")
public class NaverController {

    NaverSearchService naverSearchService;

    @Autowired
    public NaverController(NaverSearchService naverSearchService) {
        this.naverSearchService = naverSearchService;
    }

    @GetMapping(value = "/blog/search/items")
    public ResponseEntity<List<NaverBlogItem>> getSearchBlogItems(
            @RequestParam String query,
            @RequestParam String display,
            @RequestParam String start,
            @RequestParam String sort,
            @RequestParam String naverClientID,
            @RequestParam String naverClientSecret
    ) {

        List<NaverBlogItem> items = naverSearchService.getSearchBlogItems(query, display, start, sort, naverClientID, naverClientSecret);

        return ResponseEntity.ok(items); // JSON 배열로 응답됨
    }


    /*
    RequestBody Example
    {
        "startDate": "2024-01-01",
        "endDate": "2024-03-31",
        "timeUnit": "month",
        "keywordGroups": [
            {
            "groupName": "주식",
            "keywords": ["비트코인", "삼성전자"]
            }
        ],
        "device": "pc",
        "ages": ["3", "4"],
        "gender": "m"
    }
     */
    @PostMapping(value = "/blog/trend")
    public NaverTrendResponse getTrend(@RequestBody NaverTrendRequest request,
                                        @RequestParam String naverClientID,
                                        @RequestParam String naverClientSecret
                                        ) {
        // System.out.println("NaverController:NaverTrendResponse:getTrend: clientId: " + naverClientID);
        // System.out.println("NaverController:NaverTrendResponse:getTrend: clientSecret: " + naverClientSecret);
        return naverSearchService.getTrend(request, naverClientID, naverClientSecret );
    }


    /*
    Embedded 방식으로 구현
    복합키 6개:  response의 startDate, endDate, timeUnit, groupTitle, keyword, period
     */
    /*
    RequestBody Example
    {
        "startDate": "2024-01-01",
        "endDate": "2024-03-31",
        "timeUnit": "month",
        "keywordGroups": [
            {
            "groupName": "주식",
            "keywords": ["비트코인", "삼성전자"]
            }
        ],
        "device": "pc",
        "ages": ["3", "4"],
        "gender": "m"
    }
     */
    @PostMapping(value = "/blog/trend2")
    public NaverTrendResponse getTrend2(@RequestBody NaverTrendRequest request,
                                       @RequestParam String naverClientID,
                                       @RequestParam String naverClientSecret
    ) {
        // System.out.println("NaverController:NaverTrendResponse:getTrend: clientId: " + naverClientID);
        // System.out.println("NaverController:NaverTrendResponse:getTrend: clientSecret: " + naverClientSecret);
        return naverSearchService.getTrend2(request, naverClientID, naverClientSecret );
    }

}
