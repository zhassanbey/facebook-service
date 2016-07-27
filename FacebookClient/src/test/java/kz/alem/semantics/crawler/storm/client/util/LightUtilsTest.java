package kz.alem.semantics.crawler.storm.client.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import kz.alem.semantics.facebook.service.facebookclient.util.LightUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Zhasan
 */
public class LightUtilsTest {
    
    public LightUtilsTest() {
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
    public void normalizeProfileUrlTest(){
        String profileUrl = "https://www.facebook.com/profile.php?id=100002245213610&fref=nf&refid=52&_ft_=top_level_post_id.969992983085533%3Atl_objid.969992983085533%3Athid.100002245213610&__tn__=C";
        assertEquals(LightUtils.normalizeProfileUrl(profileUrl),"https://www.facebook.com/profile.php?id=100002245213610");
        profileUrl = "https://www.facebook.com/profile.php?id=100004353882238&fref=nf&refid=52&_ft_=qid.6241778820240999833%3Amf_story_key.8382586606513356756&__tn__=C";
        assertEquals(LightUtils.normalizeProfileUrl(profileUrl),"https://www.facebook.com/profile.php?id=100004353882238");
        profileUrl = "https://www.facebook.com/profile.php?id=1293819849238928";
        assertEquals(LightUtils.normalizeProfileUrl(profileUrl),profileUrl);
        
    }
    
    @Test
    public void normalizeUrlTest(){
        String url = "https://m.facebook.com/story.php?story_fbid=880106698706112&id=100001203705599&refid=17&_ft_=top_level_post_id.880106698706112%3Atl_objid.880106698706112%3Athid.100001203705599%3A306061129499414%3A3%3A0%3A1446361199%3A951670867030854240#footer_action_list";
        
        String normalized = LightUtils.normalizeUrl(url);
        
        System.out.println(normalized);
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
