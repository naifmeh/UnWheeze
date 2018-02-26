package com.unwheeze.CrudResources;

import com.rethinkdb.gen.ast.Http;
import com.unwheeze.database.DbScheme;
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


public class AirResources {
    private static final Logger log = LogManager.getLogger(AirResources.class.getName());
    private String AIRTABLE = DbScheme._AIRDATA;

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




        return null;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAirCollection/{area}")
    public Response getCollectionData(String json) {
        //TODO : Check security key first
        return null;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAirData")
    public Response getAirData(String json) {
        //TODO : Check security key first
        return null;
    }


}
