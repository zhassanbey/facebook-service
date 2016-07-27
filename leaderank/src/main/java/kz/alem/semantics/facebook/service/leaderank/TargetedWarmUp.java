/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.leaderank;

import java.util.List;
import kz.alem.semantics.facebook.service.facebookclient.client.FacebookClient;
import kz.alem.semantics.facebook.service.facebookclient.client.impl.FacebookLightClientImpl;
import org.jboss.logging.Logger;

/**
 *
 * @author root
 */
public class TargetedWarmUp {

    public static String username = "alemasel";
    public static String password = "Soul1502!";
    public static String fbUrl = "http://54.154.60.220:8092";
    public static FacebookClient client;
    public static String usernames[] = {"adelyaalem", "abaybolov"};
    public static String passwords[] = {"leavemealone", "Soul1501!"};
    public static final Logger logger = Logger.getLogger(TargetedWarmUp.class);
    public static void init() {
        client = new FacebookLightClientImpl(fbUrl);
    }

    public static void main(String args[]) {
        init();
        for (int i = 0; i < usernames.length; i++) {
            String pName = usernames[i];
            String pPass = passwords[i];

            List<String> frList = client.getFriendsList(pName, pPass);
            List<String> sbList = client.getSubscribesList(pName, pPass);
            List<String> grList = client.getGroupsList(pName, pPass);
            logger.info("Processing friends of "+pName+" ...");
            for (String frName : frList) {
                if (!client.makeFriend(username, password, frName)) {
                    logger.info("MakeFriend request was unsuccessful, trying to subscribe "+frName+" ...");
                    client.subscribe(username, password, frName);
                }
            }
            logger.info("Processed friends of "+pName+".");
            logger.info("Processing subscriptions of "+pName+"....");
            for(String sName:sbList){
                if(!client.makeFriend(username, password, sName)){
                    logger.info("MakeFriend request was unsuccessful, trying to subscribe "+sName+" ...");
                    client.subscribe(username, password, sName);
                }
            }
            logger.info("Processed subscriptions of "+pName);
        }
    }

}
