/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookclient.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import kz.alem.semantics.solr.client.model.Profile;
import kz.alem.semantics.parse.constants.DateConstants;
import kz.alem.semantics.parse.model.Comment;
import kz.alem.semantics.parse.model.Feedback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Azat
 */
public class JSONFacebookParser {

    private JSONObject mainObject = null;
    private SimpleDateFormat solrDF;
    private List<Profile> profileList = new ArrayList<>();
    private List<Comment> commentList = new ArrayList<>();
    private Feedback feedback = new Feedback();
    
    public JSONFacebookParser(Document doc) {
        this.prepareParser(doc);
        this.parseJSONData();
    }

    private void parseJSONData() {
        if(mainObject == null){
            return;
        }
        
        try {
            if (mainObject.has("feedbacktargets")) {
                JSONArray feedBackTargetsJSONArray = mainObject.getJSONArray("feedbacktargets");
                feedback = parseFeedBackTargets(feedBackTargetsJSONArray);
            }
            if (mainObject.has("profiles")) {
                JSONArray profilesJSONArray = mainObject.getJSONArray("profiles");
                profileList = parseProfiles(profilesJSONArray);
            }
            if (mainObject.has("comments")) {
                JSONArray commentsJSONArray = mainObject.getJSONArray("comments");
                commentList = parseComments(commentsJSONArray);
            }
        } catch (JSONException ex) {
            Logger.getLogger(JSONFacebookParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<Profile> parseProfiles(JSONArray profiles) {
        List<Profile> profileList = new ArrayList<>();
        try {
            for (int i = 0; i < profiles.length(); i++) {
                Profile pr = new Profile();
                JSONObject profile = profiles.getJSONObject(i);
                pr.setId(profile.getString("id"));
                pr.setPhoto(profile.getString("thumbSrc"));
                pr.setUrl("https://www.facebook.com/" + profile.getString("id"));

                if (profile.getString("type").equals("user") || profile.getString("type").equals("friend")) {
                    pr.setType(Profile.ProfileType.user);
                    pr.setFirstName(profile.getString("firstName"));
                    pr.setSecondName(profile.getString("name").substring(profile.getString("name").indexOf(" ") + 1));
                } else {
                    pr.setType(Profile.ProfileType.group);
                    pr.setFirstName(profile.getString("name"));
                }
                if (!profile.getString("vanity").equals("")) {
                    pr.setScreenName(profile.getString("vanity"));
                }
                pr.setName(pr.getFirstName() + " " + pr.getSecondName());
                profileList.add(pr);
            }
        } catch (JSONException ex) {
            System.out.println("Error in parseProfiles method");
            Logger.getLogger(JSONFacebookParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return profileList;
    }

    private List<Comment> parseComments(JSONArray commentsJSONArr) {
        List<Comment> commentList = new ArrayList<>();
        try {
            for (int i = 0; i < commentsJSONArr.length(); i++) {
                Comment comment = new Comment();

                JSONObject commentJSONObj = commentsJSONArr.getJSONObject(i);
                Profile profile = getProfileById(commentJSONObj.getString("author"));
                if(profile != null){
                    comment.setProfile(profile);
                    comment.setAuthor(profile.getName());
                }
                comment.setText(commentJSONObj.getJSONObject("body").getString("text"));
                
                if (commentJSONObj.getInt("likecount") > 0) {
                    comment.setLikesCount(commentJSONObj.getInt("likecount"));
                }

                Date published = new Date(Long.parseLong(commentJSONObj.getJSONObject("timestamp").getString("time")) * 1000);
                String dateStr = solrDF.format(published);
                comment.setDate(dateStr);
                
                commentList.add(comment);
            }
        } catch (NumberFormatException | JSONException ex) {
            Logger.getLogger(JSONFacebookParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return commentList;
    }

    private Feedback parseFeedBackTargets(JSONArray feedBackTargets) {
        Feedback feedback = new Feedback();
        try {
            JSONObject feedBackTarget = (JSONObject) feedBackTargets.get(0);
            feedback.setLikesCount(feedBackTarget.getInt("likecount"));
            feedback.setCommentsCount(feedBackTarget.getInt("commentcount"));
            feedback.setSharesCount(feedBackTarget.getInt("sharecount"));
        } catch (JSONException ex) {
            Logger.getLogger(JSONFacebookParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return feedback;
    }

    public Profile getProfileById(String id) {
        for (Profile profile : profileList) {
            if (profile.getId().equals(id)) {
                return profile;
            }
        }
        return null;
    }

    private void prepareParser(Document doc) {
        solrDF = new SimpleDateFormat(DateConstants.SOLR_DATE_FORMAT);
        solrDF.setTimeZone(TimeZone.getTimeZone("Asia/Almaty"));

        Elements scripts = doc.select("script");
        String scriptHtml = "";
        for (int i = 12; i < scripts.size(); i++) {
            scriptHtml = scripts.get(i).html();
            if (scriptHtml.contains("bigPipe.onPageletArrive({\"content\":{\"stream_pagelet")) {
                break;
            }
        }
        try {
            String jsonCommentObjectString = scriptHtml.substring(scriptHtml.indexOf("onPageletArrive") + 16, scriptHtml.length() - 8);//get json from page
            JSONObject globalObject = new JSONObject(jsonCommentObjectString);
            JSONArray instances = (JSONArray) globalObject.getJSONObject("jsmods").getJSONArray("instances");
            mainObject = null;// mainObject stores comments, profiles, feedback targets
            for (int i = 0; i < instances.length(); i++) {
                JSONArray instance = (JSONArray) instances.get(i);
                JSONArray mainObjectContainingArray = ((JSONArray) instance.get(2));
                if (mainObjectContainingArray.length() > 2 && mainObjectContainingArray.get(2) instanceof JSONObject) {
                    mainObject = (JSONObject) mainObjectContainingArray.get(2);
                    if (mainObject.has("feedbacktargets")) {
                        break;
                    }
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(JSONFacebookParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Profile> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<Profile> profileList) {
        this.profileList = profileList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }
    
}
