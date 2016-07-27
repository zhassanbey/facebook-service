/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import kz.alem.semantics.facebook.service.leaderank.model.UserNode;
import kz.alem.semantics.facebook.service.leaderank.queue.RabbitQueue;
import kz.alem.semantics.leaderank.leaderank.orm.model.LeaderRank;
import kz.alem.semantics.sql.orm.model.Bot;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class RabbitQueueIT {
    
    public RabbitQueueIT() {
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
    public void addRemoveTest() throws IOException, TimeoutException, InterruptedException, ClassNotFoundException{
        RabbitQueue rabbitQueue = new RabbitQueue("leaderank","54.72.168.234",5672,"leaderank-service","leaderank-service","nodes");
        UserNode userNode = new UserNode();
        LeaderRank lr = new LeaderRank();
        lr.setCountry("sefsefs");
        lr.setUsername("Dalbaeb");
        userNode.setUser(lr);
        userNode.setBot(new Bot());
        userNode.setDepth(1);
        
        //rabbitQueue.add(userNode);
        UserNode jesus = rabbitQueue.remove();
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
