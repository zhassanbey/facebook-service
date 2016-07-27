/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookservice.controller.service.impl;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import kz.alem.semantics.facebook.service.facebookservice.controller.constants.FacebookWebConstants;
import kz.alem.semantics.facebook.service.facebookservice.controller.service.FetchService;
import kz.alem.semantics.facebook.service.facebookservice.controller.web.client.wrapper.FbWrapperManager;
import kz.alem.semantics.facebook.service.facebookservice.controller.web.client.wrapper.WebClientWrapper;
import kz.alem.semantics.parse.model.PageData;

/**
 * Implementation of the FetchService interface using selenium client browsers.
 *
 * @author Zhasan
 */
public class FetchServiceImpl implements FetchService {

    @Override
    public List<String> getProfileFeedUrls(String botUsername, String botPassword, String username) throws Exception {
        FbWrapperManager wrapperMngr = FbWrapperManager.getInstance();

        WebClientWrapper wrapper = wrapperMngr.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapper();

            if (wrapper.authorize(botUsername, botPassword)) {
                wrapperMngr.put(botUsername, wrapper);
            } else {
                throw new Exception("[Autorization exception] Error while autorizing bot " + botUsername);
            }

        }

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        System.out.println("Time => " + calendar.getTime());

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        List<String> contents = new ArrayList<>();

        System.out.println("Fetching data from internet..");

        for (int i = 0; i < 2; i++) {
            HtmlPage htmlPage = null;

            try {
                // wrapper.getWebClient().getCurrentWindow().setInnerHeight(1000);
                String url = String.format(FacebookWebConstants.PROFILE_TEMPLATE, username) + FacebookWebConstants.FACEBOOK_TIMELINE_SUFFIX + year + "/" + month;
                System.out.println("url = > " + url);
                String content = wrapper.getPageContent(botUsername, botPassword, url);
                contents.add(content);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            month = Math.abs(1 - 12 * month / 12);
        }

        System.out.println("Data has been fetched from internet!");
        return contents;
    }

    @Override
    public String getPageContent(String botUsername, String botPassword, String url) throws Exception {
        PageData pageData = new PageData(url);

        FbWrapperManager wrapperMngr = FbWrapperManager.getInstance();

        WebClientWrapper wrapper = wrapperMngr.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapper();

            if (wrapper.authorize(botUsername, botPassword)) {
                wrapperMngr.put(botUsername, wrapper);
            } else {
                throw new Exception("[Autorization exception] Error while autorizing bot " + botUsername);
            }

        }

        System.out.println("Fetching data from internet..");
        String content = wrapper.getPageContent(botUsername, botPassword, url);
        System.out.println("Data has been fetched from internet!");

        return content;
    }

    @Override
    public String getUsersFeedUrls(String botUsername, String botPassword) throws Exception {
        FbWrapperManager wrapperMngr = FbWrapperManager.getInstance();

        WebClientWrapper wrapper = wrapperMngr.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapper();

            if (wrapper.authorize(botUsername, botPassword)) {
                wrapperMngr.put(botUsername, wrapper);
            } else {
                throw new Exception("[Autorization exception] Error while autorizing bot " + botUsername);
            }

        }

        HtmlPage htmlPage = null;

        System.out.println("Fetching data from internet..");
        String content = wrapper.getPageContent(botUsername, botPassword, FacebookWebConstants.FACEBOOK_FEED_ADDRESS);

        System.out.println("Data has been fetched from internet!");

        return content;

    }

    @Override
    public String getFriendsList(String botUsername, String botPassword) throws Exception {
        FbWrapperManager wrapperMngr = FbWrapperManager.getInstance();

        WebClientWrapper wrapper = wrapperMngr.get(botUsername);

        if (wrapper == null) {
            wrapper = new WebClientWrapper();

            if (wrapper.authorize(botUsername, botPassword)) {
                wrapperMngr.put(botUsername, wrapper);
            } else {
                throw new Exception("[Autorization exception] Error while autorizing bot " + botUsername);
            }

        }

        System.out.println("Fetching data from internet..");

        String content = wrapper.getPageContent(botUsername, botPassword, String.format(FacebookWebConstants.FACEBOOK_FRIENDS_URL_TEMPLATE, botUsername));

        System.out.println("Data has been fetched from internet!");

        return content;
    }

    @Override
    public String getSubscribesList(String botUsername, String botPassword) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getGroupList(String botUsername, String botPassword) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getGroupsFeedUrls(String botUsername, String botPassword, String username) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getUserProfile(String botUsername, String botPassword, String username) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getUsersSubscribesList(String botUsername, String botPassword, String username) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getUsersSubscribersCount(String botUsername, String botPassword, String username) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getProfileFriendsList(String botUsername, String botPassword, String username) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean subscribePerson(String botUsername, String botPassword, String username) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean makeFriend(String botUsername, String botPassword, String username) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean like(String botUsername, String botPassword, String url) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getShareForm(String botUsername, String botPassword, String url) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getUsersInfo(String botUsername, String botPassword, String username) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
