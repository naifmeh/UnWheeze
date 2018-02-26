package com.unwheeze.CrudResources;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rethinkdb.gen.ast.Json;
import com.rethinkdb.model.MapObject;
import com.unwheeze.beans.User;
import com.unwheeze.database.DbScheme;
import com.unwheeze.database.UnwheezeDb;
import com.unwheeze.database.UnwheezeDbUsers;
import com.unwheeze.security.FieldCrypter;
import com.unwheeze.security.JWTBuilder;
import com.unwheeze.utils.ByteConverter;
import com.unwheeze.utils.ClientScopes;
import com.unwheeze.utils.JsonErrorStatus;
import com.unwheeze.utils.SecurityVerificationUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.regex.Pattern;


@Path("users/")
public class UserResources {

    private final static Logger log = LogManager.getLogger(UserResources.class.getName());

    Gson gson = new Gson();
    private final String REGEX_EMAIL="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertNewUser(String json,@Context HttpHeaders headers) {
        //TODO: Check API KEY

        UnwheezeDbUsers db = new UnwheezeDbUsers();
        JsonObject userJson = gson.fromJson(json,JsonObject.class);

        String email = userJson.get(DbScheme.USERS_EMAIL).getAsString();
        boolean userInDb = db.isUserInCollection(email, DbScheme.USERS_EMAIL);
        if(userInDb)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(JsonErrorStatus.errorEmailInDb)
                    .build();

        String pwd = "";
        try {
            pwd = new String(Base64.decode(userJson.get(DbScheme.USERS_PWD).getAsString()),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(pwd.isEmpty() || pwd == null){
            String errorCodeEmptyPwd = "{'errorStatus':'Password field is empty'}";
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity(errorCodeEmptyPwd)
                    .build();
        }
        //-----Compute pwd

        byte[] salt = FieldCrypter.generateRandomSalt();
        byte[] encryptedPwd = FieldCrypter.generateHash(pwd.toCharArray(),salt);
        userJson.addProperty(DbScheme.USERS_SALT, ByteConverter.HexToStr(salt));
        userJson.addProperty(DbScheme.USERS_PWD,ByteConverter.HexToStr(encryptedPwd));
        //-----------------
        String uuid = db.generateUUID();
        userJson.addProperty(DbScheme.USERS_ID,uuid);
        User user = gson.fromJson(userJson,User.class);

        //------ Verifying email
        boolean matchingEmail = Pattern.compile(this.REGEX_EMAIL)
                .matcher(email)
                .matches();
        if(!matchingEmail)
            return Response.status(Response.Status.EXPECTATION_FAILED)
            .entity(JsonErrorStatus.errorInvalidFormatEmail)
            .build();
        
        //TODO : Send confirmation email
        int responseInsert = db.putUserInCollection(user);

        switch(responseInsert) {
            case -1:
                log.error(JsonErrorStatus.errorCodeInsertion);
                return Response.status(Response.Status.EXPECTATION_FAILED)
                        .entity(JsonErrorStatus.errorCodeInsertion)
                        .build();
            case -2:
                log.error(JsonErrorStatus.errorDbNotInit);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(JsonErrorStatus.errorDbNotInit)
                        .build();
            default:
                log.debug("Insertion done with no errors");
                String userDoc = "";
                try {
                    userDoc = db.getUserFromCollection(email, DbScheme.USERS_EMAIL);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                return Response.status(Response.Status.OK)
                        .entity(userDoc)
                        .build();
        }

    }


    @POST
    @Path("/signin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response signInUser(@Context HttpHeaders header) {
        log.info("Signing in user");

        HashMap<String,Object> claimsMap = (HashMap<String,Object>) SecurityVerificationUtils.userCredentials(header);

        Response respErrorCode = checkErrorCodes((int)claimsMap.get("errorCode"),(String)claimsMap.get("error"));
        if(respErrorCode != null)
            return respErrorCode;


        String userId = ((String)claimsMap.get("subject")).split("user/")[1];
        UnwheezeDbUsers db = new UnwheezeDbUsers();

        boolean userExists = db.isUserInCollection(userId,DbScheme.USERS_ID);

        if(!userExists)
            return Response.status(Response.Status.BAD_REQUEST).entity(JsonErrorStatus.errorEmailNotInDb).build();

        String userDoc;
        try {
            userDoc = db.getUserFromCollection(userId,DbScheme.USERS_ID);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        JsonObject jsonUser = gson.fromJson(userDoc, JsonObject.class);
        jsonUser.remove(DbScheme.USERS_PWD);
        jsonUser.remove(DbScheme.USERS_SALT);

        userDoc = gson.toJson(jsonUser);

        return Response.status(Response.Status.OK)
                .entity(userDoc).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/remove")
    public Response deleteUser(@Context HttpHeaders headers) {
        //TODO : Rewrite this shit

        HashMap<String,Object> claimsMap = (HashMap<String,Object>) SecurityVerificationUtils.userCredentials(headers);

        Response respErrorCode = checkErrorCodes((int)claimsMap.get("errorCode"),(String)claimsMap.get("error"));
        if(respErrorCode != null)
            return respErrorCode;

        String userScope = (String)claimsMap.get("scope");
        String userId = ((String)claimsMap.get("subject")).split("user/")[1];
        //TODO: verifier user a les droits nescessaire
        UnwheezeDbUsers db = new UnwheezeDbUsers();
       HashMap<String,Object> reponse = db.removeUser(userId);

        int errors = (int)((long) reponse.get("errors"));
        if(errors != 0)
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .build();

        return Response.status(Response.Status.OK)
                .entity(reponse.toString())
                .build();

    }


    public Response checkErrorCodes(int code,String error) {
        switch(code) {
            case -1:
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(error)
                        .build();
            case -2:
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(error)
                        .build();
            case -3:
                return Response.status(Response.Status.EXPECTATION_FAILED)
                        .entity(JsonErrorStatus.errorInvalidAuthTkn)
                        .build();
            default:
                break;
        }
        return null;
    }


}
