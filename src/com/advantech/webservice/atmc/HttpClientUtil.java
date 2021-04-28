/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.atmc;

import com.google.gson.Gson;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Wei.Cheng
 */
public class HttpClientUtil {

    private static SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("SSL");

        // set up a TrustManager that trusts everything
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                System.out.println("getAcceptedIssuers =============");
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs,
                    String authType) {
                System.out.println("checkClientTrusted =============");
            }

            public void checkServerTrusted(X509Certificate[] certs,
                    String authType) {
                System.out.println("checkServerTrusted =============");
            }
        }}, new SecureRandom());
        
        return sslContext;
    }

    public static String doGet(String url, Map<String, String> params, String charset) throws Exception {

        String result = null;

        URIBuilder builder = new URIBuilder(url);

        params.forEach((k, v) -> {
            builder.setParameter(k, v);
        });

        HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(getSSLContext())).build();
        HttpGet httpGet = new HttpGet(builder.build());
        httpGet.addHeader("Content-Type", "application/json");
        HttpResponse response = httpClient.execute(httpGet);
        if (response != null) {
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, charset);
            }
        }

        return result;
    }

    public static String doPost(String url, Map<String, String> params, String charset) throws Exception {

        String result = null;

        HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(getSSLContext())).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        StringEntity se = new StringEntity(new Gson().toJson(params));
        se.setContentType("text/json");
        se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
        httpPost.setEntity(se);
        HttpResponse response = httpClient.execute(httpPost);
        if (response != null) {
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, charset);
            }
        }

        return result;
    }
}
