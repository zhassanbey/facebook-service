/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookservice.controller.web.client.wrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import kz.alem.semantics.facebook.service.facebookservice.controller.constants.FacebookLightConstants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Zhasan
 */
public class WebClientWrapperLight {

    private HttpClient httpClient;
    private CookieStore cookieStore;
    private BasicHttpContext httpContext;

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public BasicHttpContext getHttpContext() {
        return httpContext;
    }

    public void setHttpContext(BasicHttpContext httpContext) {
        this.httpContext = httpContext;
    }

    public WebClientWrapperLight() {
        httpClient = HttpClientBuilder.create().build();
        cookieStore = new BasicCookieStore();
        httpContext = new BasicHttpContext();

        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
    }

    public boolean sendForm(Map<String, String> map) throws UnsupportedEncodingException, IOException{
        
        String requestUrl = "https://m.facebook.com/composer/mbasic/";
        
        System.out.println(requestUrl);
        
        HttpPost post = new HttpPost(requestUrl);
        
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        
        map.remove("action");
        
        List<NameValuePair> nvps = new ArrayList<>();
        
//        for(String key:map.keySet()){
//            if(key.startsWith("view") && ! key.contains("post")){
//                map.remove(key);
//            }
//        }
        
        for(String key:map.keySet()){
            System.out.println("Settign name value pair "+key+" = "+map.get(key));
             //nvps.add(new BasicNameValuePair(key, StringEscapeUtils.unescapeXml(map.get(key))));
            builder.addTextBody(key, map.get(key));
        }
        
        post.setEntity(builder.build());
        
        HttpResponse response = httpClient.execute(post, httpContext);
        
        
        
        System.out.println("statusCode: " + response.getStatusLine().getStatusCode());

        HttpEntity entity = response.getEntity();

        System.out.println("page: ");
        System.out.println(EntityUtils.toString(entity));
        
        return true;
    }
    
    public boolean authorize(String username, String password) throws UnsupportedEncodingException, IOException {

        HttpPost post = new HttpPost(FacebookLightConstants.AUTHORIZATION_URL);
        List<NameValuePair> nvps = new ArrayList<>();

        nvps.add(new BasicNameValuePair("lsd", "AVpVMx4W"));
        nvps.add(new BasicNameValuePair("version", "1"));
        nvps.add(new BasicNameValuePair("pxr", "0"));
        nvps.add(new BasicNameValuePair("qps", "0"));
        nvps.add(new BasicNameValuePair("dimensions", "0"));
        nvps.add(new BasicNameValuePair("m_ts", "1442986312"));
        nvps.add(new BasicNameValuePair("li", "SDkCVnQ2xrSY1F6ppsNPevvA"));
        nvps.add(new BasicNameValuePair("email", username));
        nvps.add(new BasicNameValuePair("pass", password));
        nvps.add(new BasicNameValuePair("login", "&#x412;&#x445;&#x43e;&#x434;"));

        post.setEntity(new UrlEncodedFormEntity(nvps));

        HttpResponse response = httpClient.execute(post, httpContext);

        System.out.println("statusLine: " + response.getStatusLine().getStatusCode());

        HttpEntity entity = response.getEntity();

        System.out.println("page: ");
        System.out.println(EntityUtils.toString(entity));

        return false;
    }

    public boolean isAuthorized() {

        List<Cookie> cookies = cookieStore.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.isExpired(new Date())) {
                return false;
            }
        }
        return !cookies.isEmpty();
    }

    public boolean isAuthorized(String responseContent) {

        String detector = "name=\"email\" value=\"";

        return !responseContent.contains(detector);
    }

    /**
     *
     * @param username
     * @param password
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public String getPageContent(String username, String password, String url) throws IOException {

        System.out.println("Doing request to: {");
        System.out.println(url);
        System.out.println("}");

        HttpGet get = new HttpGet(url);

        HttpResponse response = httpClient.execute(get, httpContext);

        String content = EntityUtils.toString(response.getEntity());

        if (!isAuthorized(content)) {
            authorize(username, password);
            get = new HttpGet(url);
            response = httpClient.execute(get, httpContext);
            content = EntityUtils.toString(response.getEntity());
        }

        System.out.println(content);

        return content;
    }

}
