/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.leaderank;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kz.alem.semantics.facebook.service.facebookclient.client.FacebookClient;
import kz.alem.semantics.facebook.service.facebookclient.client.impl.FacebookLightClientImpl;
import kz.alem.semantics.facebook.service.leaderank.model.UserNode;

/**
 *
 * @author Zhasan
 */
public class Main {

    public static String bots[] = {"zhassan.baipakbayev","zeynab.heydar"};
    public static String passw[] = {"precious5pro","Onelovemusic1"};
    public static Map<String, Long> leaders = new LinkedHashMap<>();
    public static FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");
    public static final String fileName= "rank.dat";
    
    public static int getDigit(int x, int pos){
        int tenPow = (int)Math.pow(10,pos);
        return (x/tenPow)%10;
    }
    
    
    public static void main(String args[]) throws FileNotFoundException, IOException, ClassNotFoundException {
       
//        final PrintWriter out = new PrintWriter(new File(fileName));
//        
//        for (int i = 0; i < bots.length; i++) {
//
//            final String botUsername = bots[i];
//            final String botPassword = passw[i];
//
//            new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    System.out.println((char)27+"[36mStarting new thread for "+botUsername);
//                    putUsersRank(out,botUsername, botPassword, botUsername, 2);
//                }
//            }).start();
//
//        }
//
//        out.close();
//        
//        Object[] usernames = leaders.keySet().toArray();
//
//        for (int i = 0; i < usernames.length; i++) {
//            long iRank = leaders.get(usernames[i].toString());
//            for (int j = 0; j < usernames.length; j++) {
//                long jRank = leaders.get(usernames[j].toString());
//
//                if (i != j && iRank >= jRank) {
//                    Object temp = usernames[j];
//                    usernames[j] = usernames[i];
//                    usernames[i] = temp;
//                }
//
//            }
//        }
//
//        System.out.println("Here is the rank of users by their subscribers: ");
//
//        for (Object username : usernames) {
//            System.out.println(" " + username.toString() + " -> " + leaders.get(username.toString()));
//        }

    }

    public static void putUsersRank(PrintWriter out,String botUsername, String botPassword, String username, int depth) {
        List<String> frList = null;

        if (botUsername.equals(username)) {
            frList = client.getFriendsList(botUsername, botPassword);
        } else {
            frList = client.getProfileFriendsList(botUsername, botPassword, username);
        }

        for (String fr : frList) {
            
             out.println(fr+"#"+client.getUsersSubscribersCount(botUsername, botPassword, fr));
             
            //leaders.put(fr, client.getUsersSubscribersCount(botUsername, botPassword, fr));
        }

        if (depth > 0) {
            for (String fr : frList) {
                putUsersRank(out,botUsername, botPassword, fr, --depth);
            }
        } else {
            return;
        }
    }
}
