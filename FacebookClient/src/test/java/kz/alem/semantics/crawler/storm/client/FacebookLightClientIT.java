/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.crawler.storm.client;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import kz.alem.semantics.facebook.service.facebookclient.client.FacebookClient;
import kz.alem.semantics.facebook.service.facebookclient.client.impl.FacebookLightClientImpl;
import kz.alem.semantics.facebook.service.facebookclient.parser.FacebookLightParser;
import kz.alem.semantics.facebook.service.facebookclient.util.StringUtils;
import kz.alem.semantics.parse.model.Comment;
import kz.alem.semantics.parse.model.PageData;
import kz.alem.semantics.solr.client.model.Attachment;
import kz.alem.semantics.solr.client.model.Profile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Zhasan
 */
@Ignore
public class FacebookLightClientIT {

    public FacebookLightClientIT() {
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
    public void getRankTest(){
        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");
        String username = "zhassan.baipakbayev";
        String password = "precious5pro";
        
        long rank = client.getUsersSubscribersCount(username, password, username);
        System.out.println(""+rank);
    }
    
    @Test
    public void getLivingCountryTest(){
        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");
        String username = "zhassan.baipakbayev";
        String password = "precious5pro";
        
        List<String> frList = client.getFriendsList(username, password);
        int count = 0;
        for(String fr:frList){
            String country = client.getLivingCountry(username, password, fr);
            if(country == null){
                count++;
            }
        }
        
        double p = (double)(count)/(double)frList.size();
        System.out.println(""+p*100+"% of "+username+"'s friends are foreign");
    } 
    
    @Ignore
    @Test
    public void getFriendListTest() {
        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");

        String username = "zeynab.heydar";
        String password = "Onelovemusic1";

        List<String> frList = client.getFriendsList(username, password);

        System.out.println(username + " has " + frList.size() + " friends : {");
        for (String fr : frList) {
            System.out.println(" " + fr);

            List<String> posts = client.getProfileFeedUrls(username, password, fr);
            System.out.println("size = " + posts.size());
            for (String url : posts) {
                System.out.println(" " + url);
            }
        }
        System.out.println("}");
    }

    @Ignore
    @Test
    public void getSubsribesListTest() {
        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");

        String username = "zhassan.baipakbayev";
        String password = "precious5pro";

        List<String> sbList = client.getSubscribesList(username, password);

        System.out.println(username + " has " + sbList.size() + " friends : {");
        for (String sb : sbList) {
            System.out.println(" " + sb);

            List<String> posts = client.getProfileFeedUrls(username, password, sb);
            System.out.println("size = " + posts.size());
            for (String url : posts) {
                System.out.println(" " + url);
            }
        }
        System.out.println("}");
    }

    @Ignore
    @Test
    public void urlDecode() throws UnsupportedEncodingException{
        String url = "https://m.facebook.com/story.php?story_fbid\\u003d119118734822552\\u0026id\\u003d100001732795442\\u0026refid\\u003d17\\u0026_ft_\\u003dtl_objid.719790271422059%3Athid.100001732795442%3A306061129499414%3A32%3A0%3A1451635199%3A719790271422059#footer_action_list";
      
        String enc[] = {"UTF8","UTF16","UTF32"};
        
        for(int i = 0;i<3;i++){
        String url8 = URLEncoder.encode(url, enc[i]);
        System.out.println(""+url8);
        }
    }
    
    @Ignore
    @Test
    public void getProfileFeedTest() {
        FacebookClient client = new FacebookLightClientImpl("http://54.154.60.220:8092");
        String botUsername = "aygerim.nadir";
        String botPass = "alem123456";
        String username = "www.adme.ru";

        List<String> url = client.getProfileFeedUrls(botUsername, botPass, username);

        for (String u : url) {
            System.out.println(u);
            
//            
        }
        System.out.println("YES ok got it!!!");
    }

    @Ignore
    @Test
    public void getUsersFeedTest() {
        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");

        String username = "zhassan.baipakbayev";
        String password = "precious5pro";

        List<String> hrefs = client.getUsersFeedUrls(username, password);

        for (String href : hrefs) {
            System.out.println("href => " + href);
        }

    }

    @Ignore
    @Test
    public void getGroupsTest() {
        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");

        String username = "zhassan.baipakbayev";
        String pass = "precious5pro";

        List<String> groups = client.getGroupsList(username, pass);

        System.out.println("Groups size = " + groups.size());
        if (groups != null && !groups.isEmpty()) {
            for (String id : groups) {
                System.out.println(id);

                List<String> feedUrls = client.getGroupsFeedUrls(username, pass, id);

                System.out.println("size = " + feedUrls.size());
                for (int i = 0; i < feedUrls.size(); i++) {
                    String feedUrl = feedUrls.get(i);

                    System.out.println("" + feedUrl);
                }

            }

        }
    }
    
    @Test
    public void getSinglePostTest() throws FileNotFoundException, UnsupportedEncodingException {
        String url = "https://m.facebook.com/photo.php?fbid=10156332693380442&id=688860441&set=a.10150258100315442.505577.688860441&refid=17&_ft_=top_level_post_id.10156332693450442%3Atl_objid.10156332693380442%3Aprofile_picture_overlay_owner.688860441%3Aprofile_picture_overlay_id.1517756271856014%3Athid.688860441%3A306061129499414%3A30%3A0%3A1451635199%3A-5681901583800541654#footer_action_list";
        url = "https://m.facebook.com/photo.php?fbid=957848330953345&id=100001845627460&set=a.108830245855162.13471.100001845627460&refid=17&_ft_=top_level_post_id.957848364286675%3Atl_objid.957848330953345%3Athid.100001845627460%3A306061129499414%3A30%3A0%3A1451635199%3A-7956275418987149646#footer_action_list";
        url = "https://m.facebook.com/AstanaMotors/photos/a.421218997900013.93917.400018376686742/1046546468700593/?type=3&bacr=1450623690%3A1450623690%3A4%3A7754110586894940228%3A1450623482%3A0%3A0%3A180&refid=28&_ft_=qid.6230381308417888741%3Amf_story_key.1053933679386584832#footer_action_list";
        url = "https://m.facebook.com/story.php?story_fbid=10153320202180172&id=309669250171&bacr=1450530149%3A1450530149%3A2%3A1016274077439621771%3A1450018161%3A0%3A0%3A180&refid=28&_ft_=qid.6229982022373882982%3Amf_story_key.4462326821893283077#footer_action_list";
        url = "https://m.facebook.com/story.php?story_fbid=10153324929060172&id=309669250171&refid=17&_ft_=top_level_post_id.10153324929060172%3Atl_objid.10153324929060172%3Athid.309669250171%3A306061129499414%3A3%3A0%3A1451635199%3A2770643214518463303#footer_action_list";
        url = "https://m.facebook.com/way2auto/photos/a.307151942786725.1073741829.291586571009929/522423594592891/?type=3&bacr=1450633590%3A1450633590%3A4%3A-1539240066817419704%3A1450633484%3A0%3A0%3A180&refid=28&_ft_=qid.6230423828590159206%3Amf_story_key.-7887461091993710377#footer_action_list";
        url = "https://m.facebook.com/way2auto/photos/a.307151942786725.1073741829.291586571009929/522394177929166/?type=3&bacr=1450541020%3A1450541020%3A3%3A-6631137002714170927%3A1450540985%3A0%3A0%3A180&refid=28&_ft_=qid.6230026243333528372%3Amf_story_key.-8532924071591555255#footer_action_list";
        url = "https://m.facebook.com/groups/444490368960315?view=permalink&id=956432354432778&refid=18&_ft_=qid.6230965230647273908%3Amf_story_key.956432354432778%3Atl_objid.956432354432778#footer_action_list";
        url = "https://m.facebook.com/homecreditkz/photos/a.243410022529629.1073741828.121920358011930/471917653012197/?type=3&_rdr";
        url = "https://m.facebook.com/photo.php?fbid=931238010282545&id=100001890658330&set=a.386832788056406.91401.100001890658330&refid=17&_ft_=top_level_post_id.931238010282545%3Atl_objid.931238010282545%3Athid.100001890658330%3A306061129499414%3A69%3A0%3A1451635199%3A4755895741938437604#footer_action_list";
        url = "https://m.facebook.com/homecreditkz/photos/a.243410022529629.1073741828.121920358011930/479109852292977/?type=3&theater";
        url = "https://m.facebook.com/story.php?story_fbid=1000032506736951&id=183379858402224&notif_t=notify_me_page&_rdr";
        FacebookClient client = new FacebookLightClientImpl("http://54.154.60.220:8092");

        String username = "abramov.rinat";
        String password = "leavemealone";

        FacebookLightParser parser = new FacebookLightParser();

        String query = StringUtils.getStringBtw(url, "/m.facebook.com/", "/m.facebook.com/".length(), "\"", 0);

        PageData page = client.getPost(username, password, url);

                
                

//        PrintWriter out = new PrintWriter("facebook.html");
//        out.println(page.getContent());
//        out.close();
        Document doc = Jsoup.parse(page.getContent());

//        out.println(doc.html());
//        out.close();
        if (url.contains("/album")) {
            page = parser.parseAlbumPhotos(page, doc);
        } else if (url.contains("photo.php") || url.contains("/photo")) {
            page = parser.parsePhoto(page, doc);

        } else {
            page = parser.parsePost(page, doc);
        }

        if (page != null) {
            System.out.println("url => " + page.getUrl());
            System.out.println("article{");
            try {
                System.out.println(" [Author]: " + page.getArticle().getAuthor());
                System.out.println(" [Text]: " + page.getArticle().getText());
            } catch (Exception ex) {
                System.out.println(page.getContent());
            }
            System.out.println("}");

            System.out.println("published:{" + page.getPublished() + "}");

            System.out.println("attachements:{");
            if (page.getAttachmentList() != null) {
                for (Attachment att : page.getAttachmentList()) {
                    System.out.println(" [" + att.getType() + "] " + att.getLink());
                }
            }
            System.out.println("}");

            Profile profile = page.getProfile();
            if (profile != null) {
                System.out.println("profile:{");
                System.out.println(" " + profile.getName());
                System.out.println(" " + profile.getScreenName());
                System.out.println(" " + profile.getUrl());
                System.out.println(" " + profile.getType());
                System.out.println("}");
            }

            List<Comment> cmts = page.getCommentList();

            if (cmts != null) {
                System.out.println("Comments:{");
                for (Comment c : cmts) {
                    System.out.println("###########################");
                    System.out.println("   " + c.getAuthor());
                    System.out.println("   " + c.getText());
                    System.out.println("   " + c.getDate());

                    System.out.println("###########################");
                }

                System.out.println("}");
            }
        }

        System.out.println("---------------------------------------------------");
    }

    @Ignore
    @Test
    public void getPostTest() {
        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");

        String username = "zhassan.baipakbayev";
        String password = "precious5pro";

        List<String> hrefs = client.getUsersFeedUrls(username, password);

        for (String href : hrefs) {

            System.out.println("------------------" + href + "------------------------------------");

            String url = "https://m.facebook.com" + href;

            PageData page = client.getPost(username, password, url);

            if (page != null) {
                System.out.println("url => " + page.getUrl());
                System.out.println("article{");
                try {
                    System.out.println(" " + page.getArticle().getAuthor());
                    System.out.println(" " + page.getArticle().getText());
                } catch (Exception ex) {
                    System.out.println(page.getContent());
                }
                System.out.println("}");

                System.out.println("published:{" + page.getPublished() + "}");

                System.out.println("attachements:{");
                if (page.getAttachmentList() != null) {
                    for (Attachment att : page.getAttachmentList()) {
                        System.out.println(" [" + att.getType() + "] " + att.getLink());
                    }
                }
                System.out.println("}");

                System.out.println("profile:{");
                Profile profile = page.getProfile();
                if (profile != null) {
                    System.out.println(" " + profile.getName());
                    System.out.println(" " + profile.getScreenName());
                    System.out.println(" " + profile.getUrl());
                    System.out.println(" " + profile.getType());
                    System.out.println("}");
                }

                List<Comment> cmts = page.getCommentList();

                if (cmts != null) {
                    System.out.println("Comments:{");
                    for (Comment c : cmts) {
                        System.out.println("###########################");
                        System.out.println("   " + c.getAuthor());
                        System.out.println("   " + c.getText());
                        System.out.println("   " + c.getDate());

                        System.out.println("###########################");
                    }

                    System.out.println("}");
                }
            }

            System.out.println("---------------------------------------------------");
        }

    }

    @Ignore
    @Test
    public void getSubscriptionsCountTest() {
        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");

        String botUsername = "zhassan.baipakbayev";
        String botPassword = "precious5pro";

        String usernmae = "zhassan.baipakbayev";

        System.out.println(client.getUsersSubscribersCount(botUsername, botPassword, usernmae));

        usernmae = "murzabayev";

        System.out.println(client.getUsersSubscribersCount(botUsername, botPassword, usernmae));

        usernmae = "murataben";

        System.out.println(client.getUsersSubscribersCount(botUsername, botPassword, usernmae));

        usernmae = "trintrava";

        System.out.println(client.getUsersSubscribersCount(botUsername, botPassword, usernmae));

        usernmae = "100001777018125";

        System.out.println(client.getUsersSubscribersCount(botUsername, botPassword, usernmae));

    }

    @Ignore
    @Test
    public void getProfileFriendsTest() {
        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");

        String botUsername = "zhassan.baipakbayev";
        String botPassword = "precious5pro";
        String username = "100001777018125";

        List<String> frList = client.getProfileFriendsList(botUsername, botPassword, username);

        for (String str : frList) {
            System.out.println("    " + str);
        }
    }

    @Ignore
    @Test
    public void subscribeTest() {
        String botUsername = "zhassan.baipakbayev";
        String botPass = "precious5pro";
        String username = "MahabbatESEN";

        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");

        boolean ans = client.subscribe(botUsername, botPass, username);

        if (ans) {
            List<String> subs = client.getSubscribesList(botUsername, botPass);

            assertTrue(subs.contains(username));
        }
    }

    @Ignore
    @Test
    public void makeFriendTest() {
        String botUsername = "zhassan.baipakbayev";
        String botPass = "precious5pro";
        String username = "MahabbatESEN";

        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");
        boolean ans = client.makeFriend(botUsername, botPass, username);

        if (ans) {
            System.out.println("Make friend request sent to " + username + ". No, you may not check. Becuase my soft is realiable!");
        }
    }

    @Ignore
    @Test
    public void likeTest() {
        String botUsername = "zhassan.baipakbayev";
        String botPass = "precious5pro";
        String url = "a/like/inline.php?feedbackinline=1&chainingEnabled=1&ul&shareID=1428861580760894&fs=4&actionsource=timeline&ft_ent_identifier=1428861580760894&av=100001845627460&gfid=AQAdtILj32jqOVS6&refid=17&_ft_=top_level_post_id.1428861604094225%3Atl_objid.1428861580760894%3Athid.100009111978362%3A306061129499414%3A30%3A0%3A1448956799%3A-1382808709972864458";

        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");
        boolean ans = client.like(botUsername, botPass, url);

        assertTrue(ans);

    }

    @Ignore
    @Test
    public void likeUrlsTest() {
        String botUsername = "zhassan.baipakbayev";
        String botPassword = "precious5pro";

        String username = "zharkynb";

        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");

        List<String> likeUrls = client.getProfileFeedLikeUrls(botUsername, botPassword, username);

        for (String url : likeUrls) {
            client.like(botUsername, botPassword, url);
        }

    }

    @Test
    public void shareTest() {
        String botUsername = "zhassan.baipakbayev";
        String botPassword = "precious5pro";

        String username = "zeynab.heydar";

        FacebookClient client = new FacebookLightClientImpl("http://localhost:8092");
        List<String> urls = client.getProfileShareUrls(botUsername, botPassword, username);

        for (String url : urls) {
            System.out.println(url);

            client.share(botUsername, botPassword, url);
        }
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
