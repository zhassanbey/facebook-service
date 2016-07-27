/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookservice.controller.util;

import kz.alem.semantics.parse.model.Article;
import kz.alem.semantics.parse.model.Comment;
import kz.alem.semantics.parse.model.Feedback;
import kz.alem.semantics.parse.model.PageBlock;
import kz.alem.semantics.parse.model.PageData;
import kz.alem.semantics.solr.client.model.Attachment;
import kz.alem.semantics.solr.client.model.Coordinate;
import kz.alem.semantics.solr.client.model.Location;
import kz.alem.semantics.solr.client.model.Profile;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class containing utility methods for performing convert operation from
 * PageData objects to json. Not used in the current version of service.
 *
 * @author Zhasan
 */
public class PageDataUtils {

    public static JSONObject toJson(PageData pageData) throws JSONException {
        JSONObject result = new JSONObject();

        if (pageData != null) {
            result.put("url", pageData.getUrl());
            result.put("content", pageData.getContent());
            result.put("article", toJson(pageData.getArticle()));
            result.put("status", pageData.getStatus() + "");
            result.put("created", pageData.getCreated());
            result.put("docType", pageData.getDocumentType() + "");
            result.put("level", pageData.getLevel());
            result.put("highlight", pageData.getHighlight());
            result.put("lastCrawledDate", pageData.getLastCrawledDate());
            result.put("outlinks", CollectionUtils.from(pageData.getOutlinks()));
            result.put("published", pageData.getPublished());
            result.put("isUrlMatchesPattern", pageData.isUrlMatchesPattern());
            result.put("topicId", pageData.getTopicId());

            result.put("profile", toJson(pageData.getProfile()));
            result.put("feedback", toJson(pageData.getFeedback()));
            result.put("attachmentList", CollectionUtils.fromAttachments(pageData.getAttachmentList()));
            result.put("commentList", CollectionUtils.fromComments(pageData.getCommentList()));
            result.put("pageBlockSet", CollectionUtils.fromPageBlocks(pageData.getPageBlockSet()));
        }
        return result;
    }

    public static JSONObject toJson(Profile profile) throws JSONException {
        JSONObject result = new JSONObject();

        if (profile != null) {
            result.put("id", profile.getId());
            result.put("firstName", profile.getFirstName());
            result.put("name", profile.getName());
            result.put("type", profile.getType());
            result.put("screenName", profile.getScreenName());
            result.put("secondName", profile.getSecondName());
            result.put("url", profile.getUrl());
            result.put("photo", profile.getPhoto());
            result.put("location", toJson(profile.getLocation()));
        }
        return result;
    }

    public static JSONObject toJson(Attachment att) throws JSONException {
        JSONObject result = new JSONObject();
        if (att != null) {
            result.put("id", att.getId());
            result.put("link", att.getLink());
            result.put("text", att.getText());
            result.put("thumb", att.getThumb());
            result.put("title", att.getTitle());
            result.put("type", att.getType());
        }
        return result;
    }

    public static JSONObject toJson(PageBlock pBlock) throws JSONException {
        JSONObject result = new JSONObject();

        if (pBlock != null) {
            result.put("tag", pBlock.getTag());
            result.put("text", pBlock.getText());
        }
        return result;
    }

    public static JSONObject toJson(Comment comment) throws JSONException {
        JSONObject result = new JSONObject();
        if (comment != null) {
            result.put("author", comment.getAuthor());
            result.put("date", comment.getDate());
            result.put("text", comment.getText());
            result.put("profile", toJson(comment.getProfile()));
            result.put("likesCount", comment.getLikesCount());
            result.put("dislikesCount", comment.getDislikesCount());
        }
        return result;
    }

    public static JSONObject toJson(Feedback fBack) throws JSONException {
        JSONObject result = new JSONObject();

        if (fBack != null) {
            result.put("commentsCount", fBack.getCommentsCount());
            result.put("dislikesCount", fBack.getDislikesCount());
            result.put("likesCount", fBack.getLikesCount());
            result.put("sharesCount", fBack.getSharesCount());
        }
        return result;
    }

    public static JSONObject toJson(Article article) throws JSONException {
        JSONObject result = new JSONObject();

        if (article != null) {
            result.put("date", article.getDate());
            result.put("text", article.getText());
            result.put("title", article.getTitle());
            result.put("author", article.getAuthor());
        }
        return result;
    }

    //secondary models
    public static JSONObject toJson(Location location) throws JSONException {
        JSONObject result = new JSONObject();
        if (location != null) {
            result.put("id", location.getId());
            result.put("city", location.getCity());
            result.put("country", location.getCountry());
            result.put("placename", location.getPlacename());
            result.put("region", location.getRegion());
            result.put("coordinate", toJson(location.getCoordinate()));
        }
        return result;
    }

    public static JSONObject toJson(Coordinate coordinate) throws JSONException {
        JSONObject result = new JSONObject();

        if (coordinate != null) {
            result.put("lat", coordinate.getLattitude());
            result.put("lon", coordinate.getLongitude());
        }
        return result;
    }
}
