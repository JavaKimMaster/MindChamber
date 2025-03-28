package mind.chamber.naversearch.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mind.chamber.naversearch.data.NaverBlogResponse;
import mind.chamber.naversearch.service.NaverSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mind.chamber.naversearch.data.NaverBlogItem;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class NaverSerarchServiceImpl implements NaverSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NaverSerarchServiceImpl.class);

    @Override
    public String getSearchBlog(String query, String display, String start, String sort, String naverClientID, String naverClientSecret) {


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

        return responseEntity.getBody();
    }

    @Override
    public String getSearchBlog2(String query, String display, String start, String sort, String naverClientID, String naverClientSecret) {

        LOGGER.info("getSearchBlog 호출");

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

        LOGGER.info("set Http Request Header");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList( new MediaType[] {MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Naver-Client-Id", naverClientID);
        headers.set("X-Naver-Client-Secret", naverClientSecret);

        HttpEntity<String> entity = new HttpEntity<> ("",  headers);

        RestTemplate restTemplate = new RestTemplate();

        LOGGER.info("requst by restTemplate");

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        LOGGER.info("status code : {}", responseEntity.getStatusCode());
        LOGGER.info("body : {}", responseEntity.getBody());

        return responseEntity.getBody();
    }

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

            return itemList;

        } catch (Exception e) {
            LOGGER.error("JSON 파싱 오류", e);
            return Collections.emptyList(); // 실패 시 빈 리스트 반환
        }

    }

    @Override
    public List<NaverBlogItem> getSearchBlogItems2(String query, String display, String start, String sort, String naverClientID, String naverClientSecret) {

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


        LOGGER.info("set Http Request Header : getSearchBlogItems2");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList( new MediaType[] {MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Naver-Client-Id", naverClientID);
        headers.set("X-Naver-Client-Secret", naverClientSecret);

        HttpEntity<String> entity = new HttpEntity<> ("",  headers);

        RestTemplate restTemplate = new RestTemplate();

        LOGGER.info("requst by restTemplate");

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

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

            return itemList;

        } catch (Exception e) {
            LOGGER.error("JSON 파싱 오류", e);
            return Collections.emptyList(); // 실패 시 빈 리스트 반환
        }

    }

}
