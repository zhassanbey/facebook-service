/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookclient.util;

/**
 *
 * @author Zhasan
 */
public class URLCreator {

    private String baseAddress;
    private StringBuilder builder;
    private final char AND = '&';
    private final char MAPS = '=';
    private boolean queriable;
    private final char QUERY_SIGN = '?';

    public URLCreator(String baseAddress) {
        this.baseAddress = baseAddress;
        builder = new StringBuilder(baseAddress);
        queriable = false;
    }

    public void addParameter(String name, String value) {
        if (!queriable) {
            builder.append(QUERY_SIGN);
            queriable = true;
        } else {
            builder.append(AND);
        }
        
        builder.append(name);
        builder.append(MAPS);
        builder.append(value);
    }

    public String getUrl() {
        return builder.toString();
    }
}
