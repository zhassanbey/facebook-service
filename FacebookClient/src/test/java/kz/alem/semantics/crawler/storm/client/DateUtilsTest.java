/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.crawler.storm.client;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import kz.alem.semantics.facebook.service.facebookclient.util.DateUtils;
import kz.alem.semantics.facebook.service.facebookclient.util.StringUtils;
import kz.alem.semantics.parse.constants.DateConstants;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Zhasan
 */
public class DateUtilsTest {

    SimpleDateFormat solrDF;

    public DateUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        solrDF = new SimpleDateFormat(DateConstants.SOLR_DATE_FORMAT);
    }

    @After
    public void tearDown() {
    }

    
    @Test
    public void getStrangeDateTest() throws Exception {
        String dStr = "31 декабря в 21:07";
        DateUtils utils = new DateUtils();

        Date date = utils.getEstimated(dStr.trim());

        System.out.println("The best date " + solrDF.format(date));
    }

    @Test
    public void getDate1Test() throws ParseException {
        String dStr = "27 сентября";
        DateUtils utils = new DateUtils();

        Date date = utils.getExactDate(dStr);

        System.out.println("1 " + solrDF.format(date));

    }

    @Test
    public void getDate2Test() throws ParseException {
        String dStr = "13 ч";
        DateUtils utils = new DateUtils();
        Date date = utils.getEstimated(dStr);

        System.out.println("2 " + date);

    }

    @Test
    public void getDate3Test() throws ParseException {
        String dStr = StringUtils.normalizeString("cреда в 11:38");

        DateUtils utils = new DateUtils();
        Date date = utils.getDate(dStr);

        System.out.println("3 " + date);

    }

    @Test
    public void getDate4Test() throws ParseException {
        String dStr = "12 мая в 1:09";
        DateUtils utils = new DateUtils();
        Date date = utils.getDate(dStr);

        System.out.println("4 " + solrDF.format(date));

    }

    
    @Test
    public void getAtDayOfWeekTest() throws ParseException {
        String dStr = "Понедельник в 12:44";

        DateUtils utils = new DateUtils();

        Date date = utils.getEstimated(dStr);

        System.out.println("Day of week = " + date);
    }

    @Test
    public void getEstimateDateTest() throws ParseException {
        String dStr = "12 апреля в 17:26";

        DateUtils utils = new DateUtils();

        Date date = utils.getEstimated(dStr);

        System.out.println("Estimated = " + date);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
