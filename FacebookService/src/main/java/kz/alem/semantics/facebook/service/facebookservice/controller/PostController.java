/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookservice.controller;

import kz.alem.semantics.facebook.service.facebookservice.controller.service.FetchService;
import kz.alem.semantics.facebook.service.facebookservice.controller.service.impl.FetchServiceImplLight;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller manages access to facebook posts
 *
 * @author Zhasan
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    /**
     * Manages retrieval of a facebook post, however it is applicable for downloading any page content from any url related to facebook.
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param url - url of the targeted post
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String getPost(@RequestParam(value = "bot_username") String botUsername, @RequestParam(value = "bot_password") String botPassword, @RequestParam(value = "url") String url) throws Exception {
        FetchService fetchService = new FetchServiceImplLight();
        String content = fetchService.getPageContent(botUsername, botPassword, url);
        return content;
    }
}
