/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.alem.semantics.crawler.storm.client.util;

import kz.alem.semantics.facebook.service.facebookclient.util.URLCreator;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Zhasan
 */
public class URLCreatorTest {
    
    public URLCreatorTest() {
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
    public void getUrlTest(){
        String baseAddress = "http://m.facebook.com/test";
        
        URLCreator creator = new URLCreator(baseAddress);
        creator.addParameter("q1", "test");
        creator.addParameter("q2", "1");
        creator.addParameter("q3","true");
        
        assertEquals(creator.getUrl(),baseAddress+"?q1=test&q2=1&q3=true");
        
        
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
