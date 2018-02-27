package com.unwheeze.CrudResources;

import com.google.gson.Gson;
import com.rethinkdb.gen.ast.Http;
import com.unwheeze.beans.AirData;
import com.unwheeze.database.DbScheme;
import com.unwheeze.database.UnwheezeDbAirData;
import com.unwheeze.database.UnwheezeDbAuth;
import com.unwheeze.exception.NoApiKeyFoundException;
import com.unwheeze.utils.JsonErrorStatus;
import com.unwheeze.utils.SecurityVerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("airData/")
public class AirResources {
    private static final Logger log = LogManager.getLogger(AirResources.class.getName());
    private String AIRTABLE = DbScheme._AIRDATA;

    private Gson gson = new Gson();
    private UnwheezeDbAirData db;
    //TODO:Complete this class



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/putAirData")
    public Response insertData(String json, @Context HttpHeaders headers) {
        boolean isApiKeyValid = SecurityVerificationUtils.isApiKeyValid(headers);
        if(!isApiKeyValid)
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(JsonErrorStatus.errorInvalidApiKey)
                    .build();

        AirData airData = gson.fromJson(json,AirData.class);
        db = new UnwheezeDbAirData();
        int reponse = db.putDataInCollection(airData);
        if(reponse < 0)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();

        return Response.status(Response.Status.OK).build();

    }

    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAirCollection")
    public Response getCollectionData(String json,@Context HttpHeaders headers) {
        boolean isApiKeyValid = SecurityVerificationUtils.isApiKeyValid(headers);
        if(!isApiKeyValid)
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(JsonErrorStatus.errorInvalidApiKey)
                    .build();
        db = new UnwheezeDbAirData();
        String reponse;
        try {
            reponse = db.getAllDataFromCollection();
        } catch (IllegalAccessException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }



        return Response.status(Response.Status.OK).entity(reponse).build();


    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAirData/{id}")
    public Response getAirData(@Context HttpHeaders headers,@PathParam("id") String id) {
        boolean isApiKeyValid = SecurityVerificationUtils.isApiKeyValid(headers);
        if(!isApiKeyValid)
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(JsonErrorStatus.errorInvalidApiKey)
                    .build();


        db = new UnwheezeDbAirData();
        String reponse;
        try {
            reponse = db.getDataFromCollection(id);
        } catch (IllegalAccessException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }



        return Response.status(Response.Status.OK).entity(reponse).build();
    }


}
