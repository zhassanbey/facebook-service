/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookclient.util;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Zhasan
 */
public class StringUtils {

    public static char[] rC = {'а', 'с', 'е', 'о', 'х', 'у'};
    public static char[] eC = {'a', 'c', 'e', 'o', 'x', 'y'};

    @Deprecated
    public static Set<String> getMatches(String text, String startPattern, String stopPattern) {

        String str = text;

        Set<String> result = new HashSet<>();

        int startIndex = Integer.MAX_VALUE;
        int stopIndex = Integer.MAX_VALUE;

        while (str.contains(startPattern)) {
            startIndex = str.indexOf(startPattern);

            stopIndex = str.indexOf(stopPattern, startIndex);

            result.add(StringEscapeUtils.unescapeXml(str.substring(startIndex, stopIndex)));

            str = str.substring(stopIndex, str.length());

        }

        return result;
    }

    public static String getStringBtw(String text, String start, int dStart, String stop, int dStop) {

        if (text.contains(start)) {
            int startIndex = text.indexOf(start) + dStart;

            int stopIndex;

            if (stop == null || stop.isEmpty()) {
                stopIndex = text.length();
            } else {
                stopIndex = text.indexOf(stop, startIndex) + dStop;
            }

            if (stopIndex >= 0) {
                return text.substring(startIndex, stopIndex);
            } else {
                return "";
            }
        }
        return "";
    }

    public static String getTextBtw(String text, String start, int dStart, String stop, int dStop) {
        String result = "";

        String content = text;

        int index = content.indexOf(stop, content.indexOf(start));;
        while (index >= 0) {

            String token = getStringBtw(content, start, dStart, stop, dStop);
            result = result + token;

            content = content.substring(index, content.length());

          //  System.out.println("[" + count + "]\n token = {" + token + "}\n result = {" + result + "}\n content = {" + content + "}");
            index = content.indexOf(stop, content.indexOf(start));
        }

        return result;
    }

    public static String normalizeString(String str) {
        String result = "";

        for (int i = 0; i < str.length(); i++) {
            result += cyrillic(str.charAt(i));

        }

        return result;
    }

    public static char cyrillic(char ch) {

        for (int i = 0; i < eC.length; i++) {
            if (eC[i] == ch) {
                return rC[i];
            }
        }

        return ch;
    }
}
