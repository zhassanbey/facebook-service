/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookclient.util;

import java.io.Serializable;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author Zhasan
 */
public class LightUtils implements Serializable {

    public static String normalizeProfileUrl(String profileUrl){
        if(profileUrl.contains("&")){
            return profileUrl.substring(0, profileUrl.indexOf("&"));
        }
        return profileUrl;
    }
    
    public static String normalizeUrl(String url) {

        String result = "https://www.facebook.com/";
        if (url.contains("groups")) {
            
            String href = null;
            
            if(url.contains("&id=")){
                String pre = url.substring(0, url.indexOf("?"));
                int startIndex = url.indexOf("&id=")+4;
                String pro = url.substring(startIndex, url.indexOf("&", startIndex));
                
                href = pre+"/"+pro;
            }
            
//            if(url.contains("&_ft_")){
//                href = StringUtils.getStringBtw(url, "/groups", 1, "&_ft_", 0);
//            } else {
//                href = StringUtils.getStringBtw(url, "/groups", 1, null, 0);
//            }
            
            
            result = turnToNormal(href);
            
        } else if(url.contains("fbid")){
            String id = StringUtils.getStringBtw(url, "&id=", 4, "&", 0);
            String storyId = StringUtils.getStringBtw(url, "fbid=", "fbid=".length(), "&", 0);

            if (!id.isEmpty()) {
                result = result + id + "/";
            } else {
                result = url;
            }

            if (!storyId.isEmpty()) {
                result = result + "posts/"+storyId;
            } else {
                result = url;
            }
        } else {
            result = turnToNormal(url);
        }
        return result;
    }

    public static String turnToNormal(String url) {
        String result = "https://www.facebook.com/";
        String index = "https://m.facebook.com/";

        String part = StringUtils.getStringBtw(url, index, index.length(), null, 0);

        result = result + part;

        return result;
    }
}
