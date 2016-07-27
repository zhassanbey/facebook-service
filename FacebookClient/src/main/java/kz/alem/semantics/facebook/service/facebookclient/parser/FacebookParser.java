package kz.alem.semantics.facebook.service.facebookclient.parser;

import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import kz.alem.semantics.facebook.service.facebookclient.constants.SocialNetworkUtil;
import kz.alem.semantics.parse.constants.DateConstants;
import kz.alem.semantics.solr.client.model.Attachment;
import kz.alem.semantics.solr.client.model.Profile;
import org.json.JSONException;
import org.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FacebookParser implements Serializable {

    public static final String CODE_SELECTOR = "code";
    public static final String POST_TEXT_SELECTOR = "div.userContent";
    public static final String POST_DATE_SELECTOR = "span span a abbr";
    public static final String POST_AUTHOR_SELECTOR = "h5 span a";
    public static final String PAGE_TITLE_SELECTOR = "pageTitle";

    public static final String DATE_UTIME_ATTR = "data-utime";

    private SimpleDateFormat solrDF;

    public FacebookParser() {
        solrDF = new SimpleDateFormat(DateConstants.SOLR_DATE_FORMAT);
        solrDF.setTimeZone(TimeZone.getTimeZone("Asia/Almaty"));
    }

    /**
     * Returns title from Document
     *
     * @param doc
     * @return
     */
    public String getPostTitle(Document doc) {
        Element title = doc.getElementById(PAGE_TITLE_SELECTOR);
        if (title != null) {
            return title.text();
        }
        return null;
    }

    /**
     * Returns date of post from element
     *
     * @param element
     * @return
     */
    public String getDate(Element element) {
        if (!element.select(FacebookParser.POST_DATE_SELECTOR).isEmpty()) {

            Long timestamp = new Long(element.select(POST_DATE_SELECTOR).attr(DATE_UTIME_ATTR)) * 1000;
            Date date = new Date(timestamp);
            String dateStr = solrDF.format(date);
            return dateStr;
        }

        return "";
    }

    /**
     * Returns profile of author of the post from hidden element
     *
     * @param element
     * @return
     */
    public Profile getPostAuthorProfile(Element element) {
        Element profileEl = element.select(POST_AUTHOR_SELECTOR).first();
        if (profileEl == null) {
            profileEl = element.select("span.fwb a").first();
        }
        if (profileEl == null) {
            profileEl = element.select("span.fwb.fcg a").first();
        }

        String profileElAttr = profileEl.attr("data-gt");
        JSONObject attrJSON = null;
        String profileId = null;

        if (!profileElAttr.isEmpty()) {
            try {
                attrJSON = new JSONObject(profileElAttr);
                profileId = (String) attrJSON.get("entity_id");
            } catch (JSONException ex) {
                Logger.getLogger(FacebookParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Profile profile = new Profile();
        profile.setType(Profile.ProfileType.user);
        Element photoEl = element.select("img").first();
        String photo = photoEl.attr("src");
        if (profileEl.attr("data-hovercard").contains("hovercard/page") || profileEl.attr("data-hovercard").contains("hovercard/group")) {
            profile.setType(Profile.ProfileType.group);
        }
        String profileLink = profileEl.attr("href");
        if (profileLink.contains("fref")) {
            profileLink = profileLink.substring(0, profileLink.indexOf("fref") - 1);
        }
        String screenName = profileLink.substring(profileLink.indexOf(".com/") + 5);
        String objectName = profileEl.text();
        if (profile.getType() == Profile.ProfileType.user) {
            if (objectName.contains(" ")) {
                String firstName = objectName.substring(0, objectName.indexOf(" "));
                String secondName = objectName.substring(objectName.indexOf(" ") + 1);
                profile.setFirstName(firstName);
                profile.setSecondName(secondName);
            }
        } else {
            screenName = profileLink.substring(profileLink.indexOf("groups/") + 7);
            if (screenName.contains("/")) {
                profileLink = profileLink.substring(profileLink.indexOf("/"));
            }
            profile.setFirstName(objectName);
        }

        profile.setName(objectName);
        profile.setId(profileId);
        profile.setPhoto(photo);

        if (profileId != null) {
            profile.setUrl("https://www.facebook.com/" + profileId);
        }

        if (!screenName.equals(profileId)) {
            profile.setScreenName(screenName);
        }

        return profile;
    }

    /**
     * Returns attachements from hiddenElement
     *
     * @param hiddenElement
     * @return
     */
    public List<Attachment> getPostAttachments(Element hiddenElement) {
        List<Attachment> attachments = null;
        Elements attachmentMTMEls = hiddenElement.select("div.mtm");
        if (attachmentMTMEls.size() > 0) {
            attachments = new ArrayList<>();
            Elements attachmentEls = hiddenElement.select("div.mtm a[ajaxify]");
            if (!attachmentEls.isEmpty()) {
                for (Element atEl : attachmentEls) {
                    Attachment att = new Attachment();
                    String attType = "link";
                    String ajaxify = atEl.attr("ajaxify");
                    //attachment type determination
                    if (ajaxify.contains("/video")) {
                        attType = "video";
                    } else if (ajaxify.contains("/photo")) {
                        attType = "photo";
                    }
                    String attLink = atEl.attr("href");//link parsing
                    if (attType.equals("photo")) {
                        try {
                            attLink = atEl.select("div img").first().attr("src");
                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (!attLink.contains("http")) {
                        String facebookURL = "https://www.facebook.com/";
                        if (attLink.charAt(0) == '/') {
                            facebookURL = "https://www.facebook.com" + attLink;
                        }
                        attLink = facebookURL + attLink;

                    }
                    String attTitle = null;
                    String attText = null;

                    if (attType.equals("video")) {
                        Elements attTextEl = hiddenElement.select("div.mtm div.mtm._5pco p");
                        Element attTitleEl = hiddenElement.select("div.mtm div._6o3.fsxxl.fwb").first();
                        if (attTextEl != null) {
                            attText = attTextEl.toString();
                        }
                        if (attTitle != null) {
                            attTitle = attTitleEl.toString();
                        }
                    }
                    if (attType.equals("link")) {
                        Element attTitleEl = hiddenElement.select("div.mtm div.mbs._6m6 a").first();
                        Elements attTextEl = hiddenElement.select("div.mtm div._6m7");
                        if (!attTextEl.isEmpty()) {
                            attText = attTextEl.toString();
                        }
                        if (attTitleEl != null) {
                            attTitle = attTitleEl.text();
                        }
                    }

                    att.setType(attType);
//                    att.setId(h);
                    att.setLink(attLink);
                    att.setText(attText);
                    att.setTitle(attTitle);
                    attachments.add(att);
                }
            } else {
                Attachment att = new Attachment();
                Element attachment = hiddenElement.select("div.mtm a").first();
                String attLink = attachment.attr("href");
                String attType = "link";

                Element attTitleEl = hiddenElement.select("div.mtm div.mbs._6m6 a").first();
                Elements attTextEl = hiddenElement.select("div.mtm div._6m7");
                String attTitle = null;
                String attText = null;
                if (attTitleEl != null) {
                    attTitle = attTitleEl.text();
                }
                if (attTextEl != null && !attTextEl.isEmpty()) {
                    attText = attTextEl.toString();
                }

                att.setType(attType);
                att.setLink(attLink);
                att.setText(attText);
                att.setTitle(attTitle);
                attachments.add(att);
            }
            return attachments;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Returns map<postId, userId> from feed page
     *
     * @param doc
     * @return
     */
    public Map<String, String> getFeedInfoMap(Document doc) {
        Map<String, String> result = new HashMap<>();

        Elements codeEls = doc.select("code");
        for (Element codeEl : codeEls) {
            Elements forms = Jsoup.parse(SocialNetworkUtil.getEncodedElementContent(codeEl)).select("form");
            for (Element formEl : forms) {
                Elements inputEls = Jsoup.parse(formEl.toString()).select("input");
                for (Element inputEl : inputEls) {
                    String input = inputEl.toString();
                    if (input.contains("&quot;actor&quot;:&quot;")) {
                        String userId = input.substring(input.lastIndexOf("&quot;actor&quot;:&quot;") + 24, input.indexOf("&quot;,&quot;target_fbid"));
                        String postId = input.substring(input.lastIndexOf(";target_fbid&quot;:&quot;") + 25, input.indexOf("&quot;,&quot;target_profile_id"));
                        result.put(postId, userId);
                    }
                }
            }
        }

        return result;
    }

    public List<String> getFriendsList(Document doc) {
        List<String> friends = new ArrayList<>();

        //Extracting html from comment section
        String commentedText = "";
        for (Element e : doc.getElementsByTag("code")) {
            Element x = e.attr("data-pnref", "friends.search");
            if (x.html().contains("friends.search")) {
                commentedText = e.html();
                break;
            }
        }

        if (commentedText.isEmpty()) {
            try {
                PrintWriter out = new PrintWriter(new File("facebook.com"));
                out.println(doc.html());
                out.close();
            } catch (Exception ex) {

            }
        }

        System.out.println("Commented text = " + commentedText);

        String html = commentedText.substring(5, commentedText.length() - 4);

        Document newDoc = Jsoup.parse(html);

        Elements elements = newDoc.select("a._5q6s._8o._8t.lfloat._ohe");
        System.out.println("*** Elements.size:" + elements.size());
        for (Element element : elements) {
            String url = element.attr("href");
            url = SocialNetworkUtil.trimFacebookProfileUrl(url);
            System.out.println("*** Url:" + url);
            friends.add(url);
        }

        return friends;
    }

}
