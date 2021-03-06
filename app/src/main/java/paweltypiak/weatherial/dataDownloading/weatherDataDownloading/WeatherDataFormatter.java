/*******************************************************************************
 * Copyright 2017 Paweł Typiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package paweltypiak.weatherial.dataDownloading.weatherDataDownloading;

import android.app.Activity;
import android.os.Build;
import android.text.format.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;

public class WeatherDataFormatter {

    private int[] units;
    private Activity activity;
    private String direction;
    private String directionName;
    private String speed;
    private String humidity;
    private String pressure;
    private String sunrise;
    private String sunset;
    private String sunrise24;
    private String sunset24;
    private int code;
    private String temperature;
    private String temperatureUnit;
    private int[] forecastCode;
    private String[] forecastHighTemperature;
    private String[] forecastLowTemperature;
    private String link;
    private String city;
    private String country;
    private String region;
    private String time24;
    private String timezone;
    private String lastBuildDate;
    private boolean isDay;
    private long currentDiffMinutes;
    private long sunsetSunriseDiffMinutes;
    private WeatherDataParser dataInitializer;
    private int hourDifference;

    public WeatherDataFormatter(Activity activity, WeatherDataParser dataInitializer){
        this.units= SharedPreferencesModifier.getUnits(activity);
        this.activity=activity;
        this.dataInitializer=dataInitializer;
        getData();
        formatData();
    }

    private void formatData(){
        formatLastBuildDate();
        formatAstronomy();
        formatAtmosphere();
        formatCondition();
        formatForecast();
        formatWind();
        countSunPosition(time24);
        countHourDifference();
    }

    private void formatLastBuildDate(){
        //format last build date to get time zone
        lastBuildDate=lastBuildDate.substring(17);
        String unformattedTime =lastBuildDate.substring(0,8);
        time24=get24Time(unformattedTime);
        timezone=lastBuildDate.substring(9);
    }

    private void formatAtmosphere(){
        humidity=humidity+" "+"%";
        pressure=formatPressureUnit(pressure);
    }

    private void formatAstronomy(){
        //format sunrise and sunset in 12h and 24h time format
        sunrise24=get24Time(sunrise);
        sunset24=get24Time(sunset);
        sunrise=formatTimeUnit(sunrise);
        sunset=formatTimeUnit(sunset);
    }

    private void formatCondition(){
        temperature=formatTemperatureUnit(temperature);
    }

    private void formatForecast(){
        String degreeSign=activity.getString(R.string.dagree_sign);
        for(int i=0;i<5;i++){
            forecastLowTemperature[i]=formatTemperatureUnit(forecastLowTemperature[i])+degreeSign;
            forecastHighTemperature[i]=formatTemperatureUnit(forecastHighTemperature[i])+degreeSign;
        }
    }

    private void formatWind(){
        //format wind direction angle to get direction name
        if( Integer.parseInt(direction)<22 ||Integer.parseInt(direction)>=337 )
            directionName="N";
        else if( Integer.parseInt(direction)>=22 &&Integer.parseInt(direction)<67 )
            directionName="NE";
        else if( Integer.parseInt(direction)>=67 &&Integer.parseInt(direction)<112 )
            directionName="E";
        else if( Integer.parseInt(direction)>=112 &&Integer.parseInt(direction)<157 )
            directionName="SE";
        else if( Integer.parseInt(direction)>=157 &&Integer.parseInt(direction)<202 )
            directionName="S";
        else if( Integer.parseInt(direction)>=202 &&Integer.parseInt(direction)<247 )
            directionName="SW";
        else if( Integer.parseInt(direction)>=247 &&Integer.parseInt(direction)<292 )
            directionName="W";
        else if( Integer.parseInt(direction)>=292 &&Integer.parseInt(direction)<337 )
            directionName="NW";
        speed= formatSpeedUnit(speed);
    }

    private void countHourDifference() {
        String timeHour=null;
        String actualTimeHour=null;
        SimpleDateFormat inputFormat;
        SimpleDateFormat outputFormat;
        if(Build.VERSION.SDK_INT >= 18) {
            inputFormat=new SimpleDateFormat("H:mm");
            outputFormat = new SimpleDateFormat("H");

        } else {
            inputFormat=new SimpleDateFormat("k:mm");
            outputFormat = new SimpleDateFormat("k");
        }
        Date date;
        Calendar calendar= Calendar.getInstance();
        try {
            if(Build.VERSION.SDK_INT >= 18) {
                actualTimeHour = DateFormat.format("H", calendar).toString();

            } else {
                actualTimeHour = DateFormat.format("k", calendar).toString();
            }
            date = inputFormat.parse(time24);
            timeHour = outputFormat.format(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        hourDifference=Integer.parseInt(timeHour)-Integer.parseInt(actualTimeHour);
    }

    public String[] countSunPosition(String currentTime){
        SimpleDateFormat outputFormat;
        if(Build.VERSION.SDK_INT >= 18) {
            outputFormat= new SimpleDateFormat("H:mm");

        } else {
            outputFormat= new SimpleDateFormat("k:mm");
        }
        Date[] dates= parseSunPositionDataDates(currentTime,outputFormat);
        Date sunriseHour=dates[0];
        Date sunsetHour=dates[1];
        Date now=dates[2];
        long[] sunPositionData;
        if((now.after(sunriseHour)&&now.before(sunsetHour))||now.equals(sunriseHour)||now.equals(sunsetHour)){
            sunPositionData=getSunPositionDataForDay(sunriseHour,sunsetHour,now);
        }
        else {
            sunPositionData=getSunPositionDataForNight(sunriseHour,sunsetHour,now,outputFormat) ;
        }
        countDiffMinutes(sunPositionData);
        String outputString[]={
                Long.toString(sunsetSunriseDiffMinutes),
                Long.toString(currentDiffMinutes),
                Integer.toString((isDay)? 1 : 0)};
        return outputString;
    }

    private Date[] parseSunPositionDataDates(String currentTime, SimpleDateFormat outputFormat){
        Date sunriseHour=null;
        Date sunsetHour=null;
        Date now=null;
        try{
            sunriseHour= outputFormat.parse(sunrise24);
            sunsetHour= outputFormat.parse(sunset24);
            now=outputFormat.parse(currentTime);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        Date[] outputArray={sunriseHour,sunsetHour,now};
        return outputArray;
    }

    private long [] getSunPositionDataForDay(Date sunriseHour,
                                               Date sunsetHour,
                                               Date now){
        isDay =true;
        long sunsetSunriseDifference = Math.abs(sunsetHour.getTime() - sunriseHour.getTime());
        long currentDifference= Math.abs(now.getTime()-sunriseHour.getTime());
        long[] output={sunsetSunriseDifference,currentDifference};
        return output;
    }

    private long[] getSunPositionDataForNight(Date sunriseHour,
                                              Date sunsetHour,
                                              Date now,
                                              SimpleDateFormat outputFormat){
        isDay=false;
        Date beforeMidnight=null;
        Date afterMidnight=null;
        try{
            beforeMidnight=outputFormat.parse("23:59");
            afterMidnight=outputFormat.parse("0:00");
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        long twentyFourHours = Math.abs(beforeMidnight.getTime()-afterMidnight.getTime());
        long sunsetSunriseDifference=twentyFourHours-Math.abs(sunsetHour.getTime() - sunriseHour.getTime());
        long currentDifference;
        if(now.before(sunriseHour)){
            currentDifference= sunsetSunriseDifference-Math.abs(now.getTime() - sunriseHour.getTime());
        }
        else {
            currentDifference= Math.abs(now.getTime() - sunsetHour.getTime());
        }
        long[] output={sunsetSunriseDifference,currentDifference};
        return output;
    }

    private void countDiffMinutes(long[] sunPositionData){
        long sunsetSunriseDifference=sunPositionData[0];
        long currentDifference=sunPositionData[1];
        sunsetSunriseDiffMinutes = sunsetSunriseDifference / (60*1000);
        currentDiffMinutes = currentDifference / (60*1000);
    }

    private String get24Time(String time){
        String time24=null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("h:mm a");
        SimpleDateFormat outputFormat;
        if(Build.VERSION.SDK_INT >= 18) {
            outputFormat = new SimpleDateFormat("H:mm");

        } else {
            outputFormat = new SimpleDateFormat("k:mm");
        }
        Date date;
        try {
            date = inputFormat.parse(time);
            time24 = outputFormat.format(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return time24;
    }

    private String formatTemperatureUnit(String temperature){
        if(units[0]==0){
            temperature=Integer.toString((int)Math.round(0.55*(Integer.parseInt(temperature)-32)));
            temperatureUnit="C";
        }
        else {
            temperatureUnit="F";
        }
        return temperature;
    }
    private String formatSpeedUnit(String speed){
        String speedUnitsNames[]=activity.getResources().getStringArray(R.array.units_array_1);
        if(units[1]==0){

            speed=Integer.toString((int)Math.round(0.625*Integer.parseInt(speed)))+" "+speedUnitsNames[0];
        }
        else speed=speed+" "+speedUnitsNames[1];
        return speed;
    }
    private String formatPressureUnit(String pressure){
        String pressureUnitsNames[]=activity.getResources().getStringArray(R.array.units_array_2);
        if (pressure.indexOf(".") != -1) pressure= pressure.substring(0 , pressure.indexOf("."));
        if(units[2]==0){
            pressure=pressure+" "+pressureUnitsNames[0];
        }
        else {
            pressure=Integer.toString((int)Math.round(0.0295*Integer.parseInt(pressure)));
            pressure=pressure+" "+pressureUnitsNames[1];
        }
        return pressure;
    }
    private String formatTimeUnit(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("h:mm a");
        if (units[3] == 0) {
            SimpleDateFormat outputFormat;
            if(Build.VERSION.SDK_INT >= 18) {
                outputFormat = new SimpleDateFormat("HH:mm");

            } else {
                outputFormat = new SimpleDateFormat("kk:mm");
            }

            Date date;
            try {
                date = inputFormat.parse(time);
                time = outputFormat.format(date);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        else {
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");
            Date date;
            try {
                date = inputFormat.parse(time);
                time = outputFormat.format(date);

            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        return time;
    }

    private void getData(){
        link=dataInitializer.getLink();
        city=dataInitializer.getCity();
        country=dataInitializer.getCountry();
        region=dataInitializer.getRegion();
        lastBuildDate=dataInitializer.getLastBuildDate();
        direction= dataInitializer.getDirection();
        speed= dataInitializer.getSpeed();
        humidity= dataInitializer.getHumidity();
        pressure= dataInitializer.getPressure();
        sunrise= dataInitializer.getSunrise();
        sunset= dataInitializer.getSunset();
        code= dataInitializer.getCode();
        temperature= dataInitializer.getTemperature();
        forecastCode = dataInitializer.getForecastCode().clone();
        forecastHighTemperature = dataInitializer.getForecastHighTemperature().clone();
        forecastLowTemperature = dataInitializer.getForecastLowTemperature().clone();
    }

    public String getDirection() {return direction;}
    public String getDirectionName() {return directionName;}
    public String getSpeed() {return speed;}
    public String getHumidity() {return humidity;}
    public String getPressure() {return pressure;}
    public String getSunrise() {return sunrise;}
    public String getSunset() {return sunset;}
    public int getCode() {return code;}
    public String getTemperature() {return temperature;}
    public String getTemperatureUnit(){return temperatureUnit;}
    public int[] getForecastCode() {return forecastCode;}
    public String[] getForecastHighTemperature() {return forecastHighTemperature;}
    public String[] getForecastLowTemperature() {return forecastLowTemperature;}
    public String getLink() {return link;}
    public String getCity() {return city;}
    public String getCountry() {return country;}
    public String getRegion() {return region;}
    public String getTimezone() {return timezone;}
    public boolean getDay() {return isDay;}
    public long getCurrentDiffMinutes() {return currentDiffMinutes;}
    public long getSunsetSunriseDiffMinutes() {return sunsetSunriseDiffMinutes;}
    public int getHourDifference(){return hourDifference;}
}
