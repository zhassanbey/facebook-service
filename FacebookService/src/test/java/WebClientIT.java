/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import kz.alem.semantics.facebook.service.facebookservice.controller.web.client.wrapper.WebClientWrapper;
import org.junit.Test;

/**
 *
 * @author Zhasan
 */
public class WebClientIT {

    public WebClientIT() {
    }

    @Test
    public void isAutorizedTest() throws IOException {
       // PrintWriter out = new PrintWriter(new File("unautorized.html"));

        WebClientWrapper wrapper = new WebClientWrapper();

        String username = "alem.petruha", password = "alem123456";

        String url = "https://www.facebook.com/zeynab.heydar/friends";

        HtmlPage htmlPage = wrapper.getWebClient().getPage(url);

        // out.println(htmlPage.asXml());
        long t0 = System.nanoTime();

        assertFalse(wrapper.isAuthorized(htmlPage));

        System.out.println("Checking for authorization took " + (System.nanoTime() - t0) / 1000000 + " ms");

        t0 = System.nanoTime();

        wrapper.authorize(username, password);

        System.out.println("Authorization took " + (System.nanoTime() - t0) / 1000000 + " ms");

        htmlPage = wrapper.getWebClient().getPage(url);

        assertTrue(wrapper.isAuthorized(htmlPage));
      //  out = new PrintWriter(new File("autorized.html"));

    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
