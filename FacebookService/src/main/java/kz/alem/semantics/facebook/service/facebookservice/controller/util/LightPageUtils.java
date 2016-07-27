/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookservice.controller.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class containing utility methods often used in Light implementation of the
 * service.
 *
 * @author Zhasan
 */
public class LightPageUtils {

    /**
     * Checks whether a string is a username or facebook id.
     *
     * @param username - string parameter to be checked
     * @return True if the string is username. False otherwise
     */
    public static boolean isScreenName(String username) {
        try {
            Long.parseLong(username);
            return false;
        } catch (Exception x) {
            return true;
        }

    }

    /**
     * Separates substring between given string expression in a big text or
     * string.
     *
     * @param text - the text containing the substring
     * @param start - string expression preceding the substring
     * @param dStart - number of symbols needed to be shifted to the right
     * starting from the first symbol of the start string
     * @param stop - string expression situated after the substring. If stop is
     * null then the stop condition is the end the text.
     * @param dStop - number of symbols needed to be shifted to the right
     * starting from the first symbol of the stop string
     * @return Substring between the start and stop string expressions with
     * given shifting parameters.Returns empty string if no match case is found
     */
    public static String getStringBtw(String text, String start, int dStart, String stop, int dStop) {
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

    /**
     * Converts html form to a map;
     *
     * @param content - html content of the page that contains the form
     * @return Map from name parameters to the value parameters of the form
     */
    public static Map<String, String> mapForm(String content) {
        Map<String, String> result = new ConcurrentHashMap<>();
        String form = getStringBtw(content, "<form", 0, "</form", 0);
        String actionId = "action=\"";
        String action = getStringBtw(form, actionId, actionId.length(), "\"", 0);
        result.put("action", action);
        String inputId = "<input";
        int startIndex = -1;
        if (form.contains(inputId)) {
            startIndex = form.indexOf(inputId);
        }
        while (startIndex >= 0) {
            int endIndex = form.indexOf(">", startIndex);
            String input = form.substring(startIndex, endIndex);
            String nameId = "name=\"";
            int nameIndex = input.indexOf(nameId);
            String name = getStringBtw(input, nameId, nameId.length(), "\"", 0);
            String valueId = "value=";
            String value = "";
            if (input.contains(valueId)) {
                value = getStringBtw(input, valueId, valueId.length(), " ", 0);
                if (value.startsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                value = value.trim();
            }
            result.put(name, value);
            startIndex = form.indexOf(inputId, endIndex);
        }
        return result;
    }

}
