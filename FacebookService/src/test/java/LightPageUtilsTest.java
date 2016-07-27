/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;
import kz.alem.semantics.facebook.service.facebookservice.controller.util.LightPageUtils;
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
public class LightPageUtilsTest {
    
    public LightPageUtilsTest() {
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
    public void isScreenNameTest(){
        String screenName = "yevgeni1990";
        
        assertTrue(LightPageUtils.isScreenName(screenName));
        
        screenName = "100008444164705";
        
        assertFalse(LightPageUtils.isScreenName(screenName));
        
    }
    
    @Test
    public void mapFormTest() throws FileNotFoundException{
        BufferedReader read = new BufferedReader(new FileReader("test.html"));
        
        Stream<String> lines = read.lines();
        
        Iterator<String> iter = lines.iterator();
        
        String content = "";
        
        while(iter.hasNext()){
        
            content+=iter.next();
            
        }
        
        Map<String, String> map = LightPageUtils.mapForm(content);
        
        
        for(String key:map.keySet()){
        
            System.out.println(key+"=>"+map.get(key));
        
        }
    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
