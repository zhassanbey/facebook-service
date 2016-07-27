/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.leaderank;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import kz.alem.semantics.facebook.service.facebookclient.client.FacebookClient;
import kz.alem.semantics.facebook.service.facebookclient.client.impl.FacebookLightClientImpl;
import kz.alem.semantics.facebook.service.leaderank.service.BotService;
import kz.alem.semantics.sql.orm.model.Bot;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Zhasan
 */
@ImportResource("classpath:applicationContext.xml")
public class BotWarmUp {

    public static final Logger LOG = Logger.getLogger(BotWarmUp.class.getName());
    
    static final String RED = (char) 27 + "[31m", PINK = (char) 27 + "[33m", BLUE = (char) 27 + "[34m", PURPLE = (char) 27 + "[36m", WHITE = (char) 27 + "[37m";

    static FacebookClient facebookClient;

    static BotService botService;

    static List<Bot> botList;

    public static void init() {
        LOG.info("Initializing...");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        facebookClient = ctx.getBean("facebookClient", FacebookLightClientImpl.class);
        botService = ctx.getBean(BotService.class);

        botList = botService.getBotList();
    }

    public static List<Bot> getRandom(int num) {
        if (num < botList.size()) {
            List<Bot> random = new ArrayList<>();

            for (int i = 0; i < num; i++) {
                int rand = (int) Math.random() * botList.size();

                random.add(botList.get(rand));
            }

            return random;
        } else {
            return botList;
        }
    }

    public static List<Bot> getNewBots() throws FileNotFoundException {

        List<Bot> result = new ArrayList<>();

        Scanner in = new Scanner(new File("bots.txt"));

        while (in.hasNextLine()) {
            String tokens[] = in.nextLine().split(" ");

            String username = tokens[0].trim();
            String password = tokens[1].trim();
            String email = tokens[2].trim();

            Bot bot = new Bot();
            bot.setDomain("facebook.com");
            bot.setLogin(email);
            bot.setUsername(username);
            bot.setPassword(password);

            result.add(bot);

        }

        return result;

    }

    public static int repost(Bot bot, List<String> urls) {
        int count = 0;

        LOG.info(PINK + "reposting posts....");
        for (String url : urls) {
            if (facebookClient.share(bot.getUsername(), bot.getPassword(), url)) {
                count++;
            } else {
                count++;
            }
        }

        return count;
    }

    public static int like(Bot bot, List<String> urls) {
        int count = 0;

        LOG.info(PINK + "liking posts...");
        for (String url : urls) {
            if (facebookClient.like(bot.getUsername(), bot.getPassword(), url)) {
                count++;
            }
        }

        return count;
    }

    public static int processFriend(Bot bot, List<String> fr) {
        int count = 0;

        LOG.info(PINK + "processing friends...");
        for (String frUrl : fr) {
            if (facebookClient.makeFriend(bot.getUsername(), bot.getPassword(), frUrl)) {
                count++;
            } else if (facebookClient.subscribe(bot.getUsername(), bot.getPassword(), frUrl)) {
                count++;
            }
        }
        LOG.info(PINK + "processed " + count + " friends");

        return count;
    }

    public static int processSubscribers(Bot bot, List<String> subList) {
        int count = 0;

        LOG.info(PINK + "processing subscribers...");
        for (String subUrl : subList) {
            if (facebookClient.subscribe(bot.getUsername(), bot.getPassword(), subUrl)) {
                count++;
            }
        }
        LOG.info(PINK + "processed " + count + " subscribers");

        return count;
    }

    public static List<String> getSomeRandom(List<String> arr, int count, boolean remove) {
        List<String> result = new ArrayList<>();

        if (arr.size() > 0) {
            for (int i = 0; i < count; i++) {
                int index = (int) (Math.random() * arr.size());

                LOG.info("* "+arr.get(index));
                        
                result.add(arr.get(index));
                
                if (remove) {
                    arr.remove(index);
                }
            }
        }

        return result;
    }

    public static void main(String arsg[]) throws FileNotFoundException {

        init();

        for (Bot bot : getNewBots()) {

            List<Bot> etalons = getRandom(2);

            int frCount = 0;
            int subCount = 0;
            int lkCount = 0;
            int shareCount = 0;

            List<String> frList = new ArrayList<>();
            List<String> subList = new ArrayList<>();

            for (Bot eBot : etalons) {

                LOG.info(BLUE + "getting friend list of " + eBot.getUsername() + " ...");
                frList.addAll(facebookClient.getFriendsList(eBot.getUsername(), eBot.getPassword()));
                LOG.info(BLUE + "got friend list of " + eBot.getUsername());

                LOG.info(PURPLE + "getting subscribers list of " + eBot.getUsername() + " ...");
                subList.addAll(facebookClient.getSubscribesList(eBot.getUsername(), eBot.getPassword()));
                LOG.info(PURPLE + "got subscribers list of " + eBot.getUsername());
            }

            int round = 0;
            while (!(frList.isEmpty() && subList.isEmpty())) {
                LOG.info("Preparing like urls...");
                List<String> toLike = new ArrayList<>();
                List<String> toRepost = new ArrayList<>();
                for (String username : getSomeRandom(frList, 3, false)) {
                    List<String> urls = facebookClient.getProfileFeedLikeUrls(bot.getUsername(), bot.getPassword(), username);
                    toLike.addAll(getSomeRandom(urls, 5, false));
                }

                for (String username : getSomeRandom(subList, 4, false)) {
                    List<String> urls = facebookClient.getProfileFeedLikeUrls(bot.getUsername(), bot.getPassword(), username);
                    toLike.addAll(getSomeRandom(urls, 5, false));
                }
                LOG.info("Prepared like urls!");

                LOG.info("Preparing repost urls..");

                for (String username : getSomeRandom(frList, 3, false)) {
                    List<String> urls = facebookClient.getProfileShareUrls(bot.getUsername(), bot.getPassword(), username);
                    toRepost.addAll(urls);
                }

                for (String username : getSomeRandom(subList, 4, false)) {
                    List<String> urls = facebookClient.getProfileShareUrls(bot.getUsername(), bot.getPassword(), username);
                    toRepost.addAll(urls);
                }

                LOG.info("Prepared repost urls!");

                toRepost.addAll(getSomeRandom(subList, 3, false));

                List<String> toSubscribe = getSomeRandom(subList, 10, true);

                List<String> toMakeFriend = getSomeRandom(frList, 10, true);

                int fc = processFriend(bot, toMakeFriend);
                frCount += fc;

                int sc = processSubscribers(bot, toSubscribe);
                subCount += sc;

                int lk = like(bot, toLike);
                lkCount += lk;

                int sk = repost(bot, toRepost);
                shareCount += sk;

                ++round;

                if (fc + sc + lk + sk == 0) {
                    LOG.info("this round bot " + bot.getUsername() + " failed round " + round);
                } else {
                    LOG.info("" + bot.getUsername() + " finished round " + round);
                }

                LOG.info(" *I have sent *" + fc + " friend requests \n *Subscribed " + sc + " people \n *Liked " + lk + " posts \n *Shared " + sk + " posts. \n Therefore I am so tired and going to sleep for 3 hours..");

                sleep(3);

            }

            if (frCount + subCount + shareCount + lkCount > 0) {
                botService.saveBot(bot);
                LOG.info("New bot " + bot.getUsername() + " is almost ready for use:=)");
            }
        }

    }

    public static void sleep(int hours) {
        try {
            Thread.sleep(hours * 3600 * 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(BotWarmUp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
