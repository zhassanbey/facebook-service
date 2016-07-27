/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookservice.controller.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kz.alem.semantics.facebook.service.facebookservice.controller.constants.FacebookLightConstants;
import kz.alem.semantics.facebook.service.facebookservice.controller.service.FetchService;
import kz.alem.semantics.facebook.service.facebookservice.controller.util.LightPageUtils;
import kz.alem.semantics.facebook.service.facebookservice.controller.web.client.wrapper.FbWrapperManagerLight;
import kz.alem.semantics.facebook.service.facebookservice.controller.web.client.wrapper.WebClientWrapperLight;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * Implementation of the FetchService interface using simple httpClient.
 * Therefore called light implementation in compare to the older one.
 *
 * @author Zhasan
 */
public class FetchServiceImplLight implements FetchService {

    @Override
    public List<String> getProfileFeedUrls(String botUsername, String botPassword, String username) throws Exception {
        List<String> result = new ArrayList<>();

        FbWrapperManagerLight wrapperManager = FbWrapperManagerLight.getInstance();

        WebClientWrapperLight wrapper = wrapperManager.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapperLight();
            wrapper.authorize(botUsername, botPassword);
            wrapperManager.put(botUsername, wrapper);
        }

        boolean isScreenName = LightPageUtils.isScreenName(username);

        String pager = null;
        String iContent = null;
        String pagerId = null;

        if (isScreenName) {
            pager = username + "?v=timeline";
            pagerId = String.format(FacebookLightConstants.TIMELINE_PAGER_ID, username);
        } else {
            pager = "profile.php?v=timeline&id=" + username;
            pagerId = String.format(FacebookLightConstants.TIMELINE_PAGER_ID, "profile.php");
        }

        for (int i = 0; i < FacebookLightConstants.TIMELINE_DEPTH; i++) {
            String content = wrapper.getPageContent(botUsername, botPassword, String.format(FacebookLightConstants.PROFILE_FEED_TEMPLATE, pager)).trim();
            iContent = iContent + content;

            if (content.contains(pagerId)) {
                int startIndex = content.indexOf(pagerId) + 1;

                pager = StringEscapeUtils.unescapeXml(content.substring(startIndex, content.indexOf("\"", startIndex)));
            } else {
                break;
            }
        }

