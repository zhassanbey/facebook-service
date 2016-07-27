/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookservice.controller.web.client.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kz.alem.semantics.sql.orm.model.Bot;

/**
 * Singleton class used to manage WebClients between bots.
 *
 * @author Zhasan
 */
public class FbWrapperManager implements Serializable {

    private Map<String, WebClientWrapper> wrappers;

    private FbWrapperManager() {
        wrappers = new HashMap<>();
    }

    private static class Holder {

        private static FbWrapperManager instance = new FbWrapperManager();
    }

    public static FbWrapperManager getInstance() {
        return Holder.instance;
    }

    public void put(String username, WebClientWrapper wrapper) {
        wrappers.put(username, wrapper);
    }

    public WebClientWrapper get(String username) {
        return wrappers.get(username);
    }

    public List<String> getBotList() {
        List<String> result = new ArrayList<>();

        result.addAll(wrappers.keySet());

        return result;
    }
}
