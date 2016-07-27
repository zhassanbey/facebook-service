package kz.alem.semantics.facebook.service.facebookclient.constants;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Element;


public class SocialNetworkUtil implements Serializable{

    public static final String FACEBOOK_POST_URL_PATTERN = "(http|https)[://](.*)(facebook.com)[/](.*)[/](posts)[/](.*)";
    public static final String FACEBOOK_QUERY = "site:facebook.com posts";
    public static final String FACEBOOK_FEED_ADDRESS = "https://www.facebook.com/?sk=nf";
    public static final String FACEBOOK_FRIENDS_URL_TEMPLATE = "https://www.facebook.com/%s/friends";
    public static final String FACEBOOK_POST_URL_TEMPLATE = "https://www.facebook.com/%s/posts/%s";
    public static final String FACEBOOK_TIMELINE_SUFFIX = "/timeline/";
    
    
    /**
     * Returns true if url is matches for facebook post url
     * @param url
     * @return 
     */
    public static boolean isFacebookPost(String url){
        Pattern pattern = Pattern.compile(FACEBOOK_POST_URL_PATTERN);
        Matcher matcher = pattern.matcher(url);
        if (matcher.matches()) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Returns content of encoded element
     * @param element
     * @return 
     */
    public static String getEncodedElementContent(Element element) {
        return element.html().substring(5, element.html().indexOf("-->"));
    }
    
    /**
     * Returns true if url is matches for post url
     * @param url
     * @return 
     */
    public static boolean isPost(String url){
        if(isFacebookPost(url)){
            return true;
        }
        return false;
    }
    
    public static String trimFacebookProfileUrl(String url) {
        if(url.contains("?")) {
            if(url.contains("profile.php")&&url.contains("id=")) {
                int index = url.indexOf("&");
                return index>=0 ? url.substring(0, index) : url;
            } else {
                return NetUtil.removeUrlAttributes(url);
            }
        }
        return url;
    }
}
