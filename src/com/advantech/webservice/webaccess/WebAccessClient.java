/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.webaccess;

import java.util.Arrays;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author MFG.ESOP
 */
public class WebAccessClient {

    @Value("${webaccess.webservice.url:}")
    private String serviceUrl;

    @Value("${webaccess.webservice.account:}")
    private String serviceLoginAcc;

    @Value("${webaccess.webservice.password:}")
    private String serviceLoginPsw;

    private RestTemplate restTemplate;
    
    public WebAccessClient(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    private HttpHeaders getHeaders() {
        String plainCredentials = serviceLoginAcc + ":" + serviceLoginPsw;
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public <T extends Object> T sendRequest(String url, Map requestMap, Class<T> type) {
        HttpEntity<Map> request = new HttpEntity<Map>(requestMap, getHeaders());
        T result = restTemplate.postForObject(serviceUrl + url, request, type);
        return result;
    }
}
