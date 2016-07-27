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
 * This controller manages facebook subscriptions
 *
 * @author Zhasan
 */
@RestController
@RequestMapping("/subscribes")
public class SubscribesController {

    /**
     * Manages retrieval of list of users subscribed by the current bot
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public String getSubscribes(@RequestParam(value = "bot_username") String botUsername,
            @RequestParam(value = "bot_password") String botPassword) throws Exception {
        String result = null;
        FetchService service = new FetchServiceImplLight();
        String iContent = service.getSubscribesList(botUsername, botPassword);
        return iContent;
    }

    /**
     * Manages retrieval of list of users subscribed by an account. Preferably
     * the account should be somehow connected to the current bot, in order to
     * avoid blocked pages
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username of facebook id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user")
    public String getUserSubscribes(@RequestParam(value = "bot_username") String botUsername,
            @RequestParam(value = "bot_password") String botPassword, @RequestParam(value = "username") String username) throws Exception {
        FetchService service = new FetchServiceImplLight();
        String iContent = service.getUsersSubscribesList(botUsername, botPassword, username);
        return iContent;
    }

    /**
     * Manages retrieval of number of users subscribed by an account
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username of facebook id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/count")
    public String getUserSubscribersCount(@RequestParam(value = "bot_username") String botUsername,
            @RequestParam(value = "bot_password") String botPassword, @RequestParam(value = "username") String username) throws Exception {
        FetchService service = new FetchServiceImplLight();
        String iContent = service.getUsersSubscribersCount(botUsername, botPassword, username);
        return iContent;
    }

}
