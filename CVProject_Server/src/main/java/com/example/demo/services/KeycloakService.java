package com.example.demo.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
@Slf4j
@Service
public class KeycloakService {

    @Value("${keycloak.base-url}")
    private String url;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.client-id}")
    private String clientId;

    private RestTemplate restTemplate;

    public Boolean introspectToken(String token) throws Exception {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("token", token);

        log.info("client_id: "+ clientId);
        log.info("client_secret: "+ clientSecret);
        log.info("token: "+ token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        log.info("Header: "+headers);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        log.info("Request API entity: "+entity);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        log.info("Response API entity: "+response);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.getBody());
        boolean active = jsonNode.get("active").asBoolean();
        log.info("Introspect: " +active);
        return active;
    }
}
