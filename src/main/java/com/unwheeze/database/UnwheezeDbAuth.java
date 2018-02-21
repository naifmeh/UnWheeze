package com.unwheeze.database;

import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Cursor;
import com.unwheeze.beans.AuthClient;
import com.unwheeze.exception.NoApiKeyFoundException;

import java.util.HashMap;

public class UnwheezeDbAuth extends UnwheezeDb {

    public UnwheezeDbAuth() {
        super();
    }

    //----------------------------- AUTH

    public int insertAuthKey(AuthClient auth) {
        MapObject authMap = super.gson.fromJson(gson.toJson(auth),MapObject.class);
        HashMap<String,Object> result = super.r.table(AUTHTABLE)
                .insert(authMap)
                .run(connection);

        return (int)((long) result.get("errors"));
    }

    public boolean isApiKeyInDb(String authKey) throws NoApiKeyFoundException{
        Cursor cursor = r.table(AUTHTABLE)
                .filter(row -> row.getField("key").eq(authKey))
                .run(connection);

        if(cursor.bufferedItems().size()>=1) {

            return true;
        }

        throw new NoApiKeyFoundException();
    }

    public MapObject removeKey(String authKey) {
        return r.table(AUTHTABLE)
                .filter(row -> row.getField(DbScheme.WSAUTH_KEY).eq(authKey))
                .delete()
                .optArg("return_changes",true)
                .run(connection);

    }

}
