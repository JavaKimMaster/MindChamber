package mind.chamber.naversearchdb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mind.chamber.naversearchdb.data.NaverBlogItem;
import mind.chamber.naversearchdb.dto.NaverTrendRequest;
import mind.chamber.naversearchdb.dto.NaverTrendResponse;
import mind.chamber.naversearchdb.entity.NaverTrendEntity;
import mind.chamber.naversearchdb.handler.NaverBlogHandler;
import mind.chamber.naversearchdb.handler.NaverTrendHandler;
import mind.chamber.naversearchdb.service.NaverSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
public class NaverSearchServiceImpl implements NaverSearchService {

    NaverBlogHandler naverBlogHandler;
    NaverTrendHandler naverTrendHandler;

    @Autowired
    public  NaverSearchServiceImpl(NaverBlogHandler naverBlogHandler,
                                   NaverTrendHandler naverTrendHandler) {
        this.naverBlogHandler = naverBlogHandler;
        this.naverTrendHandler = naverTrendHandler;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(NaverSearchServiceImpl.class);

    @Override
    public List<NaverBlogItem> getSearchBlogItems(String query, String display, String start, String sort, String naverClientID, String naverClientSecret) {

        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/blog.json")
                .queryParam("query", query)
                .queryParam("display", display)
                .queryParam("start", start)
                .queryParam("sort", sort)
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", naverClientID)
                .header("X-Naver-Client-Secret", naverClientSecret)
                .build()
                ;

        RestTemplate restTemplate = new RestTemplate();
        //ResponseEntity<NaverBlogResponse> responseEntity = restTemplate.exchange(requestEntity, NaverBlogResponse.class);

        // 💡 응답을 JSON 문자열로 바로 받기
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);


        LOGGER.info("getSearchBlog 호출");
        LOGGER.info("status code : {}", responseEntity.getStatusCode());
        LOGGER.info("body : {}", responseEntity.getBody());

        String body = responseEntity.getBody(); // 전체 JSON 문자열

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;

        try {
            rootNode = objectMapper.readTree(body); // JSON 파싱
            JsonNode itemsNode = rootNode.get("items"); // items 배열만 추출

            // itemsNode → List<NaverBlogItem>로 매핑
            List<NaverBlogItem> itemList = objectMapper.readerForListOf(NaverBlogItem.class).readValue(itemsNode);

            if (naverBlogHandler.saveBlogItem(itemList)) {
                LOGGER.info("naverBlogHandler.saveBlogItem(itemList) 호출");
                return itemList;
            }
            else {
                LOGGER.info("naverBlogHandler.saveBlogItem(itemList) ");
                return Collections.emptyList(); // 실패 시 빈 리스트 반환
            }


        } catch (Exception e) {
            LOGGER.error("JSON 파싱 오류", e);
            return Collections.emptyList(); // 실패 시 빈 리스트 반환
        }

    }


    public NaverTrendResponse getTrend(NaverTrendRequest requestDto, String naverClientID, String naverClientSecret) {

        LOGGER.info("getTrend 호출");

        String url = "https://openapi.naver.com/v1/datalab/search";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Naver-Client-Id", naverClientID);
        headers.set("X-Naver-Client-Secret", naverClientSecret);

        HttpEntity<NaverTrendRequest> request = new HttpEntity<>(requestDto, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<NaverTrendResponse> response = restTemplate.postForEntity(
                url, request, NaverTrendResponse.class);


        LOGGER.info("status code : {}", response.getStatusCode());
        LOGGER.info("body : {}", response.getBody());

        try {
            NaverTrendResponse responseBody = response.getBody();

            if ( responseBody != null && naverTrendHandler.saveTrendData(responseBody.getResults()) ) {
                LOGGER.info("naverTrendHandler.saveTrendData(results) 호출");
                return responseBody;
            } else {
                LOGGER.info("naverTrendHandler.saveTrendData 실패");
                return null;
            }

        } catch (Exception e) {
            LOGGER.error("처리 중 오류 발생", e);
            return null;
        }

    }


}
