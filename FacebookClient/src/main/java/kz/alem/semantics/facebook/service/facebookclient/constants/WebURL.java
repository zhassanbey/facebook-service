package kz.alem.semantics.facebook.service.facebookclient.constants;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebURL implements Serializable {

    private static final Logger LOG = Logger.getLogger(WebURL.class.getName());

    private static final long serialVersionUID = -2855892811073593050L;

    //  @PrimaryKey
    private String url;

    private int docid;
    private int parentDocid;
    private String parentUrl;
    private short depth;
    private String domain;
    private String subDomain;
    private String path;
    private String anchor;
    private byte priority;

    public WebURL(String url) {
        setURL(url);
    }

    public WebURL() {

    }

    //returns unique id assigned to this url
    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WebURL otherUrl = (WebURL) o;
        return url != null && url.equals(otherUrl.getURL());

    }

    @Override
    public String toString() {
        return url;
    }

    //return the unique url address
    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        try {
            if (url == null) {
                this.url = null;
                this.domain = null;
                this.subDomain = null;
                this.path = null;
                return;
            } else {
                url = url.trim();
                if (url.isEmpty()) {
                    this.url = null;
                    this.domain = null;
                    this.subDomain = null;
                    this.path = null;
                    return;
                }
            }

            this.url = url;

            int domainStartIdx = url.indexOf("//") + 2;
            int domainEndIdx = url.indexOf('/', domainStartIdx);

            //If url don't have "/" symbol after "//"
            if (domainEndIdx < 0) {
                domainEndIdx = url.length();
            }

            domain = url.substring(domainStartIdx, domainEndIdx);
            subDomain = "";
            String[] parts = domain.split("\\.");
            if (parts.length > 2) {
                domain = parts[parts.length - 2] + "." + parts[parts.length - 1];
                int limit = 2;
                for (int i = 0; i < parts.length - limit; i++) {
                    if (subDomain.length() > 0) {
                        subDomain += ".";
                    }
                    subDomain += parts[i];
                }
            }
            path = url.substring(domainEndIdx);
            int pathEndIdx = path.indexOf('?');
            if (pathEndIdx >= 0) {
                path = path.substring(0, pathEndIdx);
            }
        } catch (Exception ex) {
            this.url = url;
            this.domain = "";
            this.subDomain = "";
            this.path = "";
            return;
        }
    }

    /**
     * Returns the unique document id of the parent page. The parent page is the
     * page in which the Url of this page is first observed.
     */
    public int getParentDocid() {
        return parentDocid;
    }

    public void setParentDocid(int parentDocid) {
        this.parentDocid = parentDocid;
    }

    /**
     * Returns the url of the parent page. The parent page is the page in which
     * the Url of this page is first observed.
     */
    public String getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    /**
     * Returns the crawl depth at which this Url is first observed. Seed Urls
     * are at depth 0. Urls that are extracted from seed Urls are at depth 1,
     * etc.
     */
    public short getDepth() {
        return depth;
    }

    public void setDepth(short depth) {
        this.depth = depth;
    }

    /**
     * Returns the domain of this Url. For 'http://www.example.com/sample.htm',
     * domain will be 'example.com'
     */
    public String getDomain() {
        return domain;
    }

    public String getSubDomain() {
        return subDomain;
    }

    /**
     * Returns the path of this Url. For 'http://www.example.com/sample.htm',
     * domain will be 'sample.htm'
     */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Returns the anchor string. For example, in <a href="example.com">A sample
     * anchor</a>
     * the anchor string is 'A sample anchor'
     */
    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    /**
     * Returns the priority for crawling this URL. A lower number results in
     * higher priority.
     */
    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

}
