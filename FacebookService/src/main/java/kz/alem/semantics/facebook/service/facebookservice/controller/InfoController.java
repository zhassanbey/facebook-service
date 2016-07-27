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
 * This controller manages general information about an account
 *
 * @author Zhassan
 */
@RestController
@RequestMapping("/info")
public class InfoController {

    /**
     * Manages the retrieval of information about an account
     *
     * @param botUsername - username of the current bot
     * @param botPassword - password of the current bot
     * @param username - unique identifier of the targeted account. Either
     * username of facebook id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "")
    public String getInfo(@RequestParam(value = "bot_username") String botUsername, @RequestParam(value = "bot_password") String botPassword, @RequestParam(value = "username") String username) throws Exception {
        FetchService service = new FetchServiceImplLight();
        String iContent = service.getUsersInfo(botUsername, botPassword, username);
        return iContent;
    }
    
}
