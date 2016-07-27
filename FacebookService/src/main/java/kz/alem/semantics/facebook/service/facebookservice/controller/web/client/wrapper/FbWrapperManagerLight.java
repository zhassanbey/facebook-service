/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookservice.controller.web.client.wrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class used to manage web clients between bots in light
 * implementation of fetcher service.
 *
 * @author Zhasan
 */
public class FbWrapperManagerLight {

    private Map<String, WebClientWrapperLight> map;

    private FbWrapperManagerLight() {
        map = new HashMap<>();
    }

    private static class Holder {

        private static FbWrapperManagerLight instance = new FbWrapperManagerLight();
    }

    public static FbWrapperManagerLight getInstance() {
        return Holder.instance;
    }

    public void put(String username, WebClientWrapperLight wrapper) {
        map.put(username, wrapper);
    }

    public WebClientWrapperLight get(String username) {
        return map.get(username);
    }
}
