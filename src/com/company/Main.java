package com.company;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        String strJson = null;
        String urlApi = "https://api.openweathermap.org/data/2.5/onecall?lat=59.9342802&lon=30.3350986&exclude=hourly,%20daily&units=metric&appid=f48590ffbc16750a16527813681ccc2b";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlApi).openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            byte[] bytes= new byte[is.available()];
            is.read(bytes);
            strJson = new String(bytes);
        } catch (Exception ex) {}
        main.parseString(strJson);
    }
    private boolean parseString(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        String[] strArr = s.split("daily");
        String[] strArr2 = strArr[1].split("\\,");
        int countDays = 0;
        int countMorn = 0;
        for(String strParse : strArr2) {
            if(strParse.contains("\"dt\":")) {
                countMorn = 0;
                countDays++;
                if(countDays > 5) return false;
                long unixTime= Integer.valueOf(strParse.replaceAll("[^0-9]", ""));
                Date date = new Date(unixTime*1000L);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));
                System.out.println(sdf.format(date));
            }
            if(strParse.contains("morn")) {
                String[] morn = strParse.split(":");
                if(countMorn == 0) {
                    System.out.println(morn[1].replaceFirst(".$",""));
                    countMorn++;
                }
            }
        }
        return false;
    }
}
