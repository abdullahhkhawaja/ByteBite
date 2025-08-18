package utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import common.Parameters;

import java.text.ParseException;
import java.util.Date;


public class JWTUtility {

    public static String generateToken(int userID) {
        try {

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(String.valueOf(userID))
                    .issuer(Parameters.App_URL)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + 3600000))
                    .build();

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                    .type(JOSEObjectType.JWT)
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(new MACSigner(Parameters.Secret_Key));

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

    public static boolean validateToken(String token, int userID) {
        try {

            SignedJWT signedJWT = SignedJWT.parse(token);

            MACVerifier verifier = new MACVerifier(Parameters.Secret_Key);

            if (signedJWT.verify(verifier)) {
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                Date expirationTime = claims.getExpirationTime();
                                                                                        //change return type to response
                                                                                        //add valid responses baasd on output
                String tokenUserID = claims.getSubject();
                if(!tokenUserID.equals(String.valueOf(userID))){
                    return false;
                }

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

//    public static String getEmailFromToken(String token) {
//        try {
//            SignedJWT signedJWT = SignedJWT.parse(token);
//            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
//            return claims.getSubject();
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}

