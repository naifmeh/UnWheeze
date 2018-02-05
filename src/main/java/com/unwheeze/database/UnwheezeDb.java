package com.unwheeze.database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import com.unwheeze.beans.AirData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.HashMap;

public class UnwheezeDb {

    private static final Logger log = LogManager.getLogger(UnwheezeDb.class);

    private static final String HOST = "localhost";
    private static final int PORT = 28015;

    private static RethinkDB r;
    private static Connection connection;

    private static boolean isDbInit = false;

    private Gson gson = new Gson();

    private static final String AIRTABLE = DbScheme._AIRDATA;

    public UnwheezeDb() {
        if(!isDbInit) {
            r = RethinkDB.r;
            connection = r.connection().hostname(HOST).port(PORT).connect();

            if(connection.isOpen())
                    isDbInit = true;
            connection.use(DbScheme.DB);
        }

    }

    public int putDataInCollection(AirData airData) {
        String jsonAir = gson.toJson(airData);
        MapObject airDataHashMap = gson.fromJson(jsonAir, new TypeToken<MapObject>(){}.getType());
        HashMap<String,Object> result = r.table(AIRTABLE).insert(airDataHashMap)
                .run(connection);

        return (int)((long)result.get("errors"));
    }

    public String getDataFromCollection(String id) throws IllegalAccessException{
        log.info("Retrieving AirData by ID");

        Cursor cursor = r.table(AIRTABLE)
                .filter(row -> row.getField(DbScheme.AIRDATA_ID).eq(id))
                .run(connection);
        if(!cursor.hasNext()) throw new IllegalAccessException("ID was not found");
        //TODO : MAYBE Write own exceptions extending IllegalAccess
        HashMap<Object,String> airData = (HashMap<Object,String>) cursor.next();
        String jsonAirData = gson.toJson(airData, new TypeToken<HashMap<Object,String>>(){}.getType());

        return jsonAirData;
    }


}
