package com.unwheeze.utils;

import com.sun.deploy.security.AuthKey;
import com.unwheeze.beans.AuthClient;
import com.unwheeze.database.UnwheezeDbAuth;
import junit.framework.TestCase;
import org.junit.Test;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.*;

public class SecurityVerificationUtilsTest extends TestCase {

    @Test
    public void testIsApiKeyValid() {
        HttpHeaders headers = new HttpHeaders() {
            @Override
            public List<String> getRequestHeader(String s) {
                return null;
            }

            @Override
            public String getHeaderString(String s) {
                String key = "";
               if(s.equals("X-Api-Key")){
                   key = (new UnwheezeDbAuth()).generateUUID();
                   (new UnwheezeDbAuth()).insertAuthKey(new AuthClient(key));
                    return key;
               } else return null;

            }

            @Override
            public MultivaluedMap<String, String> getRequestHeaders() {
                return null;
            }

            @Override
            public List<MediaType> getAcceptableMediaTypes() {
                return null;
            }

            @Override
            public List<Locale> getAcceptableLanguages() {
                return null;
            }

            @Override
            public MediaType getMediaType() {
                return null;
            }

            @Override
            public Locale getLanguage() {
                return null;
            }

            @Override
            public Map<String, Cookie> getCookies() {
                return null;
            }

            @Override
            public Date getDate() {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }
        };

        boolean isApiKey = SecurityVerificationUtils.isApiKeyValid(headers);

        assertEquals(true,isApiKey);
    }
}
