package mind.chamber.naversearch.control;

import mind.chamber.naversearch.data.NaverBlogItem;
import mind.chamber.naversearch.data.NaverBlogResponse;
import mind.chamber.naversearch.service.NaverSearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/naver")
public class NaverController {

    NaverSearchService naverSearchService;

    @Autowired
    public NaverController(NaverSearchService naverSearchService) {
        this.naverSearchService = naverSearchService;
    }

    @GetMapping(value = "/blog/search")
    public ResponseEntity<String> getSearchBlog(
            @RequestParam String query,
            @RequestParam String display,
            @RequestParam String start,
            @RequestParam String sort,
            @RequestParam String naverClientID,
            @RequestParam String naverClientSecret
    ) {

        String result = naverSearchService.getSearchBlog(query, display, start, sort, naverClientID, naverClientSecret);
        //return ResponseEntity.ok(result);
        return ResponseEntity.ok().body(result);  // 클라이언트는 JSON처럼 받게 됨
    }

    @GetMapping(value = "/blog/search2")
    public ResponseEntity<String> getSearchBlog2(
            @RequestParam String query,
            @RequestParam String display,
            @RequestParam String start,
            @RequestParam String sort,
            @RequestParam String naverClientID,
            @RequestParam String naverClientSecret
    ) {

        String result =  naverSearchService.getSearchBlog2(query, display, start, sort, naverClientID, naverClientSecret);

        return ResponseEntity.ok().body(result);  // 클라이언트는 JSON처럼 받게 됨
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

    @GetMapping(value = "/blog/search/items2")
    public ResponseEntity<List<NaverBlogItem>> getSearchBlogItems2(
            @RequestParam String query,
            @RequestParam String display,
            @RequestParam String start,
            @RequestParam String sort,
            @RequestParam String naverClientID,
            @RequestParam String naverClientSecret
    ) {

        List<NaverBlogItem> items = naverSearchService.getSearchBlogItems2(query, display, start, sort, naverClientID, naverClientSecret);

        return ResponseEntity.ok(items); // JSON 배열로 응답됨
    }

}