        result.add(iContent);
        return result;
    }

    @Override
    public String getUsersFeedUrls(String botUsername, String botPassword) throws Exception {
        FbWrapperManagerLight wrapperManager = FbWrapperManagerLight.getInstance();

        WebClientWrapperLight wrapper = wrapperManager.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapperLight();
            wrapper.authorize(botUsername, botPassword);
            wrapperManager.put(botUsername, wrapper);
        }

        String iContent = "";
        String pager = "null";
        for (int i = 0; i < FacebookLightConstants.STORIES_DEPTH; i++) {

            String content = wrapper.getPageContent(botUsername, botPassword, String.format(FacebookLightConstants.FEED_TEMPLATE, pager)).trim();
            iContent = iContent + content;

            if (content.contains(FacebookLightConstants.STORIES_PAGER_ID)) {
                int startIndex = content.indexOf(FacebookLightConstants.STORIES_PAGER_ID) + FacebookLightConstants.STORIES_PAGER_ID.length();

                pager = StringEscapeUtils.unescapeXml(content.substring(startIndex, content.indexOf("\"", startIndex)));
            } else {
                break;
            }
        }

        return iContent;
    }

    @Override
    public String getPageContent(String botUsername, String botPassword, String url) throws Exception {
        FbWrapperManagerLight wrapperManager = FbWrapperManagerLight.getInstance();

        WebClientWrapperLight wrapper = wrapperManager.get(botUsername);

        if (wrapper == null) {
            System.out.println("wrapepr is null. Creating new wrapper for " + botUsername);
            wrapper = new WebClientWrapperLight();
            wrapper.authorize(botUsername, botPassword);
            wrapperManager.put(botUsername, wrapper);
        }

        String iContent = wrapper.getPageContent(botUsername, botPassword, url);

        return iContent;
    }

    @Override
    public String getFriendsList(String botUsername, String botPassword) throws Exception {
        FbWrapperManagerLight wrapperManager = FbWrapperManagerLight.getInstance();

        WebClientWrapperLight wrapper = wrapperManager.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapperLight();
            wrapper.authorize(botUsername, botPassword);
            wrapperManager.put(botUsername, wrapper);
        }

        String iContent = "";
        String pager = "=1&startindex=0";
        String pagerId = String.format(FacebookLightConstants.FRIEND_LIST_PAGER_ID, botUsername);

        String content = "";

        do {
            content = wrapper.getPageContent(botUsername, botPassword, String.format(FacebookLightConstants.FRIEND_LIST_TEMPLATE, botUsername, pager)).trim();
            iContent = iContent + content;
            int startIndex = content.indexOf(pagerId) + pagerId.length();

//            String x = content;
//            int numFr = 0;
//            String token = "class=\"v s\"";
//            while(x.contains(token)){
//                x = x.substring(x.indexOf(token)+token.length());
//                numFr++;
//            }(Integer.parseInt(pager)+numFr+1)+"" ;
            try {
                pager = StringEscapeUtils.unescapeXml(content.substring(startIndex, content.indexOf("\"", startIndex)));
            } catch (Exception ex) {
                break;
            }
        } while (content.contains(pagerId));

        return iContent;
    }

    @Override
    public String getSubscribesList(String botUsername, String botPassword) throws Exception {
        FbWrapperManagerLight wrapperManager = FbWrapperManagerLight.getInstance();

        WebClientWrapperLight wrapper = wrapperManager.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapperLight();
            wrapper.authorize(botUsername, botPassword);
            wrapperManager.put(botUsername, wrapper);
        }

        String iContent = "";
        String pager = "0";
        String pagerId = FacebookLightConstants.SUBSCRIPTIONS_PAGER_ID;
        String content = "";

        do {
            content = wrapper.getPageContent(botUsername, botPassword, String.format(FacebookLightConstants.USER_SUBSCRIPTIONS_TEMPLATE, pager)).trim();
            iContent = iContent + content;
            int startIndex = content.indexOf(pagerId) + pagerId.length();

//            String x = content;
//            int numFr = 0;
//            String token = "class=\"v s\"";
//            while(x.contains(token)){
//                x = x.substring(x.indexOf(token)+token.length());
//                numFr++;
//            }(Integer.parseInt(pager)+numFr+1)+"" ;
            try {
                pager = StringEscapeUtils.unescapeXml(content.substring(startIndex, content.indexOf("\"", startIndex)));

                if (pager.contains("&t=in")) {
                    String pPre = pager.substring(0, pager.indexOf("&t"));

                    pager = pPre + "&t=out";
                }
            } catch (Exception ex) {
                break;
            }
        } while (content.contains(pagerId));

        return iContent;
    }

    @Override
    public String getGroupList(String botUsername, String botPassword) throws Exception {
        FbWrapperManagerLight wrapperManager = FbWrapperManagerLight.getInstance();

        WebClientWrapperLight wrapper = wrapperManager.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapperLight();
            wrapper.authorize(botUsername, botPassword);
            wrapperManager.put(botUsername, wrapper);
        }

        String iContent = "";
        String pager = "";
        String pagerId = FacebookLightConstants.GROUPS_PAGER_ID;
        String content = "";

        do {
            content = wrapper.getPageContent(botUsername, botPassword, String.format(FacebookLightConstants.GROUPS_TEMPLATE, pager)).trim();
            iContent = iContent + content;
            int startIndex = content.indexOf(pagerId) + pagerId.length();

//            String x = content;
//            int numFr = 0;
//            String token = "class=\"v s\"";
//            while(x.contains(token)){
//                x = x.substring(x.indexOf(token)+token.length());
//                numFr++;
//            }(Integer.parseInt(pager)+numFr+1)+"" ;
            try {
                pager = StringEscapeUtils.unescapeXml(content.substring(startIndex, content.indexOf("\"", startIndex)));

            } catch (Exception ex) {
                break;
            }
        } while (content.contains(pagerId));

        return iContent;
    }

    @Override
    public String getGroupsFeedUrls(String botUsername, String botPassword, String username) throws Exception {
        FbWrapperManagerLight wrapperManager = FbWrapperManagerLight.getInstance();

        WebClientWrapperLight wrapper = wrapperManager.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapperLight();
            wrapper.authorize(botUsername, botPassword);
            wrapperManager.put(botUsername, wrapper);
        }

        String iContent = "";
        String pager = "null";
        String pagerId = String.format(FacebookLightConstants.GROUPS_FEED_PAGER_ID, username);

        for (int i = 0; i < FacebookLightConstants.STORIES_DEPTH; i++) {

            String content = wrapper.getPageContent(botUsername, botPassword, String.format(FacebookLightConstants.GROUPS_FEED_TEMPLATE, username, pager)).trim();
            iContent = iContent + content;

            if (content.contains(pagerId)) {
                int startIndex = content.indexOf(pagerId) + pagerId.length();

                pager = StringEscapeUtils.unescapeXml(content.substring(startIndex, content.indexOf("\"", startIndex)));
            } else {
                break;
            }
        }

        return iContent;
    }

    @Override
    public String getUserProfile(String botUsername, String botPassword, String username) throws Exception {

        FbWrapperManagerLight wrapperManager = FbWrapperManagerLight.getInstance();

        WebClientWrapperLight wrapper = wrapperManager.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapperLight();
            wrapper.authorize(botUsername, botPassword);
            wrapperManager.put(botUsername, wrapper);
        }

        boolean isScreenName = LightPageUtils.isScreenName(username);

        String url = "https://m.facebook.com/";

        if (isScreenName) {
            url = url + username + "/friends";
        } else {
            url = url + "profile.php?v=friends&id=" + username;
        }

        String iContent = getPageContent(botUsername, botPassword, url);

        return iContent;
    }

    @Override
    public String getUsersSubscribesList(String botUsername, String botPassword, String username) throws Exception {
        FbWrapperManagerLight wrapperManager = FbWrapperManagerLight.getInstance();

        WebClientWrapperLight wrapper = wrapperManager.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapperLight();
            wrapper.authorize(botUsername, botPassword);
            wrapperManager.put(botUsername, wrapper);
        }

        boolean isScreenName = LightPageUtils.isScreenName(username);

        String iContent = "";
        String pager = "0";
        String pagerId = FacebookLightConstants.SUBSCRIPTIONS_PAGER_ID;
        String content = "";

        String query = "https://m.facebook.com/" + username + "?v=followers";
        if (!isScreenName) {
            query = "https://m.facebook.com/profile.php?v=followers&id=" + username;
        }

        do {
            content = wrapper.getPageContent(botUsername, botPassword, query).trim();
            iContent = iContent + content;
            int startIndex = content.indexOf(pagerId) + pagerId.length();

//            String x = content;
//            int numFr = 0;
//            String token = "class=\"v s\"";
//            while(x.contains(token)){
//                x = x.substring(x.indexOf(token)+token.length());
//                numFr++;
//            }(Integer.parseInt(pager)+numFr+1)+"" ;
            try {
                pager = StringEscapeUtils.unescapeXml(content.substring(startIndex, content.indexOf("\"", startIndex)));

                if (pager.contains("&t=out")) {
                    String pPre = pager.substring(0, pager.indexOf("&t"));

                    pager = pPre + "&t=in";
                }
            } catch (Exception ex) {
                break;
            }

            query = String.format(FacebookLightConstants.USER_SUBSCRIPTIONS_TEMPLATE, pager);
        } while (content.contains(pagerId));

        return iContent;
    }

    @Override
    public String getUsersSubscribersCount(String botUsername, String botPassword, String username) throws Exception {
        FbWrapperManagerLight wrapperManager = FbWrapperManagerLight.getInstance();

        WebClientWrapperLight wrapper = wrapperManager.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapperLight();
            wrapper.authorize(botUsername, botPassword);
            wrapperManager.put(botUsername, wrapper);
        }

        boolean isScreenName = LightPageUtils.isScreenName(username);

        String query = "https://www.facebook.com/" + username + "/followers";

        if (!isScreenName) {
            query = "https://www.facebook.com/profile.php?id=" + username + "&v=followers";
        }

        String content = wrapper.getPageContent(username, botPassword, query);

        return content;
    }

    @Override
    public String getProfileFriendsList(String botUsername, String botPassword, String username) throws Exception {
        FbWrapperManagerLight wrapperManager = FbWrapperManagerLight.getInstance();

        WebClientWrapperLight wrapper = wrapperManager.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapperLight();
            wrapper.authorize(botUsername, botPassword);
            wrapperManager.put(botUsername, wrapper);
        }

        boolean isScreenName = LightPageUtils.isScreenName(username);

        String iContent = "";
        String pager = "=1&startindex=0";
        String pagerId = String.format(FacebookLightConstants.FRIEND_LIST_PAGER_ID, username);

        String queryTemplate = FacebookLightConstants.FRIEND_LIST_TEMPLATE;

        if (!isScreenName) {
            pager = "";
            pagerId = String.format(FacebookLightConstants.FRIEND_LIST_PAGER_FOR_ID, username);
            queryTemplate = FacebookLightConstants.FRIEND_LIST_TEMPLATE_FOR_ID;
        }

        String content = "";
        int i = 0;
        do {
            content = StringEscapeUtils.unescapeXml(wrapper.getPageContent(botUsername, botPassword, String.format(queryTemplate, username, pager)).trim());
            iContent = iContent + content;
            int startIndex = content.indexOf(pagerId) + pagerId.length();

//            String x = content;
//            int numFr = 0;
//            String token = "class=\"v s\"";
//            while(x.contains(token)){
//                x = x.substring(x.indexOf(token)+token.length());
//                numFr++;
//            }(Integer.parseInt(pager)+numFr+1)+"" ;
            try {
                pager = StringEscapeUtils.unescapeXml(content.substring(startIndex, content.indexOf("\"", startIndex)));
            } catch (Exception ex) {
                break;
            }
            System.out.println("i="+i+" pagerId="+pagerId);
        } while (content.contains(pagerId));

        return iContent;
    }

    @Override
    public boolean subscribePerson(String botUsername, String botPassword, String username) throws Exception {

//        FbWrapperManagerLight wrapperManager = FbWrapperManagerLight.getInstance();
//
//        WebClientWrapperLight wrapper = wrapperManager.get(botUsername);
//
//        if (wrapper == null) {
//            wrapper = new WebClientWrapperLight();
//            wrapper.authorize(botUsername, botPassword);
//            wrapperManager.put(botUsername, wrapper);
//        }
        boolean isScreenName = LightPageUtils.isScreenName(username);

        String baseUrl = "https://m.facebook.com/";

        String url = baseUrl;
        if (isScreenName) {
            url = url + username;
        } else {
            url = url + "profile.php?id=" + username;
        }

        String iContent = getPageContent(botUsername, botPassword, url);

        if (iContent.contains(FacebookLightConstants.SUBSCURIBE_URL_ID)) {
            int startIndex = iContent.indexOf(FacebookLightConstants.SUBSCURIBE_URL_ID) + 1;
            int endIndex = iContent.indexOf('\"', startIndex);

            String subscribeUrl = StringEscapeUtils.unescapeXml(iContent.substring(startIndex, endIndex));
            if (!getPageContent(botUsername, botPassword, baseUrl + subscribeUrl).contains(FacebookLightConstants.SUBSCURIBE_URL_ID)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean makeFriend(String botUsername, String botPassword, String username) throws Exception {

//        FbWrapperManagerLight wrapperManager = FbWrapperManagerLight.getInstance();
//
//        WebClientWrapperLight wrapper = wrapperManager.get(botUsername);
//
//        if (wrapper == null) {
//            wrapper = new WebClientWrapperLight();
//            wrapper.authorize(botUsername, botPassword);
//            wrapperManager.put(botUsername, wrapper);
//        }
        boolean isScreenName = LightPageUtils.isScreenName(username);

        String baseUrl = "https://m.facebook.com/";

        String url = baseUrl;
        if (isScreenName) {
            url = url + username;
        } else {
            url = url + "profile.php?id=" + username;
        }

        String iContent = getPageContent(botUsername, botPassword, url);

        if (iContent.contains(FacebookLightConstants.MAKE_FRIEND_URL_ID)) {
            int startIndex = iContent.indexOf(FacebookLightConstants.MAKE_FRIEND_URL_ID) + 1;
            int endIndex = iContent.indexOf('\"', startIndex);

            String makeFriendUrl = StringEscapeUtils.unescapeXml(iContent.substring(startIndex, endIndex));

            if (!getPageContent(botUsername, botPassword, baseUrl + makeFriendUrl).contains(FacebookLightConstants.MAKE_FRIEND_URL_ID)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean like(String botUsername, String botPassword, String url) throws Exception {

        String baseUrl = "https://m.facebook.com/";

        url = baseUrl + StringEscapeUtils.unescapeXml(url);

        String iContent = getPageContent(botUsername, botPassword, url);

        String link = LightPageUtils.getStringBtw(url, FacebookLightConstants.LIKE_ID, FacebookLightConstants.LIKE_ID.length(), "\"", 0);

        System.out.println("searchin for \"" + link + "\"");

        if (iContent.contains(link)) {
            return true;
        }

        return false;
    }

    @Override
    public String getShareForm(String botUsername, String botPassword, String url) throws Exception {
        String baseUrl = "https://m.facebook.com/";

        url = baseUrl + StringEscapeUtils.unescapeXml(url);

        String iContent = getPageContent(botUsername, botPassword, url);

        Map<String, String> map = LightPageUtils.mapForm(iContent);

        if (FbWrapperManagerLight.getInstance().get(botUsername).sendForm(map)) {
            return "OK";
        }

        return iContent;
    }

    @Override
    public String getUsersInfo(String botUsername, String botPassword, String username) throws Exception {
        String baseUrl = "https://m.facebook.com/";
        boolean isScreenName = LightPageUtils.isScreenName(username);

        String url = baseUrl + "profile.php?id=" + username + "&v=info";

        if (isScreenName) {
            url = baseUrl + username + "/about";
        }

        String iContent = getPageContent(botUsername, botPassword, url);

        return iContent;
    }

}
