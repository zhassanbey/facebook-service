/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.alem.semantics.facebook.service.facebookclient.constants;

import java.util.List;
import kz.alem.semantics.facebook.service.facebookclient.client.impl.FacebookLightClientImpl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Zhasan
 */
public class RequestUtil {
    
     private static final Logger LOG = Logger.getLogger(RequestUtil.class);

     private static final String RED = (char) 27 + "[31m ", WHITE = (char) 27 + "[37m ";
    
     private static int timoutSeconds = 60 * 2;
    
     public static String getContent(String url) throws Exception {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, timoutSeconds * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, timoutSeconds * 1000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpGet get = new HttpGet(url);

        LOG.info(RED + " Starting to fetch data from service...");

        HttpResponse response = httpClient.execute(get);

        HttpEntity entity = response.getEntity();

        String content = EntityUtils.toString(entity);

        LOG.info(RED + " Fetched data from the service!");

        return content;

    }
     
     public static String postContent(String url, List<NameValuePair> nvps) throws Exception {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, timoutSeconds * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, timoutSeconds * 1000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(nvps));

        LOG.info(RED + " Sending share post request to the server...");

        HttpResponse response = httpClient.execute(post);

        HttpEntity entity = response.getEntity();

        String content = EntityUtils.toString(entity);

        return content;
    }
    
}
