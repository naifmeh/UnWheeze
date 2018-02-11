package com.unwheeze.CrudResources;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class CustomJsonReader implements MessageBodyReader<String> {
    private static final Logger log = LogManager.getLogger(CustomJsonReader.class.getName());

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public String readFrom(Class<String> aClass, Type type, Annotation[] annotations,
                           MediaType mediaType, MultivaluedMap<String, String> multivaluedMap,
                           InputStream inputStream)
            throws IOException, WebApplicationException {

        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream,writer,"UTF-8");
        String json = writer.toString();

        if(json.equals("")) log.error("No JSON body found");
        if(String.class == type)
            return aClass.cast(json);

        return json;
        //TODO: A ameliorer

    }
}
