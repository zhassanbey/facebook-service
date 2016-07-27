/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.leaderank;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import kz.alem.semantics.facebook.service.facebookclient.client.FacebookClient;
import kz.alem.semantics.facebook.service.facebookclient.client.impl.FacebookLightClientImpl;
import kz.alem.semantics.facebook.service.leaderank.config.LeaderankConfig;
import kz.alem.semantics.facebook.service.leaderank.service.BotService;
import kz.alem.semantics.leaderank.leaderank.orm.dao.LeaderRankDao;
import kz.alem.semantics.leaderank.leaderank.orm.model.LeaderRank;
import kz.alem.semantics.linkdb.orm.dao.PageDao;
import kz.alem.semantics.linkdb.orm.dao.impl.PageDaoImpl;
import kz.alem.semantics.linkdb.orm.model.Page;
import kz.alem.semantics.linkdb.orm.model.PageStatus;
import kz.alem.semantics.linkdb.orm.model.PageType;
import kz.alem.semantics.sql.orm.dao.BotDao;
import kz.alem.semantics.sql.orm.dao.impl.BotDaoImpl;
import kz.alem.semantics.sql.orm.model.Bot;
import org.jboss.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author root
 */
@ImportResource("classpath:applicationContext.xml")
public class SubscribeLeaders {

    public static FacebookClient client;
    public static LeaderRankDao leaderankDao;
    public static PageDao pageDao;
    public static BotService botDao;
    public static ApplicationContext ctx;
    public static ApplicationContext leaderankCtx;
    public static final Logger LOG = Logger.getLogger(SubscribeLeaders.class);
    public static final String NEON = (char) 27 + "[36m";
    public static final String PUPLE = (char) 27 + "[35m";
    public static final String GREEN = (char) 27 + "[34m";

    public static void init() {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        leaderankCtx = new AnnotationConfigApplicationContext(LeaderankConfig.class);
        client = ctx.getBean("facebookClient", FacebookLightClientImpl.class);
        pageDao = ctx.getBean("pageDao", PageDaoImpl.class);
        leaderankDao = leaderankCtx.getBean(LeaderRankDao.class);
        botDao = ctx.getBean(BotService.class);
    }

    public static void saveToPage(Bot bot, LeaderRank lr) {
        Page page;
        page = new Page();
        page.setBot(bot);
        page.setCrawledDate(new Date());
        page.setCreatedDate(new Date());
        page.setDomain("facebook.com");
        page.setUrl(lr.getUsername());
        page.setUpdatedDate(new Date());
        page.setType(PageType.PROFILE);
        page.setHash("leader_" + lr.getUsername());
        page.setStatus(PageStatus.NEW);
        page.setLevel(0);
        page.setNextCrawlDate(new Date());
        pageDao.saveOrUpdate(page);
        LOG.info(PUPLE + "leader " + lr.getUsername() + " saved as page");
    }

    public static void main(String args[]) throws FileNotFoundException {
        Scanner in = new Scanner(new File("query.txt"));
        init();
        String sqlQuery = in.nextLine();
        List<LeaderRank> leaders = leaderankDao.get(sqlQuery);
        List<Bot> bots = botDao.getBotList();
        if (bots != null && !bots.isEmpty()) {
            if (leaders != null && !leaders.isEmpty()) {
                for (LeaderRank lr : leaders) {
                    int subsCount = 0, frCount = 0;
                    int randex = (int) (Math.random() * bots.size());
                    Bot bot = bots.get(randex);
                    if (client.subscribe(bot.getUsername(), bot.getPassword(), lr.getUsername())) {
                        subsCount++;
                        LOG.info(NEON + "bot " + bot.getUsername() + " subscibed " + lr.getUsername());
                    } else if (client.makeFriend(bot.getUsername(), bot.getPassword(), lr.getUsername())) {
                        frCount++;
                        LOG.info(GREEN + "bot " + bot.getUsername() + " sent friend request to " + lr.getUsername());
                    }
                    boolean saved = false;
                    if (subsCount > 0 || frCount > 0) {
                        lr.setStatus(1);
                        leaderankDao.update(lr);
                        saveToPage(bot, lr);
                        saved = true;
                    }
                    for (int i = 0; i < 2; i++) {
                        randex = (int) (Math.random() * bots.size());
                        bot = bots.get(randex);
                        if (client.subscribe(bot.getUsername(), bot.getPassword(), lr.getUsername())) {
                            subsCount++;
                            LOG.info(NEON + "bot " + bot.getUsername() + " also subscibed " + lr.getUsername());
                            if (!saved) {
                                lr.setStatus(1);
                                leaderankDao.update(lr);
                                saveToPage(bot, lr);
                                saved = true;
                            }
                        } else if (client.makeFriend(bot.getUsername(), bot.getPassword(), lr.getUsername())) {
                            frCount++;
                            LOG.info(GREEN + "bot " + bot.getUsername() + " also sent friend request to " + lr.getUsername());
                            if (!saved) {
                                lr.setStatus(1);
                                leaderankDao.update(lr);
                                saveToPage(bot, lr);
                                saved = true;
                            }
                        }
                    }
                    if (!saved) {
                        lr.setStatus(1);
                        leaderankDao.update(lr);
                        saveToPage(bot, lr);
                        saved = true;
                    }
                }

            }
        }
    }

}
