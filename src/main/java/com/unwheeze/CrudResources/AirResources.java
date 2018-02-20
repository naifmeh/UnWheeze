package com.unwheeze.CrudResources;

import com.unwheeze.database.DbScheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
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
    public Response insertData(String json) {
        return null;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAirCollection")
    public Response getCollectionData(String json) {
        return null;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAirData")
    public Response getAirData(String json) {
        return null;
    }


}
