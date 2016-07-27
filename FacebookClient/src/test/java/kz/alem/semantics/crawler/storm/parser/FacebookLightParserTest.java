/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.crawler.storm.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kz.alem.semantics.facebook.service.facebookclient.parser.FacebookLightParser;
import kz.alem.semantics.parse.model.Article;
import kz.alem.semantics.parse.model.Comment;
import kz.alem.semantics.parse.model.PageData;
import kz.alem.semantics.solr.client.model.Attachment;
import kz.alem.semantics.solr.client.model.Profile;
import org.apache.commons.lang.SystemUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
public class FacebookLightParserTest {

    FacebookLightParser parser;
    String pathToProject;

    public FacebookLightParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        parser = new FacebookLightParser();
        pathToProject = getAbsolutePath();
    }

    @After
    public void tearDown() {
    }

    private String getAbsolutePath() {
        File file = new File("");
        return file.getAbsolutePath();
    }

    private String read(String fileName) {
        String path = pathToProject + "\\src\\test\\" + fileName;
        
        if(SystemUtils.IS_OS_LINUX){
            path = pathToProject+"/src/test/"+fileName;
        }
        
        StringBuilder builder = new StringBuilder();
        try {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FacebookLightParserTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FacebookLightParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        return builder.toString();
    }
    
    @Test
    public void parseCountryTest(){
        Document doc = Jsoup.parse(read("Info.html"));
        String x = parser.parseCountry(doc);
        assertEquals(x,"KZ");
    }
    
    @Test
    public void parseFeedUrlsTest() {
        String content = "<html><a href=\"m.facebook.com/spefsefopksef#footer_action_list\">Click me</a> <a href=\"m.facebook.com/spefsefopksef#footer_action_list\">Click me</a> <a href=\"m.facebook.com/spefsefopksef#footer_action_list\">Click me</a></html>";
        List<String> urls = parser.parsePostUrls(Jsoup.parse(content));

        assertNotNull(urls);
        assertTrue(urls.size() > 0);
    }

    @Test
    public void parseShareUrlsTest() {
        Document doc = Jsoup.parse(read("UsersFriendList.html"));

        List<String> urls = parser.parseFriendList(doc);

        assertNotNull(urls);
        assertEquals(urls.size(), 22);
        assertTrue(urls.contains("100001473655793"));
    }

    @Test
    public void parseLikeUrlsTest() {
        Document doc = Jsoup.parse(read("ProfileFeed.html"));
        List<String> likeUrls = parser.parseLikeUrls(doc);
        assertNotNull(likeUrls);
        assertEquals(likeUrls.size(), 5);
        assertTrue(likeUrls.get(0).startsWith("a/like.php"));
    }

    @Test
    public void parsePostUrls() {
        Document doc = Jsoup.parse(read("UsersFeed.html"));
        List<String> feeds = parser.parsePostUrls(doc);
        assertEquals(feeds.size(),8);
        doc = Jsoup.parse(read("ProfileFeed.html"));
        feeds = parser.parsePostUrls(doc);
        assertEquals(feeds.size(),5);
    }

    @Test
    public void parseAlbumPhotoesTest() {
        Document doc = Jsoup.parse(read("AlbumPhoto.html"));
        
        PageData p = new PageData("test_url");
        
        p = parser.parseAlbumPhotos(p, doc);
        
        assertTrue(p.getPublished().startsWith("2010-12-20T"));
        Article a = p.getArticle();
        
        assertEquals(a.getText(),"Мои фото");
        assertTrue(a.getDate().startsWith("2010-12-20T"));
        
        List<Attachment> atts = p.getAttachmentList();
        assertNotNull(atts);
        assertEquals(atts.size(),5);
        assertEquals(p.getUrl(),"test_url");
    }

    @Test
    public void parsePhotoTest() {
        Document doc = Jsoup.parse(read("Photo.html"));
        PageData p = new PageData("test_url");
        
        p = parser.parsePhoto(p, doc);
        assertTrue(p.getPublished().startsWith("2010-12-17T"));
        Article a = p.getArticle();
        assertTrue(a.getDate().startsWith("2010-12-17T"));
        List<Attachment> atts = p.getAttachmentList();
        assertNotNull(atts);
        
        assertEquals(atts.size(),6);
        
        Profile pr = p.getProfile();
        assertNotNull(pr);
        assertEquals(pr.getScreenName(),"kairat.kelimbetov");
        assertEquals(pr.getName(),"Кайрат Келимбетов");
        
        assertEquals(pr.getUrl(),"https://www.facebook.com/kairat.kelimbetov");
        assertEquals(p.getUrl(),"test_url");
    }

    @Test
    public void parsePostTest() {
        Document doc = Jsoup.parse(read("SimplePost.html"));
        PageData p = new PageData("test_url");
        
        p = parser.parsePost(p, doc);

        System.out.println("published = "+p.getPublished());
        
        assertTrue(p.getPublished().startsWith("2015-11-26T"));
        
        Article a = p.getArticle();
        
        assertEquals(a.getAuthor(),"Сергей Хегай");
                
        assertTrue(a.getText().startsWith("Ассалам алейкум помогите распространить"));
        assertTrue(a.getDate().startsWith("2015-11-26T"));
        
        
        List<Attachment> atts = p.getAttachmentList();
        
        assertEquals(atts.size(),4);
        
        Profile pr = p.getProfile();
        
        assertEquals(pr.getName(),a.getAuthor());
        assertEquals(pr.getUrl(),"https://www.facebook.com/profile.php?id=100001473655793&fref=nf&refid=52&_ft_=top_level_post_id.1009965405729238%3Atl_objid.1009965405729238%3Athid.100001473655793&__tn__=C");
        assertNull(pr.getScreenName());
        assertEquals(p.getUrl(),"test_url");
    }

    @Test
    public void parseFriendListTest() {
        Document doc = Jsoup.parse(read("UsersFriendList.html"));
        
        List<String> urls = parser.parseFriendList(doc);
        assertEquals(urls.size(),22);
    }

    @Test
    public void parseProfileFriendListTest() {
        Document doc = Jsoup.parse(read("ProfileFriendList.html"));
        
        List<String> urls = parser.parseProfileFriendList(doc);
        
        assertEquals(urls.size(),23);
    }

    @Test
    public void parseGroupListTest() {
        Document doc = Jsoup.parse(read("GroupList.html"));
        List<String> url  = parser.parseGroupsList(doc);
        
        assertEquals(url.size(),5);
    }

    @Test
    public void parseSubscribeListTest() {
        Document doc = Jsoup.parse(read("SubscribesList.html"));
        List<String> urls = parser.parseSubscribesList(doc);
        
        assertEquals(urls.size(), 14);
    }

    @Test
    public void parseUsersSubscibesCountTest() {
        String html = read("UsersSubscribesCount.html");
        
        long count = parser.parseUsersSubscribersCount(html);
        assertEquals(count,21);
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
