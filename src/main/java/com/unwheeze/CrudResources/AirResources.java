package com.unwheeze.CrudResources;

import com.google.gson.Gson;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Point;
import com.unwheeze.beans.AirData;
import com.unwheeze.beans.GeoBean;
import com.unwheeze.database.DbScheme;
import com.unwheeze.database.UnwheezeDbAirData;
import com.unwheeze.utils.JsonErrorStatus;
import com.unwheeze.utils.SecurityVerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

@Path("airData/")
public class AirResources {
    private static final Logger log = LogManager.getLogger(AirResources.class.getName());
    private String AIRTABLE = DbScheme._AIRDATA;

    private Gson gson = new Gson();
    private UnwheezeDbAirData db;
    //TODO:Complete this class


    /**
     * /putAirData endpoint allow to insert an air data json object in the database
     * Must have the following fields : pm25,pm10,pm1 float format ; location string with latitude,longitude and userId (optionnal)
     * @param json
     * @param headers
     * @return
     */
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

        String parsedLocation[] = airData.getLocation().split(",");
        airData.setGeolocation(new GeoBean(Arrays.stream(parsedLocation)
                            .mapToDouble(Double::parseDouble)
                            .toArray()));

        db = new UnwheezeDbAirData();
        int reponse = db.putDataInCollection(airData);
        if(reponse < 0)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();

        return Response.status(Response.Status.OK).build();

    }

    /**
     * Return JsonArray containing the airData table content
     * @param json
     * @param headers
     * @return Stringified JSONARRAY containing database content
     */
    @GET
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

    /**
     * /getAirData/{id} get specific Id airData
     * @param headers
     * @param id
     * @return
     */
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

    /**
     * /getNearest/{location} returns the nearest point to the specified location.
     * Location must be of format Lat,Long
     * Search radius is of 20 meters by default.
     * See /getNearest/{location}/{radius} for a custom radius.
     * @param location
     * @return JsonObject containing the nearest point and it's attributes
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("")
    @Path("/getNearest/{location}")
    public Response getNearest(@Context HttpHeaders headers,@PathParam("location") String location) {
        boolean isApiKeyValid = SecurityVerificationUtils.isApiKeyValid(headers);
        if(!isApiKeyValid)
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(JsonErrorStatus.errorInvalidApiKey)
                    .build();

        String[] locationArray = location.split(",");
        if(locationArray.length != 2) return Response.status(Response.Status.BAD_REQUEST)
                                                    .entity(JsonErrorStatus.errorInvalidLocationParam)
                                                    .build();

        Point point = RethinkDB.r.point(Double.parseDouble(locationArray[0]),Double.parseDouble(locationArray[1]));

        db = new UnwheezeDbAirData();

        return Response.status(Response.Status.OK).entity(db.getNearestPoint(point,20,20)).build();
    }

    /**
     *
     * Same as getNearest except the radius is specified ../{radius}
     * @param headers
     * @param location
     * @param radius
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getNearest/{location}/{radius}")
    public Response getNearestWithRadius(@Context HttpHeaders headers,@PathParam("location") String location,
                                         @PathParam("radius") int radius) {
        boolean isApiKeyValid = SecurityVerificationUtils.isApiKeyValid(headers);
        if(!isApiKeyValid)
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(JsonErrorStatus.errorInvalidApiKey)
                    .build();

        String[] locationArray = location.split(",");
        if(locationArray.length != 2) return Response.status(Response.Status.BAD_REQUEST)
                .entity(JsonErrorStatus.errorInvalidLocationParam)
                .build();

        Point point = RethinkDB.r.point(Double.parseDouble(locationArray[0]),Double.parseDouble(locationArray[1]));

        db = new UnwheezeDbAirData();

        return Response.status(Response.Status.OK).entity(db.getNearestPoint(point,radius,20)).build();
    }



}
