/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import kz.alem.semantics.facebook.service.leaderank.model.UserNode;
import kz.alem.semantics.leaderank.leaderank.orm.model.LeaderRank;
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
@Ignore
public class UserNodeTest {
    
    public UserNodeTest() {
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
    public void userNodeTest() throws IOException, ClassNotFoundException{
        UserNode node = new UserNode();
        LeaderRank rank = new LeaderRank();
        rank.setDomain("osuiefosf");
        rank.setRank(1);
        rank.setUsername("username");
        node.setBot(null);
        node.setDepth(1);
        node.setUser(rank);
        
        byte[] arr = node.toByteArray();
        
        UserNode recovery = UserNode.fromBytes(arr);
        
        
        System.out.println("Yupi "+recovery.getUser().getUsername());
        
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
