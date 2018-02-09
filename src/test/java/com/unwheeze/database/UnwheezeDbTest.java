package com.unwheeze.database;

import com.google.gson.Gson;
import com.unwheeze.beans.AirData;
import com.unwheeze.beans.User;
import junit.framework.TestCase;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Test;

import java.util.Date;

public class UnwheezeDbTest extends TestCase {


    @Test
    public void testPutDataInCollection() {
        String location = "50.633333,3.066667";
        String userId = "54fd5fe5f5d";
        float pm10 = 20.5f;
        float pm25 = 14.5f;
        String no2 = "10";
        String dateTime = Long.toString((new Date()).getTime());

        AirData airData = new AirData(location,pm10,pm25,dateTime,userId);
        UnwheezeDb db = new UnwheezeDb();
        int res = db.putDataInCollection(airData);
        System.out.println(res);
        assertEquals(0,res);
    }

    @Test
    public void testGetDataFromCollection() {
        UnwheezeDb db = new UnwheezeDb();
        String id = "debac791-5b86-4151-a2f9-b32974974f62";
        String json="";
        try{
            json = db.getDataFromCollection(id);
        } catch(IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
        String expected = "{" +
                "\"datetime\":  \"1518017373390\" ," +
                "\"id\":  \"debac791-5b86-4151-a2f9-b32974974f62\" ," +
                "\"location\":  \"50.633333,3.066667\" ," +
                "\"no2\": 0 ," +
                "\"pm10\": 14.5 ," +
                "\"pm25\": 20.5 ," +
                "\"userID\":  \"54fd5fe5f5d\"" +
                "}";
        Gson gson = new Gson();
        AirData airdataJson = gson.fromJson(json,AirData.class);
        AirData airdataExp = gson.fromJson(expected,AirData.class);

        assertTrue(EqualsBuilder.reflectionEquals(airdataExp,airdataJson));

    }

    @Test
    public void testPutUserInCollection() {
        UnwheezeDb db = new UnwheezeDb();
        User user = new User("57859854e","5e4f5e1ed15","5d7e8f5d1d5","Naif","Mehanna","","naif.meh@gmail.com","15.055020,50.265445","Lille","France");

        int result = 0;
        if(!db.isUserInCollection("naif.meh@gmail.com","email"))
            result = db.putUserInCollection(user);

        assertEquals(0,result);
    }
    @Test
    public void testIsUserInCollection() {
        UnwheezeDb db = new UnwheezeDb();
        String emailGood = "naif.meh@gmail.com";
        String emailFalse = "naif.meha@gmail.com";
        String field = "email";

        boolean response1 = db.isUserInCollection(emailGood,field);
        boolean response2 = db.isUserInCollection(emailFalse,field);

        assertEquals(false,response2);
        assertEquals(true,response1);
    }
}
