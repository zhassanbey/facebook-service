package kz.alem.semantics.facebook.service.facebookclient.constants;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NetUtil implements Serializable {

    private static Pattern pattern = initializePattern();

    public static Set<String> extractUrls(String input, WebURL webUrl) {
        Set<String> extractedUrls = new HashSet<>();
        Document doc = Jsoup.parse(input);
        Elements linkElements = doc.select("a[href]");
        for (Element linkElement : linkElements) {
            String link = linkElement.attr("href");
            link = link.trim();

            if (link.isEmpty() || link.equals("#") || link.startsWith("#")) {
                continue;
            }

            try {
                URL baseUrl = new URL(webUrl.getURL());
                URL url = new URL(baseUrl, link);
                link = url.toString();
            } catch (MalformedURLException ex) {
                if (link.startsWith("//")) {
                    link = link.replaceFirst("//", "");
                } else if (link.startsWith("/")) {
                    link = webUrl.getDomain() + link;
                    if (!webUrl.getSubDomain().isEmpty()) {
                        link = webUrl.getSubDomain() + "." + link;
                    }
                }

                if (!link.startsWith("http")) {
                    link = "http://" + link;
                }
            }

            extractedUrls.add(link);
        }

        return extractedUrls;
    }

    public static String getDomain(String url) {
        WebURL webUrl = new WebURL(url);

        String domain = webUrl.getDomain();

        String subDomain = webUrl.getSubDomain();
        if (!subDomain.isEmpty() && !subDomain.equals("www")) {
            domain = String.format("%s.%s", subDomain, domain);
        }

        return domain;
    }

    public static String getBaseDomain(String url) {
        WebURL webUrl = new WebURL(url);
        String domain = webUrl.getDomain();
        return domain;
    }

    public static String removeUrlAttributes(String url) {
        int index = url.indexOf("?");
        return index >= 0 ? url.substring(0, index) : url;
    }

    // Singleton like one time call to initialize the Pattern
    private static Pattern initializePattern() {
        return Pattern.compile("\\s*(?i)href\\s*=\\s*(\\\"([^\"]*\\\")|'[^']*'|([^'\">\\s]+))");
    }

}
