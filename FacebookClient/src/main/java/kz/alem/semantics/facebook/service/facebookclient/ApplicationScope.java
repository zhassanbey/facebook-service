/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookclient;

import kz.alem.semantics.facebook.service.facebookclient.constants.ConfigurationConstants;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Singleton class containing spring application context
 *
 * @author Zhasan
 */
public class ApplicationScope {

    public static ApplicationContext ctx = new ClassPathXmlApplicationContext(ConfigurationConstants.APPLICATION_CONTEXT);

    private ApplicationScope() {

    }

    private static class Holder {

        private static ApplicationScope instance = new ApplicationScope();
    }

    public ApplicationScope getInstance() {
        return Holder.instance;
    }
}
