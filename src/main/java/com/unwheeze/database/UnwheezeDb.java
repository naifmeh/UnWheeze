package com.unwheeze.database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import com.unwheeze.beans.AirData;
import com.unwheeze.beans.AuthClient;
import com.unwheeze.beans.User;
import com.unwheeze.utils.ReflectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UnwheezeDb {

    private static final Logger log = LogManager.getLogger(UnwheezeDb.class);

    protected static final String HOST = "localhost";
    protected static final int PORT = 28015;

    protected  RethinkDB r;
    protected  Connection connection;

    protected  boolean isDbInit = false;

    protected Gson gson = new Gson();

    protected static final String AIRTABLE = DbScheme._AIRDATA;
    protected static final String USERTABLE = DbScheme._USERS;
    protected static final String AUTHTABLE = DbScheme._WSAUTH;
    public UnwheezeDb() {
        if(!isDbInit) {
            r = RethinkDB.r;
            connection = r.connection().hostname(HOST).port(PORT).connect();

            if(connection.isOpen())
                    isDbInit = true;

            assertDatabaseExists();
            connection.use(DbScheme.DB);

            try {
                assertDbStructExists();
            } catch(IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }
    public int assertDatabaseExists() {
        boolean containDb = r.dbList().contains(DbScheme.DB).run(connection);
        if(!containDb)
            r.dbCreate(DbScheme.DB).run(connection);
        return 0;
    }

    public int assertDbStructExists() throws IllegalAccessException {

        HashMap<String,Object> fields = ReflectionUtils.getObject(new DbScheme());
        Iterator it = fields.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();

            String key = (String) pair.getKey();
            if (key.charAt(0) == '_' && key != "_VERSION") {
                boolean containTable = r.tableList().contains((String) pair.getValue()).run(connection);
                if (!containTable) r.tableCreate((String) pair.getValue()).run(connection);
            }
        }

        return 0;
    }

    public String generateUUID() {
        return r.uuid().run(connection);
    }






}
