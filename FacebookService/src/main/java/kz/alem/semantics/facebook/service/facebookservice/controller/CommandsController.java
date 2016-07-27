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
 * This controller manages action commands directed to bots
 *
 * @author Zhasan
 */
@RestController
@RequestMapping("/command")
public class CommandsController {

    /**
     * Manages subscribe action command facebook
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the account to subscribe. Either
     * username of facebook id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/subscribe")
    public boolean subscribeUser(@RequestParam(value = "bot_username") String botUsername,
            @RequestParam(value = "bot_password") String botPassword, @RequestParam(value = "username") String username) throws Exception {
        FetchService service = new FetchServiceImplLight();
        boolean result = service.subscribePerson(botUsername, botPassword, username);
        return result;
    }

    /**
     * Manages make friend action command in facebook
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the account to make friend with.
     * Either username of facebook id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/makefriend")
    public boolean makeFriend(@RequestParam(value = "bot_username") String botUsername,
            @RequestParam(value = "bot_password") String botPassword, @RequestParam(value = "username") String username) throws Exception {
        FetchService service = new FetchServiceImplLight();
        boolean result = service.makeFriend(botUsername, botPassword, username);
        return result;
    }

    /**
     * Manages like action command in facebook
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param url - url of the post to be liked
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public boolean likePost(@RequestParam(value = "bot_username") String botUsername,
            @RequestParam(value = "bot_password") String botPassword, @RequestParam(value = "url") String url) throws Exception {
        FetchService service = new FetchServiceImplLight();
        boolean result = service.like(botUsername, botPassword, url);
        return result;
    }

    /**
     * Manages share command in facebook
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param url - url of the post to be shared
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/share", method = RequestMethod.POST)
    public String getShareForm(@RequestParam(value = "bot_username") String botUsername,
            @RequestParam(value = "bot_password") String botPassword, @RequestParam(value = "url") String url) throws Exception {
        FetchService service = new FetchServiceImplLight();
        String result = service.getShareForm(botUsername, botPassword, url);
        return result;
    }

}
