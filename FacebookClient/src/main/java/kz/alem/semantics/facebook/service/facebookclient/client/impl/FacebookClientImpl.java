/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookclient.client.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import kz.alem.semantics.facebook.service.facebookclient.client.FacebookClient;
import kz.alem.semantics.facebook.service.facebookclient.constants.SocialNetworkUtil;
import kz.alem.semantics.facebook.service.facebookclient.parser.FacebookParser;
import kz.alem.semantics.facebook.service.facebookclient.parser.PostParser;
import kz.alem.semantics.facebook.service.facebookclient.util.URLCreator;
import kz.alem.semantics.parse.model.PageData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Implementation of the FacebookClient based on web version of facebook.com.
 * @author Zhasan
 */
@Component
@Scope("singleton")
public class FacebookClientImpl implements FacebookClient,Serializable {

    String RED = (char) 27 + "[31m ", WHITE = (char) 27 + "[37m ";
    
    private String facebookServiceURL;
    private int timoutSeconds = 3*60;

    public String getFacebookServiceURL() {
        return facebookServiceURL;
    }

    public void setFacebookServiceURL(String facebookServiceURL) {
        this.facebookServiceURL = facebookServiceURL;
    }

    private FacebookParser parser;

    public FacebookClientImpl(String facebookServiceURL){
        this.facebookServiceURL = facebookServiceURL;
        parser = new FacebookParser();
    }
    
    public FacebookClientImpl() {
        facebookServiceURL = "http://localhost:8092";//ApplicationScope.ctx.getBean("facebookServiceURL", String.class);
        parser = new FacebookParser();
    }

    @Override
    public List<String> getUsersFeedUrls(String botUsername, String botPassword) {
        String baseUrl = facebookServiceURL + "/users/feed";

        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);

        String address = urlCreator.getUrl();

        System.out.println("url = " + address);

        
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, timoutSeconds*1000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpGet get = new HttpGet(address);
        try {

            System.out.println(RED + " Starting to fetch data from service...");

            HttpResponse response = httpClient.execute(get);
            
           
            
            HttpEntity entity = response.getEntity();

            String content = EntityUtils.toString(entity);

            System.out.println(RED + " Fetched data from the service!");

            //here goes parsing of the content
            List<String> urlList = new ArrayList<>();

            System.out.println(WHITE + "Parsing documents...");
            Document doc = Jsoup.parse(content);

            Map<String, String> feedInfoMap = parser.getFeedInfoMap(doc);

            for (String postId : feedInfoMap.keySet()) {
                String userId = feedInfoMap.get(postId);
                String url = String.format(SocialNetworkUtil.FACEBOOK_POST_URL_TEMPLATE, userId, postId);
                urlList.add(url);
            }

            System.out.println(WHITE + "Parsed Documents !");
            return urlList;
        } catch (IOException ex) {
            Logger.getLogger(FacebookClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public List<String> getProfileFeedUrls(String botUsername, String botPassword, String username) {

        String baseUrl = facebookServiceURL + "/friends/feed";

        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);
        urlCreator.addParameter("username", username);

        String address = urlCreator.getUrl();

        System.out.println("url = " + address);

        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, timoutSeconds*1000);
        
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpGet get = new HttpGet(address);
        try {
            System.out.println(RED + " Starting to fetch data from service...");
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            String content = EntityUtils.toString(entity);

            System.out.println(RED + " Fetched data from the service!");
            List<String> contents = new ArrayList<>();

            JSONParser jParser = new JSONParser();
            System.out.println(WHITE + "Parsing documents...");
            try {
                JSONArray jArr = (JSONArray) jParser.parse(content);

                for (int i = 0; i < jArr.size(); i++) {
                    contents.add((String) jArr.get(i));
                }
            } catch (ParseException ex) {
                Logger.getLogger(FacebookClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            //here goes parsing of the content
            List<String> urlList = new ArrayList<>();

            for (int i = 0; i < contents.size(); i++) {

                Document doc = Jsoup.parse(contents.get(i));

                Map<String, String> feedInfoMap = parser.getFeedInfoMap(doc);

                for (String postId : feedInfoMap.keySet()) {
                    String userId = feedInfoMap.get(postId);
                    String url = String.format(SocialNetworkUtil.FACEBOOK_POST_URL_TEMPLATE, userId, postId);
                    urlList.add(url);
                }
            }
            System.out.println(WHITE + "Parsed Documents !");
            return urlList;
        } catch (IOException ex) {
            Logger.getLogger(FacebookClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public PageData getPost(String botUsername, String botPassword, String url) {
        String baseUrl = facebookServiceURL + "/posts/post";

        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);
        urlCreator.addParameter("url", url);

        String address = urlCreator.getUrl();

        System.out.println("url = " + address);

         
        HttpParams httpParams = new BasicHttpParams();
        
        HttpConnectionParams.setConnectionTimeout(httpParams, timoutSeconds*1000);
        
        HttpClient httpClient = new DefaultHttpClient(httpParams);

    
        
        HttpGet get = new HttpGet(address);
        try {
            System.out.println(RED + " Starting to fetch data from service...");
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            String content = EntityUtils.toString(entity);
            System.out.println(RED + " Fetched data from the service!");

            PageData pageData = new PageData(url);
            pageData.setContent(content);
            System.out.println(WHITE + "Parsing documents...");

            pageData = PostParser.parsePost(parser, pageData);
            System.out.println(WHITE + "Parsed Documents !");
            return pageData;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public List<String> getFriendsList(String botUsername, String botPassword) {

        String baseUrl = facebookServiceURL + "/friends/list";

        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);

        String address = urlCreator.getUrl();

        System.out.println("url = " + address);

        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, timoutSeconds*1000);
        
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpGet get = new HttpGet(address);
        try {

            System.out.println(RED + " Starting to fetch data from service...");

            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            String content = EntityUtils.toString(entity);

            System.out.println(RED + " Fetched data from the service!");

            Document doc = Jsoup.parse(content);

            List<String> friends = parser.getFriendsList(doc);

            return friends;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getSubscribesList(String botUsername, String botPassword) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getGroupsList(String botUsername, String botPassword) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getGroupsFeedUrls(String botUsername, String botPassword, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getUsersSubscribersCount(String botUsername, String botPassword, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getProfileFriendsList(String botUsername, String botPassword, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean subscribe(String botUsername, String botPassword, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean makeFriend(String botUsername, String botPassword, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean like(String botUsername, String botPassword, String url) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getUsersFeedLikeUrls(String botUsername, String botPassword) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getProfileFeedLikeUrls(String botUsername, String botPassword, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean share(String botUsername, String botPassword, String url) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getProfileShareUrls(String botUsername, String botPassword, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLivingCountry(String botUsername, String botPassword, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
