package com.unwheeze.security;


import com.sun.istack.internal.Nullable;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;

import javax.ws.rs.core.HttpHeaders;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;


public class JWTBuilder {
    private static final Logger log = LogManager.getLogger(JWTBuilder.class.getSimpleName());

    private String id;
    private String issuer;
    private String scope;
    private String email;
    private Date expDate;
    private final long TOKEN_DURATION = 7200;

    private String pathPvKey;

    public JWTBuilder() {
    }

    public JWTBuilder(String id,String email,String scope, String issuer) {
        this.id = id;
        this.email = email;
        this.scope = scope;
        this.issuer = issuer;
        this.expDate = new Date((new Date()).getTime()+TOKEN_DURATION);

    }

    public JWTBuilder(String id,String email,String scope, String issuer, Date expDate) {
        this(id,email,scope,issuer);
        this.expDate = expDate;
    }

    public String getSignatureKey() throws IOException {
        String key="";
        InputStreamReader in = new InputStreamReader(
                new FileInputStream(pathPvKey),"UTF-8"); {
                StringBuilder builder = new StringBuilder();
                int content;
                while((content = in.read()) != -1) {
                    builder.append((char) content);
                }
                key = builder.toString();
            }
        in.close();

        return key;
    }

    public String buildJWT() {
        String key = "";
        try {
            key = this.getSignatureKey();
        } catch(IOException e) {
            e.printStackTrace();
        }

        String jwt = Jwts.builder()
                .setSubject("user/"+this.id)
                .setIssuer(this.issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(this.expDate)
                .claim("scope",this.scope)
                .claim("email",this.email)
                .signWith(SignatureAlgorithm.HS256,key)
                .compact();

        return jwt;
    }

    public Claims getClaims(String jwt) throws SecurityException,IOException {
        Claims claims = Jwts.parser()
                .setSigningKey(this.getSignatureKey())
                .parseClaimsJws(jwt)
                .getBody();

        return claims;
    }

    /**
     * Retourne -1 en cas d'absence du header Authorization
     * -2 en cas d'erreur d'encodage du jwt
     * -3 en cas d'erreur du jwt
     * -4 en cas d'absence des claims
     * 0 si tout va bien
     * @param headers
     * @return
     */
    public static Bearer checkJwtBearer(HttpHeaders headers) {
        String HEADER_AUTH = "Authorization";

        if(headers.getHeaderString(HEADER_AUTH) == null)
            return new Bearer(-1,null);

        String authHeader = headers.getHeaderString(HEADER_AUTH);
        String jwt;

        try {
            jwt = new String(Base64.decode(
                    authHeader.split("Bearer")[1]),
                    "UTF-8");
        } catch(UnsupportedEncodingException e) {
            log.error("Unsupported encoding exception on token : "+e.getMessage());
            return new Bearer(-3,null);
        }
        Claims claims = new DefaultClaims();
        try {
            claims = (new JWTBuilder()).getClaims(jwt);
        } catch(SecurityException | IOException e) {
            log.error(e.getMessage());
            return new Bearer(-3,null);
        }

        if(claims.isEmpty()) return new Bearer(-4,null);

        return new Bearer(0,claims);
    }

    public static class Bearer {
        private int response;
        @Nullable private Claims claims;

        public Bearer(int response, Claims claims) {
            this.response = response;
            this.claims = claims;
        }

        public int getResponse() {
            return response;
        }

        public void setResponse(int response) {
            this.response = response;
        }

        public Claims getClaims() {
            return claims;
        }

        public void setClaims(Claims claims) {
            this.claims = claims;
        }
    }
    

}
