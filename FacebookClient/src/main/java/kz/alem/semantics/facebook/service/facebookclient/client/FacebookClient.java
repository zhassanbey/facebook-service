/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookclient.client;

import java.io.Serializable;
import java.util.List;
import kz.alem.semantics.parse.model.PageData;
import org.apache.http.NameValuePair;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Interface used to interact with the FacebookService.
 *
 * @author Zhasan
 */
@Component
@Scope("singleton")
public interface FacebookClient extends Serializable {

    /**
     * Gets list of urls in news feed of the current bot
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @return list of urls as list of strings
     */
    public List<String> getUsersFeedUrls(String botUsername, String botPassword);

    /**
     * Gets list of urls in timeline news of an account
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username or facebook id
     * @return list of urls as list of strings
     */
    public List<String> getProfileFeedUrls(String botUsername, String botPassword, String username);

    /**
     * Gets facebook post/
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param url - url of the targeted post without host
     * @return post as PageData object
     */
    public PageData getPost(String botUsername, String botPassword, String url);

    /**
     * Gets list of friends of the current bot.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @return list of friends username as list of strings
     */
    public List<String> getFriendsList(String botUsername, String botPassword);

    /**
     * Gets list of friends of an account.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username or facebook id
     * @return list of friends username as list of strings
     */
    public List<String> getProfileFriendsList(String botUsername, String botPassword, String username);

    /**
     * Gets list of accounts subscribed by the current bot.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @return list of usernames of accounts as list of strsings
     */
    public List<String> getSubscribesList(String botUsername, String botPassword);
    /**
     * Gets list of groups which contain the current bot.
     * @param botUsernames - username of the current bot
     * @param botPassword - password of the current bot
     * @return list of group identifier as a list of strings
     */
    public List<String> getGroupsList(String botUsername, String botPassword);
    /**
     * Gets list of post urls from the groups page
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the facebook group, either group id or group username
     * @return 
     */
    public List<String> getGroupsFeedUrls(String botUsername, String botPassword, String username);
    /**
     * Returns the number of subscribers of the account
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the facebook account, either account id or username
     * @return 
     */
    public long getUsersSubscribersCount(String botUsername, String botPassword, String username);
    /**
     * Subscribes current bot account to the given account
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the account, either facebook id or username
     * @return 
     */
    public boolean subscribe(String botUsername, String botPassword, String username);
    /**
     * Sends friend request from the current bot account to the give account
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the account, either facebook id or username
     * @return 
     */
    public boolean makeFriend(String botUsername, String botPassword, String username);
    /**
     * Makes the current bot account like the post given by url link
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param url - the like button link url of the post to be liked
     * @return 
     */
    public boolean like(String botUsername, String botPassword, String url);
    /**
     * Returns list of like links from the bots main page
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @return 
     */
    public List<String> getUsersFeedLikeUrls(String botUsername, String botPassword);
    /**
     * Return list of like links from the accounts timeline page
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the current account, either facebook id or username
     * @return 
     */
    public List<String> getProfileFeedLikeUrls(String botUsername, String botPassword, String username);
    /**
     * Makes the current bot account share the post given by share link
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param url - the share linke url of the post to be shared
     * @return 
     */
    public boolean share(String botUsername, String botPassword, String url);
    /**
     * Returns the list of share link urls from the account timeline page
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the account, either facebook id or username
     * @return 
     */
    public List<String> getProfileShareUrls(String botUsername, String botPassword, String username);
    /**
     * Returns the geo-location information about the country, either hometown or current city
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the account , either facebook id or username
     * @return 
     */
    public String getLivingCountry(String botUsername, String botPassword, String username);

}
