/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.alem.semantics.crawler.storm.client.util;

import kz.alem.semantics.facebook.service.facebookclient.util.StringUtils;
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
public class StringUtilsTest {
    
    public StringUtilsTest() {
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
    public void getStringBetweenTest(){
        String str = "#sefsefsefsefsefsefs%";
        assertEquals(StringUtils.getStringBtw(str, "#", 0, "%", 0),"#sefsefsefsefsefsefs");
        assertEquals(StringUtils.getStringBtw(str, "#", 1, "%", 0),"sefsefsefsefsefsefs");
        assertEquals(StringUtils.getStringBtw(str, "#", 2, "%", -1),"efsefsefsefsefsef");
    
    }
    
    
    @Test
    public void getTextBetweenTest(){
        String text = "<p><span>Toptal introduces this community-drive</span>" +
"<wbr />" +
"<span class=\"word_break\"></span>n list of great interview questions for Unity3D. We've begun with an initial sampling of questions. Read them, comment on them, or even contribute your own.Toptal is pleased to provide this service to the community and welcomes your feedback." +
"<a class=\"_5ayv\" href=\"/hashtag/unity3d?refid=52&amp;_ft_=qid.6198448020125227854%3Amf_story_key.-2695866129182567245%3Aei.6198448020844385114.6030517593035.19.0%3AeligibleForSeeFirstBumping.&amp;__tn__=%2As\">" +
"    <span class=\"_5aw4\">?#?</span>" +
"    <span class=\"_5ayu\">Unity3D?</span>" +
"</a>" +
"<a class=\"_5ayv\" href=\"/hashtag/hiring?refid=52&amp;_ft_=qid.6198448020125227854%3Amf_story_key.-2695866129182567245%3Aei.6198448020844385114.6030517593035.19.0%3AeligibleForSeeFirstBumping.&amp;__tn__=%2As\">" +
"    <span class=\"_5aw4\">?#?</span>" +
"    <span class=\"_5ayu\">Hiring?</span>" +
"</a>\n" +
"<a class=\"_5ayv\" href=\"/hashtag/interview?refid=52&amp;_ft_=qid.6198448020125227854%3Amf_story_key.-2695866129182567245%3Aei.6198448020844385114.6030517593035.19.0%3AeligibleForSeeFirstBumping.&amp;__tn__=%2As\">" +
"    <span class=\"_5aw4\">?#?</span>" +
"    <span class=\"_5ayu\">Interview?</span>" +
"</a>" +
"<a class=\"_5ayv\" href=\"/hashtag/questions?refid=52&amp;_ft_=qid.6198448020125227854%3Amf_story_key.-2695866129182567245%3Aei.6198448020844385114.6030517593035.19.0%3AeligibleForSeeFirstBumping.&amp;__tn__=%2As\">" +
"    <span class=\"_5aw4\">?#?</span>" +
"    <span class=\"_5ayu\">Questions?</span>" +
"</a></p>";
        
        text = "\n" +
" 2-ой день KFW SS-2016\n" +
"";
        
        String raw = StringUtils.getTextBtw(text, ">", 1, "<", 0);
        
        System.out.println(raw);
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
