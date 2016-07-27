/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookclient.parser;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import kz.alem.semantics.facebook.service.facebookclient.util.DateUtils;
import kz.alem.semantics.facebook.service.facebookclient.util.GeoUtils;
import kz.alem.semantics.facebook.service.facebookclient.util.StringUtils;
import kz.alem.semantics.parse.constants.DateConstants;
import kz.alem.semantics.parse.model.Article;
import kz.alem.semantics.parse.model.Comment;
import kz.alem.semantics.parse.model.Feedback;
import kz.alem.semantics.parse.model.PageData;
import kz.alem.semantics.solr.client.model.Attachment;
import kz.alem.semantics.solr.client.model.DocumentType;
import kz.alem.semantics.solr.client.model.Profile;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Zhasan
 */
public class FacebookLightParser implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(FacebookLightParser.class);
    private static final Logger ERR = Logger.getLogger("parse");
    
    SimpleDateFormat solrDF;
    
    public FacebookLightParser() {
        solrDF = new SimpleDateFormat(DateConstants.SOLR_DATE_FORMAT);
        
        solrDF.setTimeZone(TimeZone.getTimeZone("Asia/Almaty"));
    }
    
    public String parseCountry(Document doc) {
        
        String country = null;
        try {
            Element div = doc.getElementById("living").child(0).child(1).child(0).child(0);
            Elements hrefs = div.getElementsByTag("a");
            for(Element a:hrefs){
                country = GeoUtils.getCountry(a.html());
                if(country != null){
                    break;
                }
                country = a.html();
            }
        } catch (Exception ex) {
            ERR.error("cannot parse geo location");
        }
        
        return country;
    }
    
    public List<String> parseShareUrls(Document doc) {
        List<String> result = new ArrayList<>();
        
        Elements likeElements = doc.getElementsByAttributeValueStarting("href", "/composer/mbasic/?c_src=share");
        
        if (likeElements.size() == 0) {
            ERR.error(doc.html());
        }
        
        int count = 0;
        
        for (Element e : likeElements) {
            String href = e.attr("href");
            result.add(href.substring(1, href.length()));
        }
        
        return result;
    }
    
    public List<String> parseLikeUrls(Document doc) {
        List<String> result = new ArrayList<>();
        
        Elements likeElements = doc.getElementsByAttributeValueStarting("href", "/a/like.php?ul&");
        
        if (likeElements.size() == 0) {
            ERR.error(doc.html());
        }
        
        int count = 0;
        
        for (Element e : likeElements) {
            String href = e.attr("href");
            result.add(href.substring(1, href.length()));
        }
        
        return result;
    }
    
    public List<String> parsePostUrls(Document doc) {
        List<String> result = new ArrayList<>();
        
        Elements dcElements = doc.getElementsByAttributeValueContaining("href", "#footer_action_list");
        
        if (dcElements.size() == 0) {
            
            ERR.error(doc.html());
            
        }
        
        int count = 0;
        for (Element e : dcElements) {
            String text = e.outerHtml();
            String href = StringUtils.getStringBtw(text, "href", 6, "\">", 0);
            result.add(StringEscapeUtils.unescapeXml(href));
            count++;
        }
        
        return result;
    }
    
    public PageData parseAlbumPhotos(PageData result, Document doc) {
        
        Element main = doc.getElementsByAttributeValue("role", "main").first().child(0);
        Element container = main;
        int i = 0;
        for (i = 0; i < 5; i++) {
            container = container.child(0);
        }
        
        try {
            Element artCont = container.child(0);
            
            result.setPublished(getPublished(artCont));
            
            int j = 0;
            for (j = 0; j < 4; j++) {
                artCont = artCont.child(0);
            }
            
            Article article = getAlbumArticle(artCont.child(0));
            
            try {
                Profile profile = getAlbumProfile(artCont.child(0).child(1));
                
                article.setAuthor(profile.getName());
                
                result.setProfile(profile);
                
            } catch (Exception ex) {
                ERR.error(ex);
            }
            
            result.setArticle(article);
            
        } catch (Exception ex) {
            ERR.error(ex);
        }
        try {
            Element attCont = container.child(2);
            List<Attachment> atts = getAttachments(attCont);
            result.setAttachmentList(atts);
        } catch (Exception ex) {
            ERR.error(ex);
        }
        
        try {
            Element commCont = container.child(4).child(0).child(0).child(2);
            List<Comment> cmnts = getComments(commCont);
            result.setCommentList(cmnts);
        } catch (Exception ex) {
            LOG.info("No comments in this album");
        }
        
        result.setCreated(solrDF.format(new Date()));
        
        return result;
    }
    
    public Article getAlbumArticle(Element div) {
        Article result = new Article();
        
        Element textCont = div.child(0);
        
        String text = (textCont.html().contains(">")) ? StringUtils.getTextBtw(textCont.html(), ">", 1, "<", 0) : textCont.html();
        result.setText(text);
        result.setDate(getPublished(div));
        
        String title = text;
        
        if (title.length() > 105) {
            title = text.substring(0, 105) + "...";
        }
        
        result.setTitle(title);
        return result;
    }
    
    public Profile getAlbumProfile(Element div) {
        Profile result = new Profile();
        
        Element a = div.getElementsByTag("a").first();
        String href = a.attr("href");
        
        String screenName = href.substring(1, href.length());
        
        String name = a.html();
        
        String tokens[] = name.split(" ");
        
        String firstName = tokens[0];
        String lastName = "";
        if (tokens.length > 1) {
            lastName = tokens[1];
        }
        
        result.setFirstName(firstName);
        result.setName(name);
        result.setScreenName(screenName);
        result.setUrl("https://www.facebook.com/" + screenName);
        result.setType(Profile.ProfileType.user);
        result.setSecondName(lastName);
        
        return result;
    }
    
    public PageData parsePhoto(PageData result, Document doc) {
        
        Element main = doc.getElementsByAttributeValue("role", "main").first();
        Element mOrk = main.child(0);
        Element containterDiv = mOrk.child(0);
        
        Element thirdDiv = containterDiv.child(2);
        
        Element div = thirdDiv.child(0);
        
        Article article = getPhotoArticle(div);
        
        try {
            List<Attachment> atts = getAttachments(div);
            atts.add(getPhotoAttachment(containterDiv.child(0)));
            result.setAttachmentList(atts);
        } catch (Exception ex) {
            LOG.info("No attachments in photo");
            ERR.error(ex);
        }
        
        result.setPublished(getPublished(div));
        
        article.setDate(result.getPublished());
        
        result.setArticle(article);
        result.setCreated(solrDF.format(new Date()));
        
        result.setDocumentType(DocumentType.post);
        result.setProfile(getPhotoProfile(div));
        
        try {
            Element commentCont = thirdDiv.child(1);
            Element fca = commentCont.child(0);
            Element dbcc = fca.child(0);
            
            result.setCommentList(getComments(dbcc.child(2)));
        } catch (Exception ex) {
            LOG.info("Bu postin hich commenti yok galiba...");
        }
        try {
            result.setFeedback(getFeedback(div));
        } catch (Exception ex) {
            LOG.info("[no feedback for " + result.getUrl() + "]");
        }
        
        return result;
    }
    
    public Attachment getPhotoAttachment(Element div) {
        Attachment result = new Attachment();
        
        Element img = div.getElementsByTag("img").first();
        
        result.setLink(img.attr("src"));
        result.setType("photo");
        
        return result;
    }
    
    public Profile getPhotoProfile(Element div) {
        Profile result = new Profile();
        
        String author = "";
        
        String screenName = "";
        
        String href = "";
        result.setType(Profile.ProfileType.user);
        
        Element bo = div.getElementsByTag("strong").first();
        if (bo != null) {
            author = bo.html();
            
            href = bo.parent().attr("href");
            
            if (href != null && !href.contains("profile.php")) {
                screenName = getScreenName(href);
                if (screenName.contains("/")) {
                    result.setType(Profile.ProfileType.group);
                }
            }
        }
        
        if (screenName != null && !screenName.isEmpty()) {
            result.setScreenName(screenName);
            result.setUrl("https://www.facebook.com/" + screenName);
        } else {
            result.setUrl("https://www.facebook.com" + href);
        }
        
        if (author != null && !author.isEmpty()) {
            
            if (author.contains("<")) {
                author = StringUtils.getTextBtw(bo.html(), ">", 1, "<", 0);
            }
            
            String tokens[] = author.split(" ");
            
            if (tokens.length == 2) {
                result.setFirstName(tokens[0]);
                result.setSecondName(tokens[1]);
            }
        }
        result.setName(author);
        return result;
    }
    
    public Article getPhotoArticle(Element div) {
        Article result = new Article();
        
        String text = "";
        String author = "";
        
        Element bo = div.getElementsByTag("strong").first();
        if (bo != null) {
            author = bo.html();
            if (author.contains("<")) {
                author = StringUtils.getTextBtw(bo.html(), ">", 1, "<", 0);
            }
        }
        
        Element bq = div.getElementsByClass("bq").first();//div.child(0).child(2);

        if (bq == null || bq.html().isEmpty()) {
            bq = div.getElementsByTag("div").first();
        }
        
        if (bq != null) {
            if (bq.html().contains("<")) {
                text = StringUtils.getTextBtw(bq.outerHtml(), ">", 1, "<", 0);
            } else {
                text = bq.html();
            }
        }
        
        String title = "";
        
        if (text.length() > 105) {
            title = text.substring(0, 105) + "...";
        } else {
            title = text;
        }
        
        result.setAuthor(author);
        result.setText(text);
        result.setTitle(title);
        return result;
    }
    
    public PageData parsePost(PageData result, Document doc) {
        
        Element root = doc.getElementsByAttributeValue("role", "main").first();
        
        Element permalinkView = root.child(0);
        
        Element div = doc.getElementsByAttributeValueStarting("id", "u").first();
        
        Article article = getArticle(div);
        
        try {
            List<Attachment> atts = getAttachments(div.child(0).child(1));
            result.setAttachmentList(atts);
        } catch (Exception ex) {
            LOG.info("No attachments in post");
            ERR.error(ex);
        }
        
        result.setPublished(getPublished(div));
        
        article.setDate(result.getPublished());
        
        result.setArticle(article);
        
        result.setProfile(getProfile(div));
        
        result.setCreated(solrDF.format(new Date()));
        
        result.setDocumentType(DocumentType.post);
        
        try {
            
            Element secondBlock = permalinkView.child(1);
            
            Element c = secondBlock.child(0);
            
            Element comments = c.child(2);
            
            if (!comments.html().contains("<form")) {
                List<Comment> cmnts = getComments(comments);
                if (!cmnts.isEmpty()) {
                    result.setCommentList(cmnts);
                }
            } else {
                comments = c.child(3);
                
                if (!comments.html().contains("<form")) {
                    List<Comment> cmnts = getComments(comments);
                    if (!cmnts.isEmpty()) {
                        result.setCommentList(cmnts);
                    }
                }
            }
            
        } catch (Exception ex) {
            LOG.info("No comments");
        }
        try {
            result.setFeedback(getFeedback(div));
        } catch (Exception ex) {
            LOG.info("[no feedback for " + result.getUrl() + "]");
        }
        
        return result;
    }
    
    public List<Comment> getComments(Element div) {
        List<Comment> result = new ArrayList<>();
        
        if (div != null) {
            for (Element e : div.children()) {
                Comment comment = new Comment();
                
                try {
                    Profile profile = getCommentProfile(e);
                    comment.setProfile(profile);
                    comment.setAuthor(profile.getName());
                    
                    if (e.children() != null && e.children().size() >= 2) {
                        String txtContent = e.child(1).outerHtml();
                        if (txtContent.contains("<")) {
                            comment.setText(StringEscapeUtils.unescapeXml(StringUtils.getTextBtw(txtContent, ">", 1, "<", 0)));
                        } else {
                            comment.setText(StringEscapeUtils.unescapeXml(txtContent));
                        }
                    }
                    comment.setDate(getPublished(e));
                    
                    result.add(comment);
                } catch (Exception ex) {
                    ERR.error("Error parsing child " + e);
// ex.printStackTrace();
                }
            }
        }
        
        return result;
    }
    
    public Profile getCommentProfile(Element div) {
        Profile result = new Profile();
        String author = "";
        
        Element h3 = div.getElementsByTag("h3").first();
        
        Element a = h3.getElementsByTag("a").first();
        author = a.html();
        
        if (author != null) {
            String tokens[] = author.split(" ");
            if (tokens.length == 2) {
                result.setFirstName(tokens[0]);
                result.setSecondName(tokens[1]);
            }
            
            if (author.contains("<")) {
                author = StringUtils.getTextBtw(a.html(), ">", 1, "<", 0);
            }
            
            result.setName(StringEscapeUtils.unescapeXml(author));
        }
        
        String href = StringEscapeUtils.unescapeXml(a.attr("href"));
        
        String screenName = getScreenName(href);
        
        if (!screenName.contains("profile.php")) {
            result.setScreenName(screenName);
            result.setUrl("https://www.facebook.com/" + screenName);
        } else {
            result.setUrl("https://www.facebook.com" + href);
        }
        
        return result;
    }
    
    public Feedback getFeedback(Element div) {
        Feedback result = new Feedback();
        
        Element likes = div.getElementsByAttributeValueContaining("href", "/likes/").first();
        
        if (likes != null) {
            String likeStr = likes.html();
            
            String number = "";
            
            for (String str : likeStr.split(" ")) {
                if (str.contains("^(?=.*[0-9])")) {
                    number = number + str;
                }
            }
            
            int likesCount = Integer.parseInt(number.trim());
            result.setLikesCount(likesCount);
        }
        
        return result;
    }
    
    public Article getArticle(Element div) {
        Article article = new Article();
        Elements ep = div.getElementsByTag("p");
        
        String text = "";
        String title = "";
        if (ep != null && !ep.isEmpty()) {
            for (Element p : ep) {
                
                String txt = p.outerHtml();
                
                if (txt.contains("<")) {
                    text = text + StringUtils.getTextBtw(txt, ">", 1, "<", 0).trim();
                } else {
                    text = text + txt;
                }
            }
        } else {
            LOG.info("[No text in ]");
        }
        
        String author = "";
        
        try {
            Element h3 = div.getElementsByTag("h3").first();
            
            Element strong = h3.getElementsByTag("strong").last();
            
            if (strong == null || strong.html().isEmpty()) {
                strong = h3;
            }
            
            author = strong.getElementsByTag("a").first().html();
            
            if (author.contains("<")) {
                author = StringUtils.getTextBtw(strong.getElementsByTag("a").first().html(), ">", 1, "<", 0);
            }
        } catch (Exception ex) {
            ERR.error(ex);
        }
        
        if (text.length() > 105) {
            title = text.substring(0, 105) + "...";
        } else {
            title = text;
        }
        
        article.setText(StringEscapeUtils.unescapeXml(text));
        article.setTitle(StringEscapeUtils.unescapeXml(title));
        article.setAuthor(StringEscapeUtils.unescapeXml(author));
        
        return article;
    }
    
    public String getScreenName(String href) {
        if (href.contains("?")) {
            return StringUtils.getStringBtw(href, "/", 1, "?", 0);
        } else {
            return StringUtils.getStringBtw(href, "/", 1, null, 0);
        }
    }
    
    public List<Attachment> getAttachments(Element div) {
        Elements links = div.getElementsByTag("a");
        
        List<Attachment> atts = new ArrayList<>();
        
        if (links != null && !links.isEmpty()) {
            for (Element link : links) {
                Attachment att = new Attachment();
                String href = StringEscapeUtils.unescapeXml(link.attr("href"));
                att.setType("link");
                
                if (href.startsWith("http")) {
                    att.setLink(href);
                } else {
                    att.setLink("https://www.facebook.com" + href);
                    if (href.contains("photo.php") || href.contains("/photo")) {
                        att.setType("photo");
                    }
                }
                
                String text = link.html();
                
                if (text != null && !text.isEmpty()) {
                    if (text.contains("<")) {
                        att.setText(StringUtils.getTextBtw(text, ">", 1, "<", 0));
                    } else {
                        att.setText(text);
                    }
                }
                atts.add(att);
            }
        }
        
        Elements imgs = div.getElementsByTag("img");
        for (Element img : imgs) {
            Attachment att = new Attachment();
            String href = StringEscapeUtils.unescapeXml(img.attr("src"));
            
            String text = img.html();
            
            if (href.startsWith("http")) {
                att.setLink(href);
            } else {
                att.setLink("https://www.facebook.com" + href);
            }
            
            if (text != null && !text.isEmpty()) {
                if (text.contains("<")) {
                    att.setText(StringEscapeUtils.unescapeXml(StringUtils.getTextBtw(text, ">", 1, "<", 0)));
                } else {
                    att.setText(StringEscapeUtils.unescapeXml(text));
                }
            }
            
            att.setType("photo");
        }
        
        return atts;
    }
    
    public String getPublished(Element div) {
        
        Date date = new Date();
        
        Elements abbrs = div.getElementsByTag("abbr");
        
        FacebookParser parser = new FacebookParser();
        
        if (abbrs != null && !abbrs.isEmpty()) {
            for (Element abbr : abbrs) {
                
                DateUtils utils = new DateUtils();
                String dStr = StringUtils.normalizeString(abbr.html());
                
                LOG.info(dStr);
                
                try {
                    
                    date = utils.getDate(dStr);
                    
                } catch (ParseException ex) {
                    
                    try {
                        date = utils.getEstimated(dStr);
                    } catch (ParseException ex1) {
                        ERR.error("Couldn't parse date" + dStr);
                    }
                    
                }
            }
        }
        
        return solrDF.format(date);
    }
    
    public Profile getProfile(Element div) {
        Profile result = new Profile();
        
        String author = "";
        
        String screenName = "";
        
        String href = "";;
        try {
            Element h3 = div.getElementsByTag("h3").first();
            
            Element strong = h3.getElementsByTag("strong").last();
            
            if (strong == null || strong.html().isEmpty()) {
                strong = h3;
            }
            
            Element a = strong.getElementsByTag("a").first();
            
            href = a.attr("href");
            
            screenName = getScreenName(href);
            
            author = a.html();
            
            if (author.contains("<")) {
                author = StringUtils.getTextBtw(a.html(), ">", 1, "<", 0);
            }
            
        } catch (Exception ex) {
            ERR.error(ex);
        }
        
        if (author != null && !author.isEmpty()) {
            String tokens[] = author.split(" ");
            
            if (tokens.length == 2) {
                result.setFirstName(tokens[0]);
                result.setSecondName(tokens[1]);
            }
            
            result.setName(StringEscapeUtils.unescapeXml(author));
        }
        
        result.setType(Profile.ProfileType.user);
        
        if (screenName != null && !screenName.isEmpty() && !screenName.contains("profile.php")) {
            result.setScreenName(screenName);
            if (screenName.contains("/")) {
                result.setType(Profile.ProfileType.group);
            }
        }
        
        if (screenName != null && !screenName.contains("profile.php")) {
            result.setUrl("https://www.facebook.com/" + screenName);
        } else {
            result.setUrl("https://www.facebook.com" + StringEscapeUtils.unescapeXml(href));
        }
        
        return result;
    }
    
    public List<String> parseFriendList(Document doc) {
        
        List<String> result = new ArrayList<>();
        
        Elements wts = doc.getElementsByAttributeValue("role", "main");
        
        LOG.info("wts.size = " + wts.size());
        for (Element wt : wts) {
            LOG.info(wts.indexOf(wt));
            Element be = wt.child(0);
            int i = wts.indexOf(wt);
            Element div2 = be.child(1 + ((i == 0) ? 0 : 1));
            
            Element container = (i == 0) ? div2.child(3) : div2;
            
            LOG.info("here " + container.children().size());
            
            addScreenNames(result, container);
        }
        return result;
    }
    
    public List<String> parseProfileFriendList(Document doc) {
        List<String> result = new ArrayList<>();
        
        Elements wts = doc.getElementsByAttributeValue("role", "main");
        
        LOG.info("wts.size = " + wts.size());
        for (Element wt : wts) {
            LOG.info(wts.indexOf(wt));
            Element be = wt.child(0);
            int i = wts.indexOf(wt);
            Element div2 = be.child(1 + ((i == 0) ? 2 : 0));
            
            Element container = div2;

            //  System.out.println(wts.indexOf(wt) + ") containter.size = " + container.children().size());
            LOG.info("here " + container.children().size());
            
            addScreenNames(result, container);
        }
        return result;
    }
    
    public List<String> parseGroupsList(Document doc) {
        List<String> result = new ArrayList<>();
        
        Elements wts = doc.getElementsByAttributeValue("role", "main");
        
        LOG.info("wts.size = " + wts.size());
        for (Element wt : wts) {
            int i = wts.indexOf(wt);
            
            Element container = wt.child(0).child(0).child(0).child(0).child(1).child(1);
            
            LOG.info("container.children().size = " + container.children().size());
            
            for (Element e : container.children()) {
                
                String groupId = getGroupId(e);
                
                if (groupId != null && !groupId.isEmpty()) {
                    result.add(groupId);
                }
            }
            
        }
        
        return result;
    }
    
    public String getGroupId(Element e) {
        String result = "";
        
        String href = e.getElementsByTag("a").first().attr("href");
        
        String groups = "/groups/";
        
        if (href.contains(groups)) {
            result = StringUtils.getStringBtw(href, groups, groups.length(), "?", 0);
        }
        
        return result;
    }
    
    public List<String> parseSubscribesList(Document doc) {
        List<String> result = new ArrayList<>();
        
        Elements wts = doc.getElementsByAttributeValue("role", "main");
        
        LOG.info("wts.size = " + wts.size());
        for (Element wt : wts) {
            int i = wts.indexOf(wt);
            
            try {
                Element div1 = wt.child(1);
                
                addScreenNames(result, div1);
            } catch (Exception ex) {
            }
            
            try {
                Element div2 = wt.child(2);
                
                addScreenNames(result, div2);
            } catch (Exception ex) {
                
            }
            
        }
        
        return result;
    }
    
    public void addScreenNames(List<String> result, Element container) {
        
        if (container != null) {
            
            for (Element e : container.children()) {
                Element a = e.getElementsByTag("a").first();
                
                try {
                    String href = a.attr("href");
                    
                    if (!href.contains("/subscribe/lists")) {
                        LOG.info(" href=" + href);
                        
                        String username = "";
                        if (href.contains("profile.php")) {
                            username = StringUtils.getStringBtw(href, "=", 1, "&", 0);
                        } else {
                            username = StringUtils.getStringBtw(href, "/", 1, "?", 0);
                        }
                        
                        result.add(username);
                    }
                    
                } catch (NullPointerException ex) {
                    
                }
            }
        }
    }
    
    public long parseUsersSubscribersCount(String html) {
        long count = 0;
        
        String result = "";
        try {
            String finder = "class=\"fwn fcg\">";
            
            String str = StringUtils.getStringBtw(html, finder, finder.length(), "<", 0);
            
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                
                int num = ch - '0';
                
                if (num >= 0 && num <= 9) {
                    result += num;
                }
            }
            count = Long.parseLong(result);
        } catch (Exception ex) {
            ERR.error(ex);
            // System.out.println(ex.getMessage() + " for " + doc);
        }
        
        return count;
    }
    
}
