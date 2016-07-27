/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.leaderank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import kz.alem.semantics.facebook.service.facebookclient.client.FacebookClient;
import kz.alem.semantics.facebook.service.facebookclient.client.impl.FacebookLightClientImpl;
import kz.alem.semantics.facebook.service.leaderank.config.LeaderankConfig;
import kz.alem.semantics.facebook.service.leaderank.model.UserNode;
import kz.alem.semantics.facebook.service.leaderank.queue.RabbitQueue;
import kz.alem.semantics.facebook.service.leaderank.service.BotService;
import kz.alem.semantics.leaderank.leaderank.orm.dao.LeaderRankDao;
import kz.alem.semantics.leaderank.leaderank.orm.model.LeaderRank;
import kz.alem.semantics.sql.orm.model.Bot;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author root
 */
public class ManageFileAccounts {

    public static final Logger LOG = Logger.getLogger(DynamicLeaderRank.class);
    public static int sleepHour = 1;
    public static FacebookClient client;
    public static RabbitQueue leaderankQueue;
    public static RabbitQueue childQueue;
    public static List<Bot> bots;
    static int depth = 3;
    static LeaderRankDao leaderankDao;
    static BotService botService;
    static final int BATCH_SIZE = 200;
    static final long week = 604800000;

    public static void init() throws IOException, TimeoutException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        ApplicationContext ctx1 = new AnnotationConfigApplicationContext(LeaderankConfig.class);
        client = ctx.getBean("facebookClient", FacebookLightClientImpl.class);
        leaderankQueue = ctx.getBean("leaderankQ", RabbitQueue.class);
        childQueue = ctx.getBean("childQ", RabbitQueue.class);

        leaderankDao = ctx1.getBean(LeaderRankDao.class);
        botService = ctx.getBean(BotService.class);

        bots = botService.getBotList();

    }

    public static void main(String args[]) throws FileNotFoundException, IOException, TimeoutException {
        init();
        File file = new File("/home/zhassan/Documents/NegativePeople.in");
        Scanner in = new Scanner(new BufferedReader(new FileReader(file)));
        int exist = 0;
        int count = 0;
        List<LeaderRank> leaders = new ArrayList<>();
        List<String> other = new ArrayList<>();
        while(in.hasNext()){
            String url = in.next();
            String username = StringUtils.substringBetween(url, "facebook.com/", "?");
            if(url.contains("profile.php")){
                username = StringUtils.substringBetween(url, "id=", "&");
            }
            LeaderRank lr = leaderankDao.find(username);
            if(lr != null){
                exist++;
                leaders.add(lr);
            } else {
                other.add(username);
            }
            count++;
        }
        System.out.println(exist+" accounts exists in leaderank db out of "+count);
        for(String username:other){
            LeaderRank lr = new LeaderRank();
            lr.setUsername(username);
            lr.setCountry("KZ");
            UserNode node = new UserNode();
            for(Bot bot: bots){
                boolean subscribed = client.subscribe(bot.getUsername(), bot.getPassword(), username);
                boolean friended = client.makeFriend(bot.getUsername(), bot.getPassword(), username);
                if(subscribed || friended || bots.indexOf(bot) == bots.size()-1){
                    node.setBot(bot);
                    lr.setStatus(1);
                    break;
                }
                
            }
            node.setUser(lr);
            leaderankQueue.add(node);
        }
        System.out.println("Not existing accounts are processed");
        for(LeaderRank lr:leaders){
            String username=  lr.getUsername();
            for(Bot bot: bots){
                boolean subscribed = client.subscribe(bot.getUsername(), bot.getPassword(), username);
                boolean friended = client.makeFriend(bot.getUsername(), bot.getPassword(), username);
                if(subscribed || friended || bots.indexOf(bot) == bots.size()-1){
                    lr.setStatus(1);
                    lr.setCountry("KZ");
                    lr.setDate(new Date(System.currentTimeMillis()));
                    leaderankDao.update(lr);
                    break;
                }
            }
        }
        System.out.println("existing leaders are updated and subscribed if it was possible");
    }
}
