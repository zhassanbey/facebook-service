/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.leaderank;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import kz.alem.semantics.facebook.service.facebookclient.client.FacebookClient;
import kz.alem.semantics.facebook.service.facebookclient.client.impl.FacebookLightClientImpl;
import kz.alem.semantics.facebook.service.leaderank.config.LeaderankConfig;
import kz.alem.semantics.facebook.service.leaderank.model.UserNode;
import kz.alem.semantics.facebook.service.leaderank.queue.RabbitQueue;
import kz.alem.semantics.facebook.service.leaderank.service.BotService;
import kz.alem.semantics.leaderank.leaderank.orm.dao.LeaderRankDao;
import kz.alem.semantics.leaderank.leaderank.orm.model.LeaderRank;
import kz.alem.semantics.sql.orm.model.Bot;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Zhasan
 */
@ImportResource("classpath:applicationContext.xml")
public class DynamicLeaderRank {

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

    public static void introduceBots() throws IOException, TimeoutException {
        List<Bot> botList = botService.getBotList();

        for (Bot bot : botList) {
            LeaderRank lr = new LeaderRank();

            lr.setUsername(bot.getUsername());
            lr.setDomain("facebook.com");
            lr.setRank(0);

            UserNode node = new UserNode();
            node.setUser(lr);
            node.setBot(bot);
            node.setDepth(depth);

            node.toByteArray();
            childQueue.add(node);
        }

    }

    public static boolean doesExist(LeaderRank lr) {
        return leaderankDao.find(lr.getUsername()) != null;
    }

    public static LeaderRank detach(LeaderRank lr) {
        return leaderankDao.find(lr.getUsername());
    }

    public static boolean isBot(String username) {
        for (Bot bot : bots) {
            if (bot.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void basicProcessBody(UserNode node) throws IOException, TimeoutException {
        LeaderRank lr = node.getUser();
        Bot bot = node.getBot();
        int rank = 20;
        try {
            rank = (int) client.getUsersSubscribersCount(bot.getUsername(), bot.getPassword(), lr.getUsername());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        LeaderRank saved = detach(lr);
        if (saved == null) {

            LOG.info("bot.username = " + bot.getUsername() + " lr.username = " + lr.getUsername());

            if (!isBot(lr.getUsername())) {
                lr.setRank(rank);
                lr.setDate(new Date(System.currentTimeMillis()));
                boolean isLocal = isLocal(lr, bot);
                if (!isLocal) {
                    node.setDepth(0);
                }
                LOG.info("saving " + lr.getUsername() + " country = " + lr.getCountry());
                leaderankDao.create(lr);
            }
            try {
                childQueue.add(node);
            } catch (TimeoutException ex) {
                java.util.logging.Logger.getLogger(DynamicLeaderRank.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (isOld(saved)) {
                LOG.info("Processing old account saved at " + saved.getDate());
                saved.setDate(new Date(System.currentTimeMillis()));
                if (rank > saved.getRank()) {
                    saved.setRank(rank);
                }
                boolean isLocal = isLocal(saved, bot);
                if (!isLocal) {
                    node.setDepth(0);
                }
                try {
                    LOG.info("updating " + saved.getUsername() + " country = " + saved.getCountry());
                    leaderankDao.update(saved);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                childQueue.add(node);
            }
        }
    }

    public static void process() throws IOException, TimeoutException, InterruptedException, ClassNotFoundException {
        LOG.info("Leaderank Queue size = " + leaderankQueue.getSize());
        UserNode node;
        while ((node = leaderankQueue.remove()) != null) {
            basicProcessBody(node);

        }
    }

    public static boolean isOld(LeaderRank lr) {
        long mill = System.currentTimeMillis();
        if (mill - lr.getDate().getTime() >= week) {
            return true;
        }
        return false;
    }

    public static void processAsynch() throws IOException {
        final Channel channel = leaderankQueue.getCahnnel();
        channel.basicQos(500);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    UserNode node = UserNode.fromBytes(body);
                    basicProcessBody(node);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (Exception ex) {
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                    java.util.logging.Logger.getLogger(DynamicLeaderRank.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        channel.basicConsume(leaderankQueue.getName(), false, consumer);
    }

    public static boolean isLocal(LeaderRank lr, Bot bot) {
        String country = client.getLivingCountry(bot.getUsername(), bot.getPassword(), lr.getUsername());
        if (country != null) {
            if (country.equals("KZ")) {
                lr.setCountry("KZ");
                return true;
            } else {
                lr.setLocation(country);
                return false;
            }
        } else {
            LOG.info((char) 27 + "[36m country for user " + lr.getUsername() + " is just null");
        }

        return false;
    }

    public static void estimateLocation(Bot bot, int treshold) {
        LOG.info("Estimating unknown locations....");
        List<LeaderRank> lrList = leaderankDao.get("select * from leaderank where domain='facebook.com' and country is null");
       
//        int r = lrList.size();
//        int l = 0;
//        while (l + 1 != r) {
//            int m = (r + l) / 2;
//            if (lrList.get(m).getRank() >= treshold) {
//                l = m;
//            } else {
//                r = m;
//            }
//        }
        for (LeaderRank lr : lrList) {
            if (lr.getCountry() == null || lr.getCountry().isEmpty()) {
                List<String> frList = null;
                int i = 0;

                frList = client.getProfileFriendsList(bot.getUsername(), bot.getPassword(), lr.getUsername());

                if (frList != null && !frList.isEmpty()) {
                    double localCount = 0D;
                    for (String fr : frList) {
                        String country = client.getLivingCountry(bot.getUsername(), bot.getPassword(), fr);

                        if (country != null && country.equals("KZ")) {
                            localCount++;
                        }
                    }
                   
                    if (localCount >= frList.size() / 3) {
                        lr.setCountry("KZ");
                        leaderankDao.update(lr);
                    }
                }

            }
            try {
                Thread.sleep((long) (Math.random()*1000));
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(DynamicLeaderRank.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        LOG.info("location estimation is over!");
    }

    
    public static void main(String argsp[]) throws IOException, TimeoutException, InterruptedException, ClassNotFoundException {
        init();
        if (argsp[0].equals("rank")) {
            if (leaderankQueue.empty()) {
                introduceBots();
            } else {
                LOG.info("Queue is not empty :=)");
            }
            try {
                int trials = 0;
                processAsynch();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Bot bot = new Bot();
            bot.setDomain("facebook.com");
            bot.setUsername("zhassan.baipakbayev");
            bot.setPassword("precious5PRO");
            estimateLocation(bot, 20);
        }
//        estimateLocation(10);
    }

}
