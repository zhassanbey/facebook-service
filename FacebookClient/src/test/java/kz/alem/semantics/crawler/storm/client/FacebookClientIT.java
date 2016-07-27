/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.crawler.storm.client;

import java.util.List;
import kz.alem.semantics.facebook.service.facebookclient.client.FacebookClient;
import kz.alem.semantics.facebook.service.facebookclient.client.impl.FacebookClientImpl;
import kz.alem.semantics.facebook.service.facebookclient.constants.ConfigurationConstants;
import kz.alem.semantics.parse.model.PageData;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Zhasan
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(ConfigurationConstants.APPLICATION_CONTEXT_TEST)
public class FacebookClientIT {

    @Ignore
    @Test
    public void getUsersFeedTest() {

        String botUsername = "zhassan.baipakbayev";
        String botPassword = "precious5pro";

        FacebookClient client = new FacebookClientImpl();

        List<String> feed = client.getUsersFeedUrls(botUsername, botPassword);

        assertNotNull(feed);

        for (String str : feed) {
            System.out.println(str);
        }
    }

    @Ignore
    @Test
    public void getProfileFeedTest() {

        String botUsername = "zhassan.baipakbayev";
        String botPassword = "precious5pro";
        String username = "tugba.akan.79";

        FacebookClient client = new FacebookClientImpl();

        List<String> feed = client.getProfileFeedUrls(botUsername, botPassword, username);

        assertNotNull(feed);

        for (String str : feed) {
            System.out.println(str);
        }
    }

    @Ignore
    @Test
    public void getPostTest() {
        String botUsername = "zhassan.baipakbayev";
        String botPassword = "precious5pro";
        String url = "https://www.facebook.com/1068065369/posts/10206292448764615";

        FacebookClient client = new FacebookClientImpl();

        PageData pageData = client.getPost(botUsername, botPassword, url);

        assertNotNull(pageData);
        //System.out.println(pageData.getContent());
        System.out.println(pageData.toString());

    }

    @Ignore
    @Test
    public void getFriendsListTest() {
        String botUsername = "zhassan.baipakbayev";
        String botPassword = "precious5pro";

        FacebookClient client = new FacebookClientImpl();

        List<String> friendList = client.getFriendsList(botUsername, botPassword);

        assertNotNull(friendList);

        for (String f : friendList) {
            System.out.println(f);
        }

    }

   
}
