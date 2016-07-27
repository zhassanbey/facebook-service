/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookservice.controller.util;

import org.json.JSONArray;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kz.alem.semantics.parse.model.Comment;
import kz.alem.semantics.parse.model.PageBlock;
import kz.alem.semantics.solr.client.model.Attachment;
import org.json.JSONException;

/**
 * Class containing utility methods for working with collections and json.
 *
 * @author Zhasan
 */
public class CollectionUtils {

    /**
     * Converts any collection of strings to a json array.
     *
     * @param strList - collection of strings
     * @return JSONArray
     */
    public static JSONArray from(Collection<String> strList) {
        JSONArray result = new JSONArray();
        if (strList != null) {
            for (String str : strList) {
                System.out.println(str);
                result.put(str);
            }
        }
        return result;
    }

    /**
     * Converts a list of Attachments to a json array.
     *
     * @param atts - list of attachments. Model Attahment.
     * @return JSONArray
     */
    public static JSONArray fromAttachments(List<Attachment> atts) throws JSONException {
        JSONArray result = new JSONArray();
        if (atts != null) {
            for (Attachment att : atts) {
                result.put(PageDataUtils.toJson(att));
            }
        }
        return result;
    }

    /**
     * Converts list of Comments to a json array
     *
     * @param comments - list of comments
     * @return JSONArray
     * @throws JSONException
     */
    public static JSONArray fromComments(List<Comment> comments) throws JSONException {
        JSONArray result = new JSONArray();
        if (comments != null) {
            for (Comment comment : comments) {
                result.put(PageDataUtils.toJson(comment));
            }
        }
        return result;
    }

    /**
     * Converts set of PageBlocks to a json array.
     *
     * @param pBlocks - Set of page blocks
     * @return JSONArray
     * @throws JSONException
     */
    public static JSONArray fromPageBlocks(Set<PageBlock> pBlocks) throws JSONException {
        JSONArray result = new JSONArray();
        if (pBlocks != null) {
            for (PageBlock pBlock : pBlocks) {
                result.put(PageDataUtils.toJson(pBlock));
            }
        }
        return result;
    }
}
