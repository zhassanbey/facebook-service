/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookclient.util;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author Zhasan
 */
public class DateUtils {

    Logger log = Logger.getLogger(DateUtils.class);
    
    public DateUtils() {

    }

    public Date getExactDate(String dStr) throws ParseException {
        Calendar calendar = new GregorianCalendar();

        SimpleDateFormat format = new SimpleDateFormat("dd MMMM");

        format.setDateFormatSymbols(new DateFormatSymbols(new Locale("RU")));

        Calendar temp = new GregorianCalendar();

        temp.setTime(format.parse(dStr));

        temp.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        temp.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        temp.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

        calendar.setTime(temp.getTime());

        return calendar.getTime();
    }

    public Date getAtDayOfWeek(String dStr) throws ParseException {
        Calendar calendar = new GregorianCalendar();

        SimpleDateFormat format = new SimpleDateFormat("EEEE в hh:mm");

        format.setDateFormatSymbols(new DateFormatSymbols(new Locale("RU")));

        Calendar temp = new GregorianCalendar();
        try {
            temp.setTime(format.parse(dStr));
        } catch (ParseException ex) {
            format.applyPattern("EEEE, в hh:mm");
            temp.setTime(format.parse(dStr));
        }

        calendar.set(Calendar.DAY_OF_WEEK, temp.get(Calendar.DAY_OF_WEEK));

        calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));

        calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));

        return calendar.getTime();
    }

    public Date getExactAt(String dStr) throws ParseException {
        Calendar calendar = new GregorianCalendar();

        SimpleDateFormat format = new SimpleDateFormat("dd MMMM в hh:mm");

        format.setDateFormatSymbols(new DateFormatSymbols(new Locale("RU")));

        Calendar temp = new GregorianCalendar();

        temp.setTime(format.parse(dStr));

        temp.set(Calendar.YEAR, calendar.get(Calendar.YEAR));

        calendar.setTime(temp.getTime());

        return calendar.getTime();
    }

    public Date getTodayAt(String dStr) throws ParseException {
        Calendar calendar = new GregorianCalendar();

        SimpleDateFormat format = new SimpleDateFormat("Сегодня в hh:mm");

        format.setDateFormatSymbols(new DateFormatSymbols(new Locale("RU")));

        Calendar temp = new GregorianCalendar();

        try {
            temp.setTime(format.parse(dStr));
        } catch (Exception ex) {
            format.applyPattern("Сегодня, в hh:mm");
            temp.setTime(format.parse(dStr));
        }
        calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));

        return calendar.getTime();

    }

    public Date getYesterdayAt(String dStr) throws ParseException {
        Calendar calendar = new GregorianCalendar();

        SimpleDateFormat format = new SimpleDateFormat("Вчера, в hh:mm");

        format.setDateFormatSymbols(new DateFormatSymbols(new Locale("RU")));

        Calendar temp = new GregorianCalendar();

        try {
            temp.setTime(format.parse(dStr));
        } catch (Exception ex) {
            format.applyPattern("Вчера в hh:mm");
            temp.setTime(format.parse(dStr));
        }
        calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
        calendar.roll(Calendar.DAY_OF_YEAR, -1);

        return calendar.getTime();
    }

    public Date getMinAgo(String dStr) throws ParseException {
        Calendar calendar = new GregorianCalendar();

        SimpleDateFormat format = new SimpleDateFormat("mm мин");

        format.setDateFormatSymbols(new DateFormatSymbols(new Locale("RU")));

        Calendar temp = new GregorianCalendar();
        temp.setTime(format.parse(dStr));

        calendar.roll(Calendar.MINUTE, -temp.get(Calendar.MINUTE));

        return calendar.getTime();
    }

    public Date getHourAgo(String dStr) throws ParseException {
        Calendar calendar = new GregorianCalendar();

        SimpleDateFormat format = new SimpleDateFormat("hh ч");

        format.setDateFormatSymbols(new DateFormatSymbols(new Locale("RU")));

        Calendar temp = new GregorianCalendar();
        temp.setTime(format.parse(dStr));

        if (calendar.get(Calendar.HOUR_OF_DAY) < temp.get(Calendar.HOUR_OF_DAY)) {
            calendar.roll(Calendar.DAY_OF_WEEK, -1);
        }

        calendar.roll(Calendar.HOUR_OF_DAY, -temp.get(Calendar.HOUR_OF_DAY));

        return calendar.getTime();
    }

    public Date getEstimated(String dStr) throws ParseException {

        boolean hour = false, minute = false, month = false, year = false, dayWeek = false, day = false;

        String pattern = "";

        String tokens[] = dStr.split(" ");

        Calendar calendar = new GregorianCalendar();

        SimpleDateFormat format = new SimpleDateFormat();
        Stack<String> stack = new Stack<>();

        for (String tk : tokens) {

            stack.push(tk);

        }

        format.setDateFormatSymbols(new DateFormatSymbols(new Locale("RU")));
        
        while (!stack.empty()) {
            String t1 = stack.pop();

            Pattern numberPat = Pattern.compile("\\d+");
            Matcher matcher = numberPat.matcher(t1);

            if (matcher.find()) {

                if (t1.contains(":")) {
                    hour = true;
                    minute = true;
                    pattern = "hh:mm "+pattern;
                } else {
                    if (t1.length() < 4) {
                        day = true;
                        pattern = "dd " + pattern;
                    } else {
                        year = true;
                        pattern = "yyyy " + pattern;
                    }
                }

            } else {
                format.applyPattern("MMMM dd");
                try {
                    Date x = format.parse(t1 + " 30");
                    if (!x.toString().contains(t1)) {
                        month = true;
                        pattern = "MMMM " + pattern;
                    } else {
                        throw new Exception("not month string " + t1);
                    }
                } catch (Exception ex) {
                    format.applyPattern("EEEE dd");
                    try {
                        Date x = format.parse(t1 + " 30");

                        if (!x.toString().contains(t1)) {
                            dayWeek = true;
                            pattern = "EEEE " + pattern;
                        } else {
                            pattern = t1 + " " + pattern;
                        }
                    } catch (Exception exx) {
                        pattern = t1 + " " + pattern;

                    }
                }

            }
        }

        log.info("Pattern \"" + pattern.trim() + "\"");

        format.applyPattern(pattern.trim());

        Calendar temp = new GregorianCalendar();
        
        temp.setTime(format.parse(dStr));

        if (hour) {
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
        }
        if (month) {
            calendar.set(Calendar.MONTH, temp.get(Calendar.MONTH));
        }
        if (minute) {
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
        }
        if (year) {
            calendar.set(Calendar.YEAR, temp.get(Calendar.YEAR));
        }
        if (day) {
            calendar.set(Calendar.DAY_OF_MONTH, temp.get(Calendar.DAY_OF_MONTH));
        }
        if (dayWeek) {
            calendar.set(Calendar.DAY_OF_WEEK, temp.get(Calendar.DAY_OF_WEEK));
        }
        
        return calendar.getTime();
    }

    public Date getDate(String dStr) throws ParseException {

        Date now = new Date();

        String tokens[] = dStr.split(" ");

        switch (tokens.length) {
            case 2: {
                try {
                    return getMinAgo(dStr);
                } catch (ParseException ex) {
                    // Logger.getLogger(DateUtils.class.getName()).log(Level.SEVERE, null, ex);
                    try {
                        return getHourAgo(dStr);
                    } catch (ParseException exx) {
                        return getExactDate(dStr);
                    }
                }
            }

            case 3: {
                try {
                    return getTodayAt(dStr);
                } catch (ParseException e) {
                    try {
                        return getYesterdayAt(dStr);
                    } catch (ParseException ex) {
                        // Logger.getLogger(DateUtils.class.getName()).log(Level.SEVERE, null, ex);
                        try {
                            return getMinAgo(dStr);
                        } catch (ParseException exx) {
                            //   Logger.getLogger(DateUtils.class.getName()).log(Level.SEVERE, null, exx);
                            try {
                                return getHourAgo(dStr);
                            } catch (ParseException exxx) {
                                try {
                                    return getAtDayOfWeek(dStr);
                                } catch (ParseException exxxxxx) {

                                }
                            }
                        }
                    }
                }
            }
            case 4: {
                try {
                    return getExactAt(dStr);
                } catch (ParseException ex) {

                }
            }
            default: {

            }
        }

        Date result = getEstimated(dStr);
        System.out.println(dStr + " is so strange... Estimated as "+result);
        return result;
    }

}
