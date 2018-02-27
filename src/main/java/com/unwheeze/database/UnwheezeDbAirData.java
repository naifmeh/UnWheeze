package com.unwheeze.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Cursor;
import com.unwheeze.beans.AirData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class UnwheezeDbAirData extends UnwheezeDb {

    public UnwheezeDbAirData() {
        super();
    }

    //----------------------------- AIR DATA
    public int putDataInCollection(AirData airData) {
        String jsonAir = super.gson.toJson(airData);
        MapObject airDataHashMap = super.gson.fromJson(jsonAir, new TypeToken<MapObject>(){}.getType());
        HashMap<String,Object> result = r.table(AIRTABLE).insert(airDataHashMap)
                .run(connection);

        return (int)((long)result.get("errors"));
    }

    public String getDataFromCollection(String id) throws IllegalAccessException{

        Cursor cursor = r.table(AIRTABLE)
                .filter(row -> row.getField(DbScheme.AIRDATA_ID).eq(id))
                .run(connection);
        if(!cursor.hasNext()) throw new IllegalAccessException("ID was not found");
        //TODO : MAYBE Write own exceptions extending IllegalAccess
        HashMap<Object,String> airData = (HashMap<Object,String>) cursor.next();
        String jsonAirData = super.gson.toJson(airData, new TypeToken<HashMap<Object,String>>(){}.getType());
        cursor.close();

        return jsonAirData;
    }

    public String getAllDataFromCollection() throws IllegalAccessException {
        Cursor cursor = r.table(AIRTABLE)
                .run(connection);
        if(!cursor.hasNext()) throw new IllegalAccessException("No rows");
        //TODO : MAYBE Write own exceptions extending IllegalAccess
        List<AirData> airDataArray =  new ArrayList<>();
        JsonArray airDataJsonArray;
        for(Object obj : cursor) {
            String temp = gson.toJson(obj);
            airDataArray.add(gson.fromJson(temp,AirData.class));
        }
        JsonElement arrayElement = gson.toJsonTree(airDataArray,new TypeToken<List<AirData>>(){}.getType());


        return arrayElement.toString();
    }
    public Cursor provideChangefeed() {
        return r.table(AIRTABLE).changes()
                .run(connection);
    }
}
