/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import kz.alem.semantics.facebook.service.facebookservice.controller.web.client.wrapper.WebClientWrapperLight;
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
public class WebClientLightIT {

    public WebClientLightIT() {
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

    @Ignore
    @Test
    public void webClientWrapperLightTest() {
        WebClientWrapperLight light = new WebClientWrapperLight();

        String username = "zhassan.baipakbayev";

        String password = "precious5pro";

        try {

            String homePage = light.getPageContent(username, password, "https://m.facebook.com/home.php");

            String friends = light.getPageContent(username, password, "https://m.facebook.com/zhassan.baipakbayev?v=friends");

            String friend = light.getPageContent(username, password, "https://m.facebook.com/zeynab.heydar");

            PrintWriter out = new PrintWriter(new File("homePage.html"));
            out.println(homePage);
            out.close();

            out = new PrintWriter(new File("friends.html"));
            out.println(friends);
            out.close();

            out = new PrintWriter(new File("friend.html"));
            out.println(friend);
            out.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Ignore
    @Test
    public void isAuthorizedTest() {
        WebClientWrapperLight light = new WebClientWrapperLight();

        String username = "zhassan.baipakbayev";

        String password = "precious5pro";

        assertFalse(light.isAuthorized());

        try {
            light.authorize(username, password);
        } catch (IOException ex) {
            Logger.getLogger(WebClientLightIT.class.getName()).log(Level.SEVERE, null, ex);
        }

        assertTrue(light.isAuthorized());

    }

    @Test
    public void isAuthorizedStableTest() throws IOException {
        WebClientWrapperLight light = new WebClientWrapperLight();

        String username = "zhassan.baipakbayev";

        String password = "precious5pro";

        String content = light.getPageContent(username, password, "https://m.facebook.com/home.php");

        System.out.println(content);
        
        assertFalse(light.isAuthorized(content));

        light.authorize(username, password);
        
        content = light.getPageContent(username, password, "https://m.facebook.com/home.php");

        assertTrue(light.isAuthorized(content));

    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
