package com.unwheeze.database;

import com.google.gson.reflect.TypeToken;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Cursor;
import com.unwheeze.beans.AirData;

import java.util.HashMap;

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

        return jsonAirData;
    }
}
