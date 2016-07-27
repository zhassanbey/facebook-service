/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.rankservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.stream.Stream;
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
import org.json.simple.parser.JSONParser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author root
 */
@ImportResource("classpath:applicationContext.xml")
public class Main {

    public static final Logger LOG = Logger.getLogger(Main.class);

    public static int sleepHour = 1;

    public static FacebookClient client;

    public static RabbitQueue leaderankQueue;
    public static RabbitQueue childQueue;

    public static List<Bot> bots;

    static LeaderRankDao leaderankDao;
    static BotService botService;

    static final int BATCH_SIZE = 200;

    public static void init() throws IOException, TimeoutException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        ApplicationContext ctx1 = new AnnotationConfigApplicationContext(LeaderankConfig.class);
        client = ctx.getBean("facebookClient", FacebookLightClientImpl.class);
        leaderankQueue = ctx.getBean("leaderankQ", RabbitQueue.class);
        childQueue = ctx.getBean("childQ", RabbitQueue.class);
        botService = ctx.getBean(BotService.class);
    }


    public static void addChildren(UserNode node) throws IOException, TimeoutException {
        if (node.getDepth() > 0) {
            List<String> frList;
            LeaderRank lr = node.getUser();
            Bot bot = node.getBot();
            if (bot.getUsername().equals(lr.getUsername())) {
                frList = client.getFriendsList(bot.getUsername(), bot.getPassword());
                frList.addAll(client.getSubscribesList(bot.getUsername(), bot.getPassword()));
            } else {
                frList = client.getProfileFriendsList(bot.getUsername(), bot.getPassword(), lr.getUsername());
            }
            if (frList != null && !frList.isEmpty()) {
                for (String fr : frList) {
                    LeaderRank flr = new LeaderRank();
                    flr.setUsername(fr);
                    flr.setDomain("facebook.com");
                    flr.setRank(0);
                    UserNode child = new UserNode();
                    child.setUser(flr);
                    child.setBot(bot);
                    child.setDepth(node.getDepth() - 1);
                    leaderankQueue.add(child);
                }
            }
            LOG.info((char) 27 + "[37mAdded " + frList.size() + " childs of+" + lr.getUsername() + " into "+leaderankQueue.getName());
        } else {
            LOG.info((char) 27 + "[37mLast level child");
        }
    }

    public static void addChildrenFromFile(File file) throws FileNotFoundException, UnsupportedEncodingException, IOException, TimeoutException{
        if (file.exists()) {
//            File [] ls = file.getParentFile().listFiles();
//            for(File f:ls){
//                System.out.println(f.getName());
//            }
            bots = botService.getBotList();
            BufferedReader read = new BufferedReader(new FileReader(file));
            JSONParser parser = new JSONParser();
            long i = 0;
            final StringBuilder out = new StringBuilder();
            Stream<String> inp  = read.lines();
            inp.forEach(new Consumer<String>() {

                @Override
                public void accept(String t) {
                  out.append(t);
                }
            });
            read.close();
            String content = out.toString();
            String facebookId = "facebook.com";
            String arr[] = StringUtils.substringsBetween(content, facebookId, "\"");
            for(String a:arr){
                String subUrl = URLDecoder.decode(a,"UTF8");
                String username = StringUtils.substringAfterLast(subUrl, "/");
                UserNode node = new UserNode();
                LeaderRank user = new LeaderRank();
                user.setDomain("facebook.com");
                user.setStatus(0);
                user.setUsername(username);
                node.setBot(bots.get((int)(Math.random()*bots.size())));
                node.setUser(user);
                node.setDepth(2);
                addChildren(node);
            }
        }
    }
    
    public static void process() throws IOException, TimeoutException, InterruptedException, ClassNotFoundException {
        UserNode node;
        while ((node = childQueue.remove()) != null) {
            addChildren(node);
        }

    }

    public static void main(String args[]) throws IOException, TimeoutException, InterruptedException, ClassNotFoundException {

        init();
//        File file = new File("/home/zhassan/Downloads/Facebook.txt");
//        addChildrenFromFile(file);
        try {
            while (true) {
                process();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
