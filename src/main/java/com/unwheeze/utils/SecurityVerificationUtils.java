package com.unwheeze.utils;

import com.rethinkdb.gen.ast.Http;
import com.unwheeze.database.UnwheezeDbAuth;
import com.unwheeze.exception.NoApiKeyFoundException;
import com.unwheeze.security.JWTBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.bouncycastle.util.encoders.Base64;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SecurityVerificationUtils {

    public static boolean isApiKeyValid(HttpHeaders headers) {
        boolean apiHeader = headers.getHeaderString("X-Api-Key").isEmpty();
        if(apiHeader)
            return false;

        String apiKey = headers.getHeaderString("X-Api-Key");
        UnwheezeDbAuth db = new UnwheezeDbAuth();
        try {
            boolean isValid = db.isApiKeyInDb(apiKey);
            return true;
        } catch (NoApiKeyFoundException e) {
            return false;
        }
    }

    public static boolean hasUserAuthToken(HttpHeaders headers) {
        return !headers.getHeaderString("Authorization").isEmpty();
    }

    public static Map<String,Object> userCredentials(HttpHeaders headers) {
        HashMap<String,Object> resultMap = new HashMap<>();

        String authHeader = headers.getHeaderString("Authorization");
        String jwt;
        try {
            jwt = new String(Base64.decode(authHeader.split("Bearer")[1]),"UTF-8");
        } catch(UnsupportedEncodingException e) {
            resultMap.put("error",e.getMessage());
            resultMap.put("errorCode",-1);
            return resultMap;
        }

        Claims claims;
        try {
            claims = (new JWTBuilder()).getClaims(jwt);
        } catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                | IllegalArgumentException | IOException e) {
            resultMap.put("error",e.getMessage());
            resultMap.put("errorCode",-2);
            return resultMap;
        }

        if(claims.isEmpty()){
            resultMap.put("error","empty claims");
            resultMap.put("errorCode",-3);
            return resultMap;
        }

        resultMap.put("subject",claims.getSubject());
        resultMap.put("issuedAt",claims.getIssuedAt());
        resultMap.put("issuer",claims.getIssuer());
        resultMap.put("scope",claims.get("scope"));
        resultMap.put("error","");
        resultMap.put("errorCode",0);

        return resultMap;

    }
}
