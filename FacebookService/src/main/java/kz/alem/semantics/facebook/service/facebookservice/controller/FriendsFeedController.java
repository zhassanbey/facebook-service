/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookservice.controller;

import java.util.List;
import kz.alem.semantics.facebook.service.facebookservice.controller.service.FetchService;
import kz.alem.semantics.facebook.service.facebookservice.controller.service.impl.FetchServiceImplLight;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller manages page of a facebook account. The account is preferred
 * to be somehow connected to the current bot, in order to avoid blocked pages
 *
 * @author Zhasan
 */
@RestController
@RequestMapping("/friends")
public class FriendsFeedController {

    /**
     * Manages retrieval of news timeline of an account
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username of facebook id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/feed")
    public String getTimeline(@RequestParam(value = "bot_username") String botUsername,
            @RequestParam(value = "bot_password") String botPassword,
            @RequestParam(value = "username") String username) throws Exception {
        FetchService service = new FetchServiceImplLight();
        List<String> contents = service.getProfileFeedUrls(botUsername, botPassword, username);
        return contents.get(0);
    }

    /**
     * Manages retrieval of friend list of the current bot user
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public String getFriendList(@RequestParam(value = "bot_username") String botUsername,
            @RequestParam(value = "bot_password") String botPassword) throws Exception {
        FetchService service = new FetchServiceImplLight();
        String content = service.getFriendsList(botUsername, botPassword);
        return content;
    }

    /**
     * Manages retrieval of friend list of an account
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username or facebook id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ofuser")
    public String getFriendList(@RequestParam(value = "bot_username") String botUsername,
            @RequestParam(value = "bot_password") String botPassword, @RequestParam(value = "username") String username) throws Exception {
        FetchService service = new FetchServiceImplLight();
        String content = service.getProfileFriendsList(botUsername, botPassword, username);
        return content;
    }

}
