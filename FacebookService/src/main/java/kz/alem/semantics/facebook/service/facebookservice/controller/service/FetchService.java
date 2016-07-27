/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookservice.controller.service;

import java.util.List;

/**
 * Interface for fetching date from facebook.
 *
 * @author Zhasan
 */
public abstract interface FetchService {

    /**
     * Fetches urls of posts in the timeline of an account at current moment of
     * time.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username or facebook id
     * @return list of urls as a list of strings
     * @throws Exception
     */
    public abstract List<String> getProfileFeedUrls(String botUsername, String botPassword, String username) throws Exception;

    /**
     * Fetches urls of posts from an accounts timleline at current moment of
     * time.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @return the page's html content as string
     * @throws Exception
     */
    public abstract String getUsersFeedUrls(String botUsername, String botPassword) throws Exception;

    /**
     * Fetches whatever page content.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param url - url of the targeted page
     * @return The pages html content as string
     * @throws Exception
     */
    public abstract String getPageContent(String botUsername, String botPassword, String url) throws Exception;

    /**
     * Fetches the friends list page of an account. Fetches just the first page
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted user. Either username
     * or facebook id
     * @return The pages html content as string
     * @throws Exception
     */
    public abstract String getUserProfile(String botUsername, String botPassword, String username) throws Exception;

    /**
     * Fetches list of all friends of the current bot
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @return The page's html content as string
     * @throws Exception
     */
    public abstract String getFriendsList(String botUsername, String botPassword) throws Exception;

    /**
     * Fetches list of all friends of an account.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username or facebook id
     * @return The page's html content as string
     * @throws Exception
     */
    public abstract String getProfileFriendsList(String botUsername, String botPassword, String username) throws Exception;

    /**
     * Fetches list of all accounts which the current bot has subscribed.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @return The pages html content as string
     * @throws Exception
     */
    public abstract String getSubscribesList(String botUsername, String botPassword) throws Exception;

    /**
     * Fetches list of all accounts which an account has subscribed.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username or facebook id
     * @return The page's html content as string
     * @throws Exception
     */
    public abstract String getUsersSubscribesList(String botUsername, String botPassword, String username) throws Exception;

    /**
     * Fetches number of accounts which an account has subscribed.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username of facebook id
     * @return The page's html content as string
     * @throws Exception
     */
    public abstract String getUsersSubscribersCount(String botUsername, String botPassword, String username) throws Exception;

    /**
     * Fetches list of all groups the current bot has joined in.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @return The page's html content as string
     * @throws Exception
     */
    public abstract String getGroupList(String botUsername, String botPassword) throws Exception;

    /**
     * Fetches urls of posts in a group's page.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - passsword of the current bot
     * @param username - unique identifier of the targeted user. Either username
     * of facebook id
     * @return The page's html content as string
     * @throws Exception
     */
    public abstract String getGroupsFeedUrls(String botUsername, String botPassword, String username) throws Exception;

    /**
     * Performs subscribe account action command.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username or facebook id
     * @return True if the operation is successful. False otherwise
     * @throws Exception
     */
    public abstract boolean subscribePerson(String botUsername, String botPassword, String username) throws Exception;

    /**
     * Sends make friend request to an account.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username or facebook id
     * @return True if the operation is successful. False otherwise
     * @throws Exception
     */
    public abstract boolean makeFriend(String botUsername, String botPassword, String username) throws Exception;

    /**
     * Likes a post.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param url - url of the targeted page
     * @return True if the operation is successful. False otherwise
     * @throws Exception
     */
    public abstract boolean like(String botUsername, String botPassword, String url) throws Exception;

    /**
     * Fetches the html page with form, required to perform share post
     * operation.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param url - url of the targeted page
     * @return The page's html content as string
     * @throws Exception
     */
    public abstract String getShareForm(String botUsername, String botPassword, String url) throws Exception;

    /**
     * Fetches information page about an account.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username of facebook id
     * @return The page's html content as string
     * @throws Exception
     */
    public abstract String getUsersInfo(String botUsername, String botPassword, String username) throws Exception;

}
