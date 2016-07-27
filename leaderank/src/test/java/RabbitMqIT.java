/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import kz.alem.semantics.facebook.service.leaderank.model.UserNode;
import kz.alem.semantics.facebook.service.leaderank.queue.RabbitQueue;
import kz.alem.semantics.leaderank.leaderank.orm.model.LeaderRank;
import kz.alem.semantics.sql.orm.model.Bot;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Zhasan
 */
public class RabbitMqIT {

    public RabbitMqIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void rabbitTest() throws IOException, TimeoutException, InterruptedException, ClassNotFoundException {
        RabbitQueue queue = new RabbitQueue("leaderank", "54.72.168.234", 5672,"leaderank-service","leaderank-service","nodes");

        UserNode node = new UserNode();
        LeaderRank rank = new LeaderRank();
        rank.setUsername("zhassan");
        rank.setDomain("facebook.com");
        rank.setRank(3);

        node.setUser(rank);

        Bot bot = new Bot();
        bot.setDomain("facebook.com");
        bot.setLogin("bot1");
        node.setBot(bot);
        node.setDepth(2);

        queue.add(node);

        UserNode node1 = new UserNode();

        LeaderRank rank1 = new LeaderRank();
        rank1.setDomain("vk.com");
        rank1.setRank(2);
        rank1.setUsername("zeynab");

        node1.setBot(bot);
        node1.setUser(rank1);
        node1.setDepth(2);

        queue.add(node1);
        
        UserNode jesus = queue.remove();
        jesus = queue.remove();
        
        System.out.println("jesus="+jesus);

    }

    @Test
    public void readMessagesTest() throws IOException, TimeoutException, InterruptedException, ClassNotFoundException {
        RabbitQueue queue = new RabbitQueue("leaderank", "54.72.168.234", 5672,"leaderank-service","leaderank-service","nodes");
        UserNode jesus1 = queue.remove();
        UserNode jesus2 = queue.remove();

        System.out.println(jesus1.toString());
//        System.out.println(jesus2.toString());
    }
    
    @Test
    public void emptyTest() throws IOException, TimeoutException, InterruptedException, ClassNotFoundException{
        RabbitQueue queue = new RabbitQueue("leaderank", "54.72.168.234", 5672,"leaderank-service","leaderank-service","nodes");
        boolean x = queue.empty();
        
        assertFalse(x);
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
