package com.unwheeze.CrudResources;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.rethinkdb.gen.ast.Http;
import com.unwheeze.beans.AuthClient;
import com.unwheeze.beans.User;
import com.unwheeze.database.DbScheme;
import com.unwheeze.database.UnwheezeDb;
import com.unwheeze.database.UnwheezeDbAuth;
import com.unwheeze.database.UnwheezeDbUsers;
import com.unwheeze.security.FieldCrypter;
import com.unwheeze.security.JWTBuilder;
import com.unwheeze.utils.ByteConverter;
import com.unwheeze.utils.JsonErrorStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
import org.bouncycastle.util.encoders.Base64;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;


@Path("auth/")
public class UnwheezeResources {
    private static final Logger log = LogManager.getLogger(UnwheezeResources.class.getSimpleName());
    private final String AUTH_HEADER = "Authorization";

    private Gson gson = new Gson();

    private String ISSUER = "UnWheeze";

    @GET
    @Path("/clientToken")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authNotConnected(@Context HttpHeaders headers) {
        //TODO : VERIFY ORIGIN HEADER TO ONLY AUTH CLIENT FROM UNWHEEZE


        UnwheezeDb db = new UnwheezeDbAuth();
        String key = db.generateUUID();

        AuthClient auth = new AuthClient(key);
        int response = ((UnwheezeDbAuth) db).insertAuthKey(auth);

        if(response < 0)
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .build();


        return Response.status(Response.Status.OK).entity(gson.toJson(auth))
                .build();
    }

    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authClient(@Context HttpHeaders headers) {
        String authHeader = headers.getHeaderString(AUTH_HEADER);
        String authContent = authHeader.split("Basic")[1];

        if(authContent.isEmpty() || authContent == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        byte[] decodedHeader = Base64.decode(authContent);

        String[] decodedStr = {"",""};
        try {
            log.debug(new String(decodedHeader, "UTF-8"));
            decodedStr = (new String(decodedHeader,"UTF-8")).split(":");
        } catch(UnsupportedEncodingException e) {
            log.error(e.getMessage()+" (Decoding failed)");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        String email = decodedStr[0];
        String pwd = decodedStr[1];
        log.debug("Initializing client verification for user "+email);
        UnwheezeDbUsers db = new UnwheezeDbUsers();
        boolean dbResp = false;

        dbResp = db.isUserInCollection(email, DbScheme.USERS_EMAIL);
        System.out.println(dbResp);
        if(!dbResp)
            return Response.status(Response.Status.BAD_REQUEST).build();

        String jsonUser= "";
        try {
            jsonUser = db.getUserFromCollection(email, DbScheme.USERS_EMAIL);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        User user = gson.fromJson(jsonUser,User.class);
        String password = user.getPwd();
        String salt = user.getSalt();

        boolean isHashCorrect = FieldCrypter.isExpectedHash(pwd.toCharArray(), ByteConverter.hexStringToByteArray(salt),
                ByteConverter.hexStringToByteArray(password));
        if(!isHashCorrect)
            return Response.status(Response.Status.FORBIDDEN).entity(JsonErrorStatus.errorIncorrectCred).build();

        JWTBuilder jwtBuilder = new JWTBuilder(user.getid(),user.getEmail(),"read,upload",ISSUER);
        String jwt = jwtBuilder.buildJWT();

        return Response.status(Response.Status.OK)
                .header("Cache-Control","no-cache")
                .header("Pragma","no-cache")
                .entity(jwt)
                .build();

    }
}
