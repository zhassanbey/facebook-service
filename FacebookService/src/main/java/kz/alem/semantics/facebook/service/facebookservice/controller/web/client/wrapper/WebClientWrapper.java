package kz.alem.semantics.facebook.service.facebookservice.controller.web.client.wrapper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Customization of web client.
 *
 * @author Zhassan
 */
public class WebClientWrapper implements Serializable {

    private WebClient webClient = null;

    public WebClientWrapper() {
        String applicationName = "Netscape";
        String applicationVersion = "5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.99 Safari/537.36";
        String userAgent = "Mozilla/24.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.99 Safari/537.36";
        int browserVersionNumeric = 24;

        BrowserVersion browser = new BrowserVersion(applicationName, applicationVersion, userAgent, browserVersionNumeric) {
            @Override
            public boolean hasFeature(BrowserVersionFeatures property) {
                return BrowserVersion.FIREFOX_24.hasFeature(property);
            }
        };

        webClient = new WebClient(browser);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(true);
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getCookieManager().setCookiesEnabled(true);

    }

    public WebClient getWebClient() {
        return this.webClient;
    }

    public boolean authorize(String email, String password) {
        try {
            HtmlPage indexPage = this.webClient.getPage("https://www.facebook.com/index.php");
            HtmlForm form = indexPage.getForms().get(0);  // forms correct
            HtmlTextInput emailInput = form.getInputByName("email");
            HtmlPasswordInput passwordInput = form.getInputByName("pass");
            emailInput.setValueAttribute(email);
            passwordInput.setValueAttribute(password);
            form.getInputByValue("Log In").click();
        } catch (IOException | FailingHttpStatusCodeException ex) {
            System.out.println("Error while authorization");
            Logger.getLogger(WebClientWrapper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public String getPageContent(String botUsername, String botPassword, String url) {
        String content = null;
        try {
            HtmlPage htmlPage = webClient.getPage(url);

            if (!isAuthorized(htmlPage)) {
                authorize(botUsername, botPassword);
                htmlPage = webClient.getPage(url);
            }

            content = htmlPage.asXml();
        } catch (IOException | FailingHttpStatusCodeException ex) {
            ex.printStackTrace();
        }
        return content;
    }

    public boolean isAuthorized(HtmlPage htmlPage) {

        try {
            HtmlForm form = htmlPage.getForms().get(0);
            HtmlTextInput emailInput = form.getInputByName("email");
            return form.getInputByValue("Log In") == null;
        } catch (Exception ex) {
            return true;
        }

    }
}
