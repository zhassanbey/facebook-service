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
 * This controller manages feed of the current bot user
 *
 * @author Zhasan
 */
@RestController
@RequestMapping("/users")
public class FeedController {

    /**
     * Method manages retrieval of the current bot's news feed
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/feed")
    public String getFeed(@RequestParam(value = "bot_username") String botUsername,
            @RequestParam(value = "bot_password") String botPassword) throws Exception {
        FetchService service = new FetchServiceImplLight();
        String content = service.getUsersFeedUrls(botUsername, botPassword);
        return content;
    }

}
