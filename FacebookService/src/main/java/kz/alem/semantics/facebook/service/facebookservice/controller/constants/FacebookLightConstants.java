/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookservice.controller.constants;

/**
 * Class stores constant strings, which are used to work with facebook.com in
 * the system
 *
 * @author Zhasan
 */
public class FacebookLightConstants {

    /**
     * Current bots news feed page depth, required to monitor at once.
     */
    public static final int STORIES_DEPTH = 3;
    /**
     * An accounts timeline depth, required to monitor at once.
     */
    public static final int TIMELINE_DEPTH = 2;
    /**
     * Common substring for like post action urls.
     */
    public static final String LIKE_ID = "/a/like.php?ul=1";
    /**
     * Common substring for share post action urls.
     */
    public static final String SHARE_ID = "/composer/mbasic/?c_src=share";
    /**
     * Common substring for subscribe user action urls.
     */
    public static final String SUBSCURIBE_URL_ID = "/a/subscribe.php?";
    /**
     * Common substring for make friend with user action urls.
     */
    public static final String MAKE_FRIEND_URL_ID = "/a/mobile/friends/profile_add_friend.php?";
    /**
     * Common substring for paging urls in the news feed page of the current
     * user.
     */
    public static final String STORIES_PAGER_ID = "/stories.php?aftercursorr=";
    /**
     * Common substring for paging urls in the timeline page of an account.
     */
    public static final String TIMELINE_PAGER_ID = "/%s?sectionLoadingID=";
    /**
     * Common substring for paging urls through friend list, of an account,
     * which has username.
     */
    public static final String FRIEND_LIST_PAGER_ID = "/%s/friends?startindex=";
    /**
     * Common substring for paging urls through friend list, of an account,
     * which has facbook id.
     */
    public static final String FRIEND_LIST_PAGER_FOR_ID = "/profile.php?v=friends&id=%s";
    /**
     * Common substring for paging urls through subscriptions. Works only with
     * facebook id.
     */
    public static final String SUBSCRIPTIONS_PAGER_ID = "/subscribe/lists/?id=";
    /**
     * Common substring for paging urls through groups.
     */
    public static final String GROUPS_PAGER_ID = "/groups/?";
    /**
     * Common substring for paging urls in a group's page.
     */
    public static final String GROUPS_FEED_PAGER_ID = "/groups/%s?bacr=";
    /**
     * Facebook mobile version authorization url.
     */
    public static final String AUTHORIZATION_URL = "https://m.facebook.com/login.php";
    /**
     * String template for url for retrieving friend list in a facebook page.
     * For accounts with username.
     */
    public static final String FRIEND_LIST_TEMPLATE = "https://m.facebook.com/%s/friends?startindex=%s";
    /**
     * String template for url for retrieving friend list in a facebook page.
     * For accounts with facebook id.
     */
    public static final String FRIEND_LIST_TEMPLATE_FOR_ID = "https://m.facebook.com/profile.php?v=friends&id=%s%s";
    /**
     * String template for url for retrieving news feed from the current bots
     * page.
     */
    public static final String FEED_TEMPLATE = "https://m.facebook.com/stories.php?aftercursorr=%s";
    /**
     * String template for url for retrieving news feed from an accounts page.
     */
    public static final String PROFILE_FEED_TEMPLATE = "https://m.facebook.com/%s";
    /**
     * String template for url for retrieving subscriptions list.
     */
    public static final String USER_SUBSCRIPTIONS_TEMPLATE = "https://m.facebook.com/subscribe/lists/?id=%s";
    /**
     * String template for url for retrieving groups list.
     */
    public static final String GROUPS_TEMPLATE = "https://m.facebook.com/groups/?%s";
    /**
     * String template for url for retrieving news feed from a group's page.
     */
    public static final String GROUPS_FEED_TEMPLATE = "https://m.facebook.com/groups/%s?bacr=%s";

}
