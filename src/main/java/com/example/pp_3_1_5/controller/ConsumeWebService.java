package com.example.pp_3_1_5.controller;

import com.example.pp_3_1_5.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
public class ConsumeWebService {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping
    public String getCode() {
        String requestUrl = "http://94.198.50.185:7081/api/users";
        String code = "";
        // 1)
        ResponseEntity<List<User>> response =
                restTemplate.exchange(requestUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        // 2)
        String cookies = response.getHeaders().getValuesAsList("Set-Cookie").get(0);
        String jsessionid = cookies.substring(cookies.indexOf("JSESSIONID="), cookies.indexOf(";"));
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.add("Cookie", jsessionid);
        // 3)
        User newUser = new User(3L, "James", "Brown", (byte)41);
        HttpEntity<User> entity = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> response1 = restTemplate.exchange(requestUrl, HttpMethod.POST, entity, String.class);
        code += response1.getBody();
        // 4)
        User user = new User(3L, "Thomas", "Shelby", (byte)41);
        HttpEntity<User> entity1 = new HttpEntity<>(user, headers);
        ResponseEntity<String> response2 = restTemplate.exchange(requestUrl, HttpMethod.PUT, entity1, String.class);
        code += response2.getBody();
        // 5)
        HttpEntity<User> entity2 = new HttpEntity<>(headers);
        ResponseEntity<String> response3 = restTemplate.exchange(requestUrl + "/3", HttpMethod.DELETE, entity2, String.class);
        code += response3.getBody();
        System.out.println("CODE - " + code);
        return code;
    }
}