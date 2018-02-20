package com.unwheeze.database;

import com.google.gson.reflect.TypeToken;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Cursor;
import com.unwheeze.beans.User;

import java.util.HashMap;

public class UnwheezeDbUsers extends UnwheezeDb {

    public UnwheezeDbUsers() {
        super();
    }

    public int putUserInCollection(User user) {
        String jsonUser = super.gson.toJson(user);

        MapObject userHashMap = super.gson.fromJson(jsonUser,new TypeToken<MapObject>(){}.getType());
        HashMap<String,Object> result = r.table(USERTABLE).insert(userHashMap)
                .run(connection);

        return (int)((long)result.get("errors"));
    }

    public boolean isUserInCollection(String data,String field) {

        Cursor cursor = r.table(USERTABLE)
                .filter(row -> row.getField(field).eq(data))
                .run(connection);

        if(cursor.bufferedItems().size()>=1) {

            return true;
        }

        return false;
    }

    public String getUserFromCollection(String data,String field) throws IllegalAccessException {


        Cursor cursor = r.table(USERTABLE)
                .filter(row -> row.getField(field)
                        .eq(data))
                .run(connection);

        if(!cursor.hasNext())
            throw new IllegalAccessException("User was not found");

        HashMap<Object,String> user = (HashMap<Object,String>) cursor.next();
        String jsonUser = super.gson.toJson(user,new TypeToken<HashMap<Object,String>>(){}.getType());

        return jsonUser;

    }

    public MapObject removeUser(String field) {

        return r.table(USERTABLE).get(field).delete().optArg("return_changes",true)
                .run(connection);

    }
}
