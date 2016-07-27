/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookservice.controller;

import kz.alem.semantics.facebook.service.facebookservice.controller.service.FetchService;
import kz.alem.semantics.facebook.service.facebookservice.controller.service.impl.FetchServiceImplLight;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller manages facebook groups. Preferably the targeted group should
 * allow access to the current bot, in order to avoid blocked pages
 *
 * @author Zhasan
 */
@RestController
@RequestMapping("/groups")
public class GroupsController {

    /**
     * Manages the retrieval of the groups, current bot is involved in
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public String getGroups(@RequestParam(value = "bot_username") String botUsername,
            @RequestParam(value = "bot_password") String botPassword) throws Exception {
        FetchService service = new FetchServiceImplLight();
        String iContent = service.getGroupList(botUsername, botPassword);
        return iContent;
    }

    /**
     * Manages the retrieval of news feed of a group
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted group. Either
     * username of facebook id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/feed")
    public String getFeed(@RequestParam(value = "bot_username") String botUsername,
            @RequestParam(value = "bot_password") String botPassword, @RequestParam(value = "username") String username) throws Exception {
        FetchService service = new FetchServiceImplLight();
        String iContent = service.getGroupsFeedUrls(botUsername, botPassword, username);
        return iContent;
    }

}
