/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.alem.semantics.facebook.service.facebookclient.parser;

import java.util.Date;
import java.util.List;
import kz.alem.semantics.parse.model.Article;
import kz.alem.semantics.parse.model.Comment;
import kz.alem.semantics.parse.model.Feedback;
import kz.alem.semantics.parse.model.PageData;
import kz.alem.semantics.parse.util.DateUtil;
import kz.alem.semantics.solr.client.model.Attachment;
import kz.alem.semantics.solr.client.model.DocumentType;
import kz.alem.semantics.solr.client.model.Profile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Zhasan
 */
public class PostParser {
    
     public static PageData parsePost(FacebookParser parser, PageData pageData) {
        Article article = new Article();

        String content = pageData.getContent();
        Document doc = Jsoup.parse(content);

        if (doc.select(FacebookParser.CODE_SELECTOR).isEmpty()) {
            
            System.out.println("[PostParser/parsePost()] code selector in doc is empty");
            
            return pageData;
        }

        String hiddenHtml = doc.select(FacebookParser.CODE_SELECTOR).get(0).html();
        hiddenHtml = hiddenHtml.substring(5, hiddenHtml.lastIndexOf("-->"));
        Element hiddenElement = Jsoup.parse(hiddenHtml);

        if (hiddenElement.select(FacebookParser.POST_TEXT_SELECTOR).isEmpty()) {
            System.out.println("[PostParser/parsePost()] post txt selector is empty in doc");
            return pageData;
        }

        String text = hiddenElement.select(FacebookParser.POST_TEXT_SELECTOR).text();
        article.setText(text);

        String date = parser.getDate(hiddenElement);
        article.setDate(date);

        Profile profile = null;
        try {
            profile = parser.getPostAuthorProfile(hiddenElement);
            article.setAuthor(profile.getName());
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        String title = parser.getPostTitle(doc);
        article.setTitle(title);

        JSONFacebookParser jsonParser = new JSONFacebookParser(doc);

        List<Comment> commentList = jsonParser.getCommentList();
        pageData.setCommentList(commentList);

        Feedback feedback = jsonParser.getFeedback();
        pageData.setFeedback(feedback);

        try {
            List<Attachment> attachmentsList = parser.getPostAttachments(hiddenElement);
            pageData.setAttachmentList(attachmentsList);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        pageData.setDocumentType(DocumentType.post);

        String created = DateUtil.getSolrFormatDate(new Date());
        pageData.setCreated(created);

        pageData.setArticle(article);
        pageData.setProfile(profile);

        return pageData;
    }

    
}
