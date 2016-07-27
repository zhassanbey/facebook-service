/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.facebookclient.util;

/**
 *
 * @author root
 */
public class GeoUtils {
    
    public static final String [] KZ ={"kazakhstan","almaty", "astana","oskemen","atyrau","aktau","oral","pavlodar","petropavl","karagand","aktobe","semey","zhezkazgan","kostanay", "shymkent","taraz", "kyzylorda",
                                       "казахстан","алматы","астана","оскемен","атырау","актау","орал","павлодар","петропавл","караганд","актобе","семей","жезказган","костанай","шымкент","тараз","кызылорда",
                                       "chimkent","chymkent","alma-ata","ust-kamenogorsk","uralsk","semipalatinsk","turkistan","turkestan","qazaqstan","kazakstan","qyzylorda",
                                       "алма-ата","усть-каменогорск","уральск","семипалатинск","семск","кустанай","уськаман","туркистан","туркестан","чимкент"}; 
    
    public static String getCountry(String str){
        String country = null;
        if(str.contains(",")){
            String tokens [] = str.split(",");
            for(String token:tokens){
                if(token.trim().equalsIgnoreCase("kazakhstan") || token.equalsIgnoreCase("казахстан")){
                    country = "KZ";
                    break;
                }
            }
        } else {
            for(String token:KZ){
                if(str.toLowerCase().contains(token)){
                    country = "KZ";
                    break;
                }
            }
        }
        return country;
    }
    
}
