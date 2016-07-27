/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookclient.client.impl;

import java.util.ArrayList;
import java.util.List;
import kz.alem.semantics.facebook.service.facebookclient.client.FacebookClient;
import kz.alem.semantics.facebook.service.facebookclient.constants.RequestUtil;
import kz.alem.semantics.facebook.service.facebookclient.parser.FacebookLightParser;
import kz.alem.semantics.facebook.service.facebookclient.util.URLCreator;
import kz.alem.semantics.parse.model.PageData;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Implementation of the FacebookClient based on mobile version of facebook.com, therefore it is more faster than usual implementation.
 * @author Zhasan
 */
@Component
@Scope("singleton")
public class FacebookLightClientImpl implements FacebookClient {

    private static final Logger LOG = Logger.getLogger(FacebookLightClientImpl.class);

    private final String RED = (char) 27 + "[31m ", WHITE = (char) 27 + "[37m ";

    private String facebookServiceURL;

    FacebookLightParser parser;

    public void setParser(FacebookLightParser parser) {
        this.parser = parser;
    }

    public FacebookLightClientImpl(String facebookServiceURL) {
        this.facebookServiceURL = facebookServiceURL;
        parser = new FacebookLightParser();
    }

    @Override
    public List<String> getUsersFeedUrls(String botUsername, String botPassword) {
        List<String> result = new ArrayList<>();

        String baseUrl = facebookServiceURL + "/users/feed";

        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);

        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            result = parser.parsePostUrls(Jsoup.parse(content));
        } catch (Exception ex) {

        }

        return result;
    }

    @Override
    public List<String> getProfileFeedUrls(String botUsername, String botPassword, String username) {
        List<String> result = new ArrayList<>();

        String baseUrl = facebookServiceURL + "/friends/feed";

        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);
        urlCreator.addParameter("username", username);
        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            result = parser.parsePostUrls(Jsoup.parse(content));
        } catch (Exception ex) {

        }

        return result;
    }

    @Override
    public PageData getPost(String botUsername, String botPassword, String url) {

        PageData result = new PageData(url);

        String baseUrl = facebookServiceURL + "/posts/post";

        String address = baseUrl;

        LOG.info("url = " + address);

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("bot_username", botUsername));
        nvps.add(new BasicNameValuePair("bot_password", botPassword));
        nvps.add(new BasicNameValuePair("url", url));

        try {
            String content = RequestUtil.postContent(address, nvps);
            result.setContent(content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    @Override
    public List<String> getFriendsList(String botUsername, String botPassword) {
        List<String> result = new ArrayList<>();

        String baseUrl = facebookServiceURL + "/friends/list";
        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);

        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            result = parser.parseFriendList(Jsoup.parse(content));
        } catch (Exception ex) {

        }

        return result;
    }

    @Override
    public List<String> getSubscribesList(String botUsername, String botPassword) {
        List<String> result = new ArrayList<>();

        String baseUrl = facebookServiceURL + "/subscribes/list";
        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);

        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            result = parser.parseSubscribesList(Jsoup.parse(content));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    @Override
    public List<String> getGroupsList(String botUsername, String botPassword) {
        List<String> result = new ArrayList<>();

        String baseUrl = facebookServiceURL + "/groups/list";
        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);

        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            result = parser.parseGroupsList(Jsoup.parse(content));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    @Override
    public List<String> getGroupsFeedUrls(String botUsername, String botPassword, String username) {
        List<String> result = new ArrayList<>();

        String baseUrl = facebookServiceURL + "/groups/feed";

        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);
        urlCreator.addParameter("username", username);
        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            result = parser.parsePostUrls(Jsoup.parse(content));
        } catch (Exception ex) {

        }

        return result;
    }

    @Override
    public long getUsersSubscribersCount(String botUsername, String botPassword, String username) {
        long result = 0;

        String baseUrl = facebookServiceURL + "/subscribes/count";

        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);
        urlCreator.addParameter("username", username);
        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            result = parser.parseUsersSubscribersCount(content);
        } catch (Exception ex) {

        }

        return result;
    }

    @Override
    public List<String> getProfileFriendsList(String botUsername, String botPassword, String username) {
        List<String> result = new ArrayList<>();

        String baseUrl = facebookServiceURL + "/friends/ofuser";
        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);
        urlCreator.addParameter("username", username);

        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            result = parser.parseProfileFriendList(Jsoup.parse(content));
        } catch (Exception ex) {

        }

        return result;
    }

    @Override
    public boolean subscribe(String botUsername, String botPassword, String username) {
        String baseUrl = facebookServiceURL + "/command/subscribe";
        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);
        urlCreator.addParameter("username", username);

        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            boolean result = Boolean.parseBoolean(content);
            LOG.info(RED + " Received subscribe response " + content + " from the service!");
            return result;
        } catch (Exception ex) {

        }

        return false;
    }

    @Override
    public boolean makeFriend(String botUsername, String botPassword, String username) {
        String baseUrl = facebookServiceURL + "/command/makefriend";
        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);
        urlCreator.addParameter("username", username);

        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            boolean result = Boolean.parseBoolean(content);
            LOG.info(RED + " Received make friend response " + content + " from the service!");
            return result;
        } catch (Exception ex) {

        }

        return false;
    }

    @Override
    public boolean like(String botUsername, String botPassword, String url) {
        String baseUrl = facebookServiceURL + "/command/like";
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("bot_username", botUsername));
        nvps.add(new BasicNameValuePair("bot_password", botPassword));
        nvps.add(new BasicNameValuePair("url", url));

        String address = baseUrl;

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.postContent(address, nvps);
            boolean result = Boolean.parseBoolean(content);
            LOG.info(RED + " Received liek post response " + content + " from the service!");
            return result;
        } catch (Exception ex) {

        }

        return false;

    }

    @Override
    public List<String> getUsersFeedLikeUrls(String botUsername, String botPassword) {

        List<String> result = new ArrayList<>();

        String baseUrl = facebookServiceURL + "/users/feed";

        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);

        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            result = parser.parseLikeUrls(Jsoup.parse(content));
        } catch (Exception ex) {

        }

        return result;

    }

    @Override
    public List<String> getProfileFeedLikeUrls(String botUsername, String botPassword, String username) {
        List<String> result = new ArrayList<>();

        String baseUrl = facebookServiceURL + "/friends/feed";

        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);
        urlCreator.addParameter("username", username);
        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            result = parser.parseLikeUrls(Jsoup.parse(content));
        } catch (Exception ex) {

        }

        return result;
    }

    @Override
    public boolean share(String botUsername, String botPassword, String url) {

        String baseUrl = facebookServiceURL + "/command/share";
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("bot_username", botUsername));
        nvps.add(new BasicNameValuePair("bot_password", botPassword));
        nvps.add(new BasicNameValuePair("url", url));

        String address = baseUrl;

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.postContent(address, nvps);
            boolean result = Boolean.parseBoolean(content);
            LOG.info(RED + " Received share post response " + content + " from the service!");
            return result;
        } catch (Exception ex) {

        }

        return false;
    }

    @Override
    public List<String> getProfileShareUrls(String botUsername, String botPassword, String username) {
        List<String> result = new ArrayList<>();

        String baseUrl = facebookServiceURL + "/friends/feed";

        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);
        urlCreator.addParameter("username", username);
        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            result = parser.parseShareUrls(Jsoup.parse(content));
        } catch (Exception ex) {
            LOG.error(ex);
        }

        return result;
    }

    @Override
    public String getLivingCountry(String botUsername, String botPassword, String username) {
        String result = null;

        String baseUrl = facebookServiceURL + "/info";
        URLCreator urlCreator = new URLCreator(baseUrl);
        urlCreator.addParameter("bot_username", botUsername);
        urlCreator.addParameter("bot_password", botPassword);
        urlCreator.addParameter("username", username);
        String address = urlCreator.getUrl();

        LOG.info("url = " + address);

        try {
            String content = RequestUtil.getContent(address);
            result = parser.parseCountry(Jsoup.parse(content));
        } catch (Exception ex) {
            LOG.error(ex);
        }

        return result;
    }

}
