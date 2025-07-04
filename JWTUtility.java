package utils;

import common.Parameters;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.Date;



public class JWTUtility {

    public static String generateToken(String username) {
        try {

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issuer(Parameters.APP_URL)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + 3600000))
                    .build();

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                    .type(JOSEObjectType.JWT)
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(new MACSigner(Parameters.SECRET_KEY));

            return signedJWT.serialize();

        } catch (JOSEException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIssuerFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getIssuer();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean validateToken(String token) {
        try {

            SignedJWT signedJWT = SignedJWT.parse(token);

            MACVerifier verifier = new MACVerifier(Parameters.SECRET_KEY);

            if (signedJWT.verify(verifier)) {
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                Date expirationTime = claims.getExpirationTime();

                if (expirationTime != null && expirationTime.after(new Date())) {
                    return true;
                }
            }
        } catch (ParseException | JOSEException e) {

            System.out.println("Error while validating token: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static String getUsernameFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            return claims.getSubject();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
